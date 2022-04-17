package bits.mt.ss.dda.groupbm.couriermgmt.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = StringValidator.class)
public @interface ValidateString {

  String[] acceptedValues();

  String message() default "{bits.mt.ss.dda.groupbm.couriermgmt.validator.ValidateString.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
