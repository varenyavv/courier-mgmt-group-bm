package bits.mt.ss.dda.groupbm.couriermgmt.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bits.mt.ss.dda.groupbm.couriermgmt.dao.AgentDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.BranchDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.CustomerDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.EmployeeDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.RouterDao;
import bits.mt.ss.dda.groupbm.couriermgmt.dao.ShipmentDao;
import bits.mt.ss.dda.groupbm.couriermgmt.enums.Status;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.CommonErrors;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.EntityNotFoundException;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.ForbiddenException;
import bits.mt.ss.dda.groupbm.couriermgmt.exception.UnauthorizedException;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Agent;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Branch;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Customer;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Employee;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Hop;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Route;
import bits.mt.ss.dda.groupbm.couriermgmt.model.Shipment;
import bits.mt.ss.dda.groupbm.couriermgmt.model.ShipmentTracker;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.BookShipmentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.ForwardShipmentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.BookShipmentResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.ForwardShipmentResponse;

@Service
public class ShipmentService {

  @Autowired AgentDao agentDao;

  @Autowired CustomerDao customerDao;

  @Autowired RandomRouteAllocator routeAllocator;

  @Autowired ShipmentDao shipmentDao;

  @Autowired BranchDao branchDao;

  @Autowired EmployeeDao employeeDao;

  @Autowired RouterDao routerDao;

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

  public List<Hop> validateForwardShipmentRequest(
      long employeeId, ForwardShipmentRequest forwardShipmentRequest) {

    Employee employee = employeeDao.getEmployeeById(employeeId);

    if (null == employee) {
      throw new UnauthorizedException(
          CommonErrors.UNAUTHORIZED,
          String.format("Employee with Id %s doesn't exist", employeeId));
    }

    Branch receivingBranch =
        branchDao.getBranchByBranchCode(forwardShipmentRequest.getReceivingBranchCode());
    if (null == receivingBranch) {
      throw new EntityNotFoundException(
          CommonErrors.ENTITY_NOT_FOUND,
          "Branch",
          "branch code",
          forwardShipmentRequest.getReceivingBranchCode());
    }

    Shipment shipment =
        shipmentDao.getShipmentByConsignmentNumber(forwardShipmentRequest.getConsignmentNumber());

    if (null == shipment) {
      throw new EntityNotFoundException(
          CommonErrors.ENTITY_NOT_FOUND,
          "Shipment",
          "consignment number",
          forwardShipmentRequest.getConsignmentNumber());
    }

    if (!employee
        .getBranch()
        .getBranchCode()
        .equals(forwardShipmentRequest.getReceivingBranchCode())) {
      throw new UnauthorizedException(
          CommonErrors.UNAUTHORIZED,
          String.format(
              "Employee with Id %s is not allowed to update the status of the shipment received at branch %s "
                  + "since s/he belongs to a different branch.",
              employeeId, forwardShipmentRequest.getReceivingBranchCode()));
    }

    return routerDao.getRouteBetweenSourceAndDestination(
        shipment.getSourcePincode(), shipment.getDestPincode());
  }

  public ForwardShipmentResponse forwardShipment(
      long employeeId, ForwardShipmentRequest forwardShipmentRequest, List<Hop> routeToFollow) {

    Employee employee = employeeDao.getEmployeeById(employeeId);
    assertNotNull(employee, "Employee must not be null by now.");

    List<ShipmentTracker> shipmentHistorySortedByDateDesc =
        shipmentDao.getShipmentHistoryByConsignmentNumber(
            forwardShipmentRequest.getConsignmentNumber());
    assertNotEmpty(shipmentHistorySortedByDateDesc, "Shipment history must not be empty by now.");

    ForwardShipmentResponse response =
        new ForwardShipmentResponse()
            .setConsignmentNumber(forwardShipmentRequest.getConsignmentNumber());

    ShipmentTracker latestTrackerRecord = shipmentHistorySortedByDateDesc.get(0);

    ShipmentTracker newTrackerRecord =
        new ShipmentTracker()
            .setShipment(latestTrackerRecord.getShipment())
            .setEmployee(employee)
            .setCreationDateTime(LocalDateTime.now())
            .setStatusRemarks(forwardShipmentRequest.getStatusRemarks());

    switch (latestTrackerRecord.getStatus()) {
      case BOOKED:
        Branch sourceBranch = routeToFollow.get(0).getBranch();

        if (!forwardShipmentRequest.getReceivingBranchCode().equals(sourceBranch.getBranchCode())) {
          throw new ForbiddenException(
              CommonErrors.INVALID_RECEIVING_BRANCH,
              forwardShipmentRequest.getReceivingBranchCode(),
              sourceBranch.getBranchCode());
        }

        newTrackerRecord.setStatus(Status.RECEIVED_AT_SOURCE_BRANCH);
        newTrackerRecord.setCurrentBranch(sourceBranch);

        shipmentDao.updateShipmentStatus(newTrackerRecord);

        response.setStatus(Status.RECEIVED_AT_SOURCE_BRANCH.toString());
        response.setReceiveDateTime(
            newTrackerRecord.getCreationDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        break;
      case RECEIVED_AT_SOURCE_BRANCH:
      case IN_TRANSIT:
        break;
      default:
        throw new ForbiddenException(
            CommonErrors.UPDATE_NOT_ALLOWED_AT_BRANCH, latestTrackerRecord.getStatus().toString());
    }

    return response;
  }
}
