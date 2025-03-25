package site.easy.to.build.crm.importcsv.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import site.easy.to.build.crm.importcsv.validator.EmailValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Format d'email invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
