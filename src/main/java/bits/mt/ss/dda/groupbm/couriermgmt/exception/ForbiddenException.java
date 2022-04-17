package bits.mt.ss.dda.groupbm.couriermgmt.exception;

public class ForbiddenException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final ApiError error;

  public ForbiddenException(ErrorCode errorCode) {
    super(errorCode.fullErrorMessage());
    this.error = new ApiError(errorCode);
  }

  public ForbiddenException(ErrorCode errorCode, Object... detailArgs) {
    super(errorCode.fullErrorMessage(detailArgs));
    this.error = new ApiError(errorCode, detailArgs);
  }

  public ForbiddenException(String code, String message) {
    super(String.format(ErrorCode.ERROR_MESSAGE_FORMAT, code, message));
    this.error = new ApiError(code, message);
  }

  public ApiError getError() {
    return error;
  }
}
