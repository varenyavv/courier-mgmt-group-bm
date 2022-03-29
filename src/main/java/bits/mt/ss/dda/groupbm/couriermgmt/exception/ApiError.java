package bits.mt.ss.dda.groupbm.couriermgmt.exception;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiError implements Serializable {

  private static final long serialVersionUID = -7704804104790159771L;

  private String code;
  private String detail;

  public ApiError() {}

  public ApiError(String code, String detail) {
    this.code = code;
    this.detail = detail;
  }

  public ApiError(ErrorCode errorCode) {
    this.code = errorCode.code();
    this.detail = errorCode.detail();
  }

  public ApiError(ErrorCode errorCode, Object... detailArgs) {
    this.code = errorCode.code();
    this.detail = errorCode.format(detailArgs);
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }
}
