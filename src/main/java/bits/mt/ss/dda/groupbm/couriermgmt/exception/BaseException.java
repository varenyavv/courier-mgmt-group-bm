package bits.mt.ss.dda.groupbm.couriermgmt.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
  private static final long serialVersionUID = -5074387176247437858L;

  private final HttpStatus status;
  private final List<ApiError> errors = new ArrayList<>();

  public BaseException(ErrorCode errorCode) {
    super(errorCode.fullErrorMessage());
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    this.errors.add(new ApiError(errorCode));
  }

  public BaseException(ErrorCode errorCode, Object... detailArgs) {
    super(errorCode.fullErrorMessage(detailArgs));
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    this.errors.add(new ApiError(errorCode, detailArgs));
  }

  public BaseException(String code, String message) {
    super(String.format(ErrorCode.ERROR_MESSAGE_FORMAT, code, message));
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    this.errors.add(new ApiError(code, message));
  }

  public BaseException(HttpStatus status, List<ApiError> errorCodes) {
    super(errorCodes.stream().map(e -> e.getDetail()).collect(Collectors.joining(",")));
    this.status = status;
    this.errors.addAll(errorCodes);
  }

  public BaseException(HttpStatus status, ErrorCode errorCode) {
    super(errorCode.fullErrorMessage());
    this.status = status;
    this.errors.add(new ApiError(errorCode));
  }

  public BaseException(HttpStatus status, ErrorCode errorCode, Object... detailArgs) {
    super(errorCode.fullErrorMessage(detailArgs));
    this.status = status;
    this.errors.add(new ApiError(errorCode, detailArgs));
  }

  public BaseException(HttpStatus status, String code, String message) {
    super(String.format(ErrorCode.ERROR_MESSAGE_FORMAT, code, message));
    this.status = status;
    this.errors.add(new ApiError(code, message));
  }

  public List<ApiError> getErrors() {
    return errors;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
