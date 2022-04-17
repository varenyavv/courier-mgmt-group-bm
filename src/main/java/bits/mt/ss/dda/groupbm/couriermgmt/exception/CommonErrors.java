package bits.mt.ss.dda.groupbm.couriermgmt.exception;

/**
 * Contains all error codes that could occur
 *
 * <p>There is the option to specify what exception caused this error code. The only reason for this
 * is the message will be populated from the exception itself. In order to provide context to
 * someone looking at this class it will be useful instead of having to find the instance of it and
 * understand what the message could look like.
 */
public enum CommonErrors implements ErrorCode {
  REQUEST_METHOD_NOT_SUPPORTED("100001", "Request Method Not Supported"),
  MISSING_REQUIRED_PARAMETER("100002", "Missing required request parameter"),
  FIELD_VALIDATION("100003", "The value : '%s' is not valid for a '%s' for field : '%s'"),
  INVALID_VALUE("100004", "Invalid value for field : '%s' type : '%s'"),
  UNABLE_TO_PARSE_JSON("100005", "Unable to parse Json due to : %s"),
  INVALID_JSON_FORMAT("100006", "Invalid Json format"),
  NUMERIC_VALUES_ONLY("100007", "Please use numeric values only."),
  GENERIC_EXCEPTION("100008", CommonErrors.EXCEPTION_MESSAGE_TO_BE_USED),
  MISSING_PATH_VARIABLE("100009", "Missing path variable"),
  ILLEGAL_ARGUMENT_EXCEPTION("100010", CommonErrors.EXCEPTION_MESSAGE_TO_BE_USED),
  UNHANDLED_EXCEPTION("100011", "System error. Please contact help desk for assistance."),
  FIELD_ERROR("100012", "%s - %s"),
  GLOBAL_ERROR("100013", "%s %s"),
  MEDIA_TYPE_NOT_SUPPORTED(
      "100014", "Content-Type header does not contain a supported Media Type."),
  MEDIA_TYPE_NOT_ACCEPTABLE("100014", "Accept header does not contain a supported Media Type"),
  SERVICE_NOT_AVAILABLE("100016", "%s"),
  ENTITY_NOT_FOUND("100017", "%s having %s: %s not found!"),
  UNAUTHORIZED("100018", "%s"),
  INVALID_RECEIVING_BRANCH(
      "100019", "Receiving branch %s is invalid. Valid receiving branch is %s."),
  UPDATE_NOT_ALLOWED_AT_BRANCH(
      "100020",
      "Branch is not allowed to further update the shipment's status as its current status is '%s'"),
  UPDATE_NOT_ALLOWED_BY_AGENT(
      "100021",
      "Agent is not allowed to update the shipment's status as it is not yet received at the destination branch."),
  SHIPMENT_ALREADY_DELIVERED(
      "100022",
      "Agent is not allowed to attempt the delivery as the shipment is already delivered."),
  INVALID_DELIVERY_ATTEMPT(
      "100023", "Status in delivery request '%s' is invalid. Allowed values are [%s]."),
  AGENT_NOT_AUTHORIZED(
      "100024",
      "Agent %s is not allowed to attempt the delivery. Agent %s is already assigned to attempt its delivery.");

  private static final String EXCEPTION_MESSAGE_TO_BE_USED = "%s";

  private static final String APP_PREFIX = "COM";

  private final String code;

  private final String detail;

  CommonErrors(String code, String detail) {
    this.code = code;
    this.detail = detail;
  }

  public String code() {
    return APP_PREFIX + code;
  }

  public String detail() {
    return detail;
  }
}
