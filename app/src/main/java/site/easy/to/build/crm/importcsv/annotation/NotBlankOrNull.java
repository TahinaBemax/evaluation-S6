package site.easy.to.build.crm.importcsv.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import site.easy.to.build.crm.importcsv.validator.NotBlankOrNullValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotBlankOrNullValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankOrNull {
    String message() default "La valeur ne doit pas être vide ou nulle";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
