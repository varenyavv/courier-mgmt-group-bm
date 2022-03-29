package bits.mt.ss.dda.groupbm.couriermgmt.exception;

public class ServiceUnavailableException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final ApiError error;

  public ServiceUnavailableException(ErrorCode errorCode) {
    super(errorCode.fullErrorMessage());
    this.error = new ApiError(errorCode);
  }

  public ServiceUnavailableException(ErrorCode errorCode, Object... detailArgs) {
    super(errorCode.fullErrorMessage(detailArgs));
    this.error = new ApiError(errorCode, detailArgs);
  }

  public ServiceUnavailableException(String code, String message) {
    super(String.format(ErrorCode.ERROR_MESSAGE_FORMAT, code, message));
    this.error = new ApiError(code, message);
  }

  public ApiError getError() {
    return error;
  }
}
