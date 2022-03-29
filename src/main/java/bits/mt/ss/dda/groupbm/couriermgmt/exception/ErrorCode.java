package bits.mt.ss.dda.groupbm.couriermgmt.exception;

public interface ErrorCode {

  String ERROR_MESSAGE_FORMAT = "%s - %s";

  String code();

  String detail();

  default String format(Object... args) {
    return String.format(detail(), args);
  }

  default String fullErrorMessage() {
    return String.format(ERROR_MESSAGE_FORMAT, code(), detail());
  }

  default String fullErrorMessage(Object... args) {
    return String.format(ERROR_MESSAGE_FORMAT, code(), format(args));
  }
}
