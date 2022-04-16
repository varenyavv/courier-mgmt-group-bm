package bits.mt.ss.dda.groupbm.couriermgmt.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bits.mt.ss.dda.groupbm.couriermgmt.dao.AgentDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.CustomerDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.ShipmentDao;
import bits.mt.ss.dda.groupbm.couriermgmt.enums.Status;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.CommonErrors;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.UnauthorizedException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Agent;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Customer;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Route;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Shipment;
import bits.mt.ss.dda.groupbm.couriermgmt.model.ShipmentTracker;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.BookShipmentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.BookShipmentResponse;

@Service
public class ShipmentService {

  @Autowired AgentDao agentDao;

  @Autowired CustomerDao customerDao;

  @Autowired RandomRouteAllocator routeAllocator;

  @Autowired ShipmentDao shipmentDao;

  public Route validateBookShipmentRequest(
      long agentContactNumber, BookShipmentRequest bookShipmentRequest) {

    Agent agent = agentDao.getAgentById(agentContactNumber);

    if (null == agent) {
      throw new UnauthorizedException(
          CommonErrors.UNAUTHORIZED,
          String.format("Agent with contact number %s doesn't exist", agentContactNumber));
    }

    boolean insertCustomer = false;
    Customer customer = customerDao.getCustomerById(bookShipmentRequest.getCustomerContactNum());

    if (null == customer) {
      validateSenderDetails(bookShipmentRequest);
      customer =
          new Customer(
              bookShipmentRequest.getCustomerContactNum(),
              bookShipmentRequest.getCustomerName(),
              bookShipmentRequest.getSourceAddressLine(),
              bookShipmentRequest.getSourcePincode(),
              bookShipmentRequest.getSourceCity(),
              bookShipmentRequest.getSourceState().toUpperCase(Locale.ROOT));
      insertCustomer = true;
    } else {
      updateExistingCustomerDetails(bookShipmentRequest, customer);
    }

    if (customer.getState().length() != 2) {
      throw new IllegalArgumentException("Valid two characters' source state code is required.");
    }

    Route route =
        routeAllocator.findRoute(
            new Shipment()
                .setSourcePincode(customer.getPincode())
                .setDestPincode(bookShipmentRequest.getDestPincode())
                .setLengthInCm(bookShipmentRequest.getLengthInCm())
                .setWidthInCm(bookShipmentRequest.getWidthInCm())
                .setHeightInCm(bookShipmentRequest.getHeightInCm())
                .setWeightInGram(bookShipmentRequest.getWeightInGram()));

    if (!agent.getBranch().getBranchCode().equals(route.getDistance().getSourceBranchCode())) {
      throw new UnauthorizedException(
          CommonErrors.UNAUTHORIZED,
          "Agent's registered branch is not same as the shipment's source branch");
    }

    if (!route.getCost().equals(bookShipmentRequest.getBookingAmount())) {
      throw new IllegalArgumentException(
          String.format(
              "Booking amount should be equal to the quoted amount of Rs %s", route.getCost()));
    }

    customerDao.upsertCustomer(customer, insertCustomer);

    return route;
  }

  private void updateExistingCustomerDetails(
      BookShipmentRequest bookShipmentRequest, Customer customer) {
    if (ObjectUtils.isNotEmpty(bookShipmentRequest.getCustomerName())
        && !customer.getName().equalsIgnoreCase(bookShipmentRequest.getCustomerName())) {
      customer.setName(bookShipmentRequest.getCustomerName());
    }
    if (ObjectUtils.isNotEmpty(bookShipmentRequest.getSourceAddressLine())
        && !customer
            .getAddressLine()
            .equalsIgnoreCase(bookShipmentRequest.getSourceAddressLine())) {
      customer.setAddressLine(bookShipmentRequest.getSourceAddressLine());
    }
    if (customer.getPincode() != bookShipmentRequest.getSourcePincode()) {
      customer.setPincode(bookShipmentRequest.getSourcePincode());
    }
    if (ObjectUtils.isNotEmpty(bookShipmentRequest.getSourceCity())
        && !customer.getCity().equalsIgnoreCase(bookShipmentRequest.getSourceCity())) {
      customer.setCity(bookShipmentRequest.getSourceCity());
    }
    if (ObjectUtils.isNotEmpty(bookShipmentRequest.getSourceState())
        && !customer.getState().equalsIgnoreCase(bookShipmentRequest.getSourceState())) {
      customer.setState(bookShipmentRequest.getSourceState().toUpperCase(Locale.ROOT));
    }
  }

  private void validateSenderDetails(BookShipmentRequest bookShipmentRequest) {
    if (bookShipmentRequest.getSourcePincode() < 100000
        || bookShipmentRequest.getSourcePincode() >= 1000000
        || ObjectUtils.isEmpty(bookShipmentRequest.getSourceAddressLine())
        || ObjectUtils.isEmpty(bookShipmentRequest.getSourceCity())
        || ObjectUtils.isEmpty(bookShipmentRequest.getSourceState())) {
      throw new IllegalArgumentException(
          "One or more fields related to sender's address are either missing or invalid. "
              + "Required fields are [sourceAddressLine,sourceCity,sourceState,sourcePincode]");
    }

    if (ObjectUtils.isEmpty(bookShipmentRequest.getCustomerName())) {
      throw new IllegalArgumentException("Sender's name is missing.");
    }
  }

  public BookShipmentResponse bookShipment(
      long agentContactNumber, BookShipmentRequest bookShipmentRequest, Route routeToFollow) {

    Agent agent = agentDao.getAgentById(agentContactNumber);
    assertNotNull(agent, "Agent can't be null by now");

    Customer customer = customerDao.getCustomerById(bookShipmentRequest.getCustomerContactNum());
    assertNotNull(agent, "Customer can't be null by now");

    Shipment shipment =
        new Shipment()
            .setSourcePincode(customer.getPincode())
            .setDestPincode(bookShipmentRequest.getDestPincode())
            .setLengthInCm(bookShipmentRequest.getLengthInCm())
            .setWidthInCm(bookShipmentRequest.getWidthInCm())
            .setHeightInCm(bookShipmentRequest.getHeightInCm())
            .setWeightInGram(bookShipmentRequest.getWeightInGram())
            .setCustomer(customer)
            .setDestAddressLine(bookShipmentRequest.getDestAddressLine())
            .setDestCity(bookShipmentRequest.getDestCity())
            .setDestState(bookShipmentRequest.getDestState())
            .setDistanceInKm(routeToFollow.getDistance().getDistanceInKm())
            .setBookingAmount(routeToFollow.getCost());

    ShipmentTracker shipmentTracker =
        new ShipmentTracker()
            .setShipment(shipment)
            .setAgent(agent)
            .setNexBranch(routeToFollow.getHops().get(0).getBranch())
            .setStatus(Status.BOOKED)
            .setStatusRemarks(bookShipmentRequest.getStatusRemarks())
            .setCreationDateTime(LocalDateTime.now());

    Shipment bookedShipment = shipmentDao.createShipment(shipmentTracker);

    return new BookShipmentResponse()
        .setShipmentId(bookedShipment.getShipmentId())
        .setConsignmentNumber(bookedShipment.getConsignmentNumber())
        .setBookingDateTime(
            shipmentTracker.getCreationDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }
}
