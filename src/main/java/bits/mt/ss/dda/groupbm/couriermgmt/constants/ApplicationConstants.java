package bits.mt.ss.dda.groupbm.couriermgmt.constants;

public final class ApplicationConstants {

  private ApplicationConstants() {}

  public static final String HTTP_200_OK = "OK";
  public static final String HTTP_400_BAD_REQUEST = "Bad Request";
  public static final String HTTP_401_UNAUTHORIZED = "Unauthorized";
  public static final String HTTP_403_FORBIDDEN = "Forbidden";
  public static final String HTTP_404_NOT_FOUND = "Resource not found";
  public static final String HTTP_204_NO_CONTENT = "No Content";
  public static final String HTTP_409_CONFLICT = "Conflict";
  public static final String HTTP_422_UNPROCESSABLE_ENTITY = "Unprocessable Entity";

  public static final class Documentation {

    private Documentation() {}

    public static final String TAG_QUOTE = "1. Get Quote";
    public static final String TAG_ROUTE = "2. Get Route";

    public static final String GET_QUOTE_SUMMARY =
        "API to get a quote for a given shipment between source and destination pincodes";
    public static final String GET_QUOTE_DESC =
        "Customers can use this API to determine whether their shipment is deliverable. "
            + "If yes, then this API can also help them to get a quotation based on their shipment's dimensions. "
            + "Depending upon the dimensions and weight of the shipment "
            + "as well as the distance between source and destination address, "
            + "this API returns the total cost of the shipment. "
            + "It also helps in checking the serviceability "
            + "between a given source and destination address based on their respective pincodes.";
    public static final String GET_ROUTE_SUMMARY =
        "API to find a route between source and destination pincodes";
    public static final String GET_ROUTE_DESC =
        "Agents at the time of booking a shipment make use of this API "
            + "to verify the route and booking amount of a given shipment.";

    public static final String TAG_SHIPMENT_LIFECYCLE =
        "3. APIs related to Shipment lifecycle | APIs to book a shipment -> forward a shipment -> deliver a shipment";
    public static final String TAG_SHIPMENT_HISTORY = "4. Shipment history";

    public static final String BOOK_SHIPMENT_SUMMARY = "API to book a shipment";
    public static final String BOOK_SHIPMENT_DESC =
        "Agents use this API to book a shipment after receiving the booking amount from the customer. "
            + "Once booked, it returns a shipment Id and a consignment number "
            + "using which customers can track this shipment.";
    public static final String FORWARD_SHIPMENT_SUMMARY =
        "API to forward a shipment either to another branch or to the delivery agent of the destination branch";
    public static final String FORWARD_SHIPMET_DESC =
        "Once a shipment is received at a branch, employees use this API to either forward it to another branch or "
            + "to move to the bucket of delivery agents if the receiving branch is the destination branch of the shipment. "
            + "Route table is used to determine the next hop of the shipment.";
    public static final String DELIVER_SHIPMENT_SUMMARY = "API to deliver a shipment";
    public static final String DELIVER_SHIPMENT_DESC =
        "Once a shipment is received at its destination branch, "
            + "agents use this API to attempt to deliver it at the destination address.";

    public static final String SHIPMENT_HISTORY_API_SUMMARY = "API to track shipment";
    public static final String SHIPMENT_HISTORY_API_DESC =
        "Either customers or employees can use this API to tack the current status of a given shipment. "
            + "It also tells the complete history of a given shipment.";

    public static final String TAG_BRANCH = "5. APIs to support Operations | Branch";
    public static final String TAG_PINCODES = "6. APIs to support Operations | Serviceable Area";
    public static final String ADD_BRANCH_SUMMARY = "API to add a new branch";
    public static final String ADD_BRANCH_DESC =
        "This API adds a new branch. Its pincode is automatically added to the service_pincode table.";
    public static final String UPDATE_BRANCH_SUMMARY = "API to update a branch";
    public static final String SERVICE_PINCODE_SUMMARY = "API to add serviceable pincodes";
    public static final String SERVICE_PINCODE_DESC =
        "This API adds list of pincodes to the service_pincode table and associate them with the provided branch. "
            + "This application can serve only to those areas whose pincodes are present in this service_pincode table.";

    public static final String TAG_EMPLOYEE = "7. APIs to support Operations | Employee";
    public static final String TAG_AGENT = "8. APIs to support Operations | Agent";
    public static final String TAG_CUSTOMER = "9. APIs to support Operations | Customer";
    public static final String GET_CUSTOMER_SUMMARY =
        "API to get details of a customer by contact number";
    public static final String GET_AGENT_SUMMARY =
        "API to get details of an agent by contact number";
    public static final String ADD_AGENT_SUMMARY = "API to add a new agent";
    public static final String UPDATE_AGENT_SUMMARY = "API to update agent details";
    public static final String GET_EMPLOYEE_SUMMARY =
        "API to get details of an employee by employee Id";
    public static final String ADD_EMPLOYEE_SUMMARY = "API to add a new employee";
    public static final String UPDATE_EMPLOYEE_SUMMARY = "API to update employee details";
  }

  public static final class Route {

    private Route() {}

    public static final String RESOURCE_ROOT_URI = "/routes";
  }

  public static final class Quote {

    private Quote() {}

    public static final String RESOURCE_ROOT_URI = "/quotes";
  }

  public static final class Shipment {

    private Shipment() {}

    public static final String SHIPMENT_RESOURCE_BASE_URI = "/shipments";
    public static final String FORWARD_SHIPMENT_URI = "/{consignment_num}/forward";
    public static final String DELIVER_SHIPMENT_URI = "/{consignment_num}/deliver";
    public static final String TRACK_SHIPMENT_URI = "/{consignment_num}";
  }

  public static final class Person {

    private Person() {}

    public static final String CUSTOMER_RESOURCE_URI = "/customers";
    public static final String EMPLOYEE_RESOURCE_URI = "/employees";
    public static final String AGENT_RESOURCE_URI = "/agents";
  }

  public static final class Branch {

    private Branch() {}

    public static final String BRANCH_RESOURCE_BASE_URI = "/branches";
  }
}
