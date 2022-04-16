package bits.mt.ss.dda.groupbm.couriermgmt.exception;

public class UnauthorizedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final ApiError error;

  public UnauthorizedException(ErrorCode errorCode) {
    super(errorCode.fullErrorMessage());
    this.error = new ApiError(errorCode);
  }

  public UnauthorizedException(ErrorCode errorCode, Object... detailArgs) {
    super(errorCode.fullErrorMessage(detailArgs));
    this.error = new ApiError(errorCode, detailArgs);
  }

  public UnauthorizedException(String code, String message) {
    super(String.format(ErrorCode.ERROR_MESSAGE_FORMAT, code, message));
    this.error = new ApiError(code, message);
  }

  public ApiError getError() {
    return error;
  }
}
