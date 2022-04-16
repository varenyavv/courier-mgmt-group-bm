package bits.mt.ss.dda.groupbm.couriermgmt.exception;

public class DaoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final ApiError error;

  public DaoException(ErrorCode errorCode) {
    super(errorCode.fullErrorMessage());
    this.error = new ApiError(errorCode);
  }

  public DaoException(ErrorCode errorCode, Object... detailArgs) {
    super(errorCode.fullErrorMessage(detailArgs));
    this.error = new ApiError(errorCode, detailArgs);
  }

  public DaoException(String code, String message) {
    super(String.format(ErrorCode.ERROR_MESSAGE_FORMAT, code, message));
    this.error = new ApiError(code, message);
  }

  public ApiError getError() {
    return error;
  }
}
