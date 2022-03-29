package bits.mt.ss.dda.groupbm.couriermgmt.model.base;

import java.util.List;

import bits.mt.ss.dda.groupbm.couriermgmt.exception.ApiError;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseErrorResponse {
  private List<ApiError> errors;
  private Links links;

  public List<ApiError> getErrors() {
    return errors;
  }

  public void setErrors(List<ApiError> errors) {
    this.errors = errors;
  }

  public Links getLinks() {
    return links;
  }

  public void setLinks(Links links) {
    this.links = links;
  }
}
