package bits.mt.ss.dda.groupbm.couriermgmt.model.base;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
  private T data;
  private Links links;

  public BaseResponse() {}

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public Links getLinks() {
    return links;
  }

  public void setLinks(Links links) {
    this.links = links;
  }
}
