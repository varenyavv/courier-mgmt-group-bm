package bits.mt.ss.dda.groupbm.couriermgmt.constants;

public final class ApplicationConstants {

  private ApplicationConstants() {}

  public static final String HTTP_200_OK = "OK";
  public static final String HTTP_400_BAD_REQUEST = "Bad Request";
  public static final String HTTP_401_UNAUTHORIZED = "Unauthorized";
  public static final String HTTP_403_FORBIDDEN = "Forbidden";
  public static final String HTTP_204_NO_CONTENT = "No Content";
  public static final String HTTP_409_CONFLICT = "Conflict";
  public static final String HTTP_422_UNPROCESSABLE_ENTITY = "Unprocessable Entity";

  public static final class Route {

    private Route() {}

    public static final String RESOURCE_ROOT_URI = "/routes";
  }

  public static final class Quote {

    private Quote() {}

    public static final String RESOURCE_ROOT_URI = "/quotes";
  }
}
