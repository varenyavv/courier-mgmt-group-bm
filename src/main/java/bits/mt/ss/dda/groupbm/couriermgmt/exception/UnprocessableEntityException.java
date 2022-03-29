package bits.mt.ss.dda.groupbm.couriermgmt.exception;

public class UnprocessableEntityException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final ApiError error;

  public UnprocessableEntityException(ErrorCode errorCode) {
    super(errorCode.fullErrorMessage());
    this.error = new ApiError(errorCode);
  }

  public UnprocessableEntityException(ErrorCode errorCode, Object... detailArgs) {
    super(errorCode.fullErrorMessage(detailArgs));
    this.error = new ApiError(errorCode, detailArgs);
  }

  public UnprocessableEntityException(String code, String message) {
    super(String.format(ErrorCode.ERROR_MESSAGE_FORMAT, code, message));
    this.error = new ApiError(code, message);
  }

  public ApiError getError() {
    return error;
  }
}
