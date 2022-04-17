package bits.mt.ss.dda.groupbm.couriermgmt.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.DeliverShipmentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.request.ForwardShipmentRequest;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.BookShipmentResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.response.DeliverShipmentResponse;
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
        processBookedShipment(forwardShipmentRequest, routeToFollow, response, newTrackerRecord);
        break;
      case RECEIVED_AT_SOURCE_BRANCH:
      case IN_TRANSIT:
        forwardShipment(
            forwardShipmentRequest, routeToFollow, response, latestTrackerRecord, newTrackerRecord);
        break;
      default:
        throw new ForbiddenException(
            CommonErrors.UPDATE_NOT_ALLOWED_AT_BRANCH, latestTrackerRecord.getStatus().toString());
    }

    shipmentDao.updateShipmentStatus(newTrackerRecord);

    response.setReceiveDateTime(
        newTrackerRecord.getCreationDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    return response;
  }

  private void forwardShipment(
      ForwardShipmentRequest forwardShipmentRequest,
      List<Hop> routeToFollow,
      ForwardShipmentResponse response,
      ShipmentTracker latestTrackerRecord,
      ShipmentTracker newTrackerRecord) {
    Optional<Hop> currentHopOptional =
        routeToFollow.stream()
            .filter(hop -> getCurrentHopPredicate(latestTrackerRecord, hop))
            .findFirst();
    assertTrue(
        currentHopOptional.isPresent(),
        "Current hop can never be null since shipments are following the route as per route table");

    Hop currentHop = currentHopOptional.get();

    if (!forwardShipmentRequest
        .getReceivingBranchCode()
        .equals(currentHop.getBranch().getBranchCode())) {
      throw new ForbiddenException(
          CommonErrors.INVALID_RECEIVING_BRANCH,
          forwardShipmentRequest.getReceivingBranchCode(),
          currentHop.getBranch().getBranchCode());
    }

    if (null == currentHop.getShipVia()) {
      // reached destination branch
      newTrackerRecord.setStatus(Status.RECEIVED_AT_DEST_BRANCH);
      response.setStatus(Status.RECEIVED_AT_DEST_BRANCH.toString());
    } else {
      Hop nextHop = routeToFollow.get(currentHop.getHopCounter() + 1);

      assertNotNull(
          nextHop,
          "Next hop can never be null since shipments are following the route as per route table");

      newTrackerRecord.setStatus(Status.IN_TRANSIT);
      newTrackerRecord.setNexBranch(nextHop.getBranch());
      newTrackerRecord.setTransportMode(currentHop.getShipVia());
      response.setStatus(Status.IN_TRANSIT.toString());
    }

    newTrackerRecord.setCurrentBranch(currentHop.getBranch());
  }

  private boolean getCurrentHopPredicate(ShipmentTracker latestTrackerRecord, Hop hop) {
    if (null == latestTrackerRecord.getNexBranch()) {
      return latestTrackerRecord
          .getCurrentBranch()
          .getBranchCode()
          .equals(hop.getBranch().getBranchCode());
    }
    return latestTrackerRecord
        .getNexBranch()
        .getBranchCode()
        .equals(hop.getBranch().getBranchCode());
  }

  private void processBookedShipment(
      ForwardShipmentRequest forwardShipmentRequest,
      List<Hop> routeToFollow,
      ForwardShipmentResponse response,
      ShipmentTracker newTrackerRecord) {
    Hop firstHop = routeToFollow.get(0);

    if (!forwardShipmentRequest
        .getReceivingBranchCode()
        .equals(firstHop.getBranch().getBranchCode())) {
      throw new ForbiddenException(
          CommonErrors.INVALID_RECEIVING_BRANCH,
          forwardShipmentRequest.getReceivingBranchCode(),
          firstHop.getBranch().getBranchCode());
    }

    newTrackerRecord.setStatus(Status.RECEIVED_AT_SOURCE_BRANCH);
    newTrackerRecord.setCurrentBranch(firstHop.getBranch());
    response.setStatus(Status.RECEIVED_AT_SOURCE_BRANCH.toString());
  }

  public void validateDeliverShipmentRequest(
      long agentContactNumber, DeliverShipmentRequest deliverShipmentRequest) {

    Agent agent = agentDao.getAgentById(agentContactNumber);

    if (null == agent) {
      throw new UnauthorizedException(
          CommonErrors.UNAUTHORIZED,
          String.format("Agent with contact number %s doesn't exist", agentContactNumber));
    }

    Shipment shipment =
        shipmentDao.getShipmentByConsignmentNumber(deliverShipmentRequest.getConsignmentNumber());

    if (null == shipment) {
      throw new EntityNotFoundException(
          CommonErrors.ENTITY_NOT_FOUND,
          "Shipment",
          "consignment number",
          deliverShipmentRequest.getConsignmentNumber());
    }

    String destinationBranchCode = branchDao.getBranchCodeByPincode(shipment.getDestPincode());

    if (!agent.getBranch().getBranchCode().equals(destinationBranchCode)) {
      throw new UnauthorizedException(
          CommonErrors.UNAUTHORIZED,
          String.format(
              "Agent with contact number %s is not allowed to attempt the delivery of the shipment "
                  + "since s/he does not belong to the recipient's home branch %s.",
              agentContactNumber, destinationBranchCode));
    }
  }

  public DeliverShipmentResponse deliverShipment(
      long agentContactNumber, DeliverShipmentRequest deliverShipmentRequest) {

    Agent agent = agentDao.getAgentById(agentContactNumber);
    assertNotNull(agent, "Agent must not be null by now.");

    List<ShipmentTracker> shipmentHistorySortedByDateDesc =
        shipmentDao.getShipmentHistoryByConsignmentNumber(
            deliverShipmentRequest.getConsignmentNumber());
    assertNotEmpty(shipmentHistorySortedByDateDesc, "Shipment history must not be empty by now.");

    ShipmentTracker latestTrackerRecord = shipmentHistorySortedByDateDesc.get(0);

    switch (latestTrackerRecord.getStatus()) {
      case RECEIVED_AT_DEST_BRANCH:
      case UNDELIVERED:
        if (!Status.OUT_FOR_DELIVERY.toString().equals(deliverShipmentRequest.getStatus())) {
          throw new ForbiddenException(
              CommonErrors.INVALID_DELIVERY_ATTEMPT,
              deliverShipmentRequest.getStatus(),
              Status.OUT_FOR_DELIVERY.toString());
        }

        break;
      case OUT_FOR_DELIVERY:
        if (!Status.DELIVERED.toString().equals(deliverShipmentRequest.getStatus())
            && !Status.UNDELIVERED.toString().equals(deliverShipmentRequest.getStatus())) {
          throw new ForbiddenException(
              CommonErrors.INVALID_DELIVERY_ATTEMPT,
              deliverShipmentRequest.getStatus(),
              Status.DELIVERED + ", " + Status.UNDELIVERED);
        }

        if (agent.getContactNumber() != latestTrackerRecord.getAgent().getContactNumber()) {
          throw new ForbiddenException(
              CommonErrors.AGENT_NOT_AUTHORIZED,
              agent.getName(),
              latestTrackerRecord.getAgent().getName());
        }

        break;
      case DELIVERED:
        throw new ForbiddenException(CommonErrors.SHIPMENT_ALREADY_DELIVERED);
      default:
        throw new ForbiddenException(CommonErrors.UPDATE_NOT_ALLOWED_BY_AGENT);
    }

    ShipmentTracker newTrackerRecord =
        new ShipmentTracker()
            .setShipment(latestTrackerRecord.getShipment())
            .setCurrentBranch(latestTrackerRecord.getCurrentBranch())
            .setAgent(agent)
            .setCreationDateTime(LocalDateTime.now())
            .setStatus(Status.getByValue(deliverShipmentRequest.getStatus()))
            .setStatusRemarks(deliverShipmentRequest.getStatusRemarks());

    shipmentDao.updateShipmentStatus(newTrackerRecord);

    return new DeliverShipmentResponse()
        .setConsignmentNumber(deliverShipmentRequest.getConsignmentNumber())
        .setStatus(deliverShipmentRequest.getStatus())
        .setDeliveryDateTime(
            newTrackerRecord.getCreationDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }
}
