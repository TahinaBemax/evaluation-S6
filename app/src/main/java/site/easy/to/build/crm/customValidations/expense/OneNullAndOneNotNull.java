package site.easy.to.build.crm.customValidations.expense;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OneNullAndOneNotNullValidator.class) // Spécifie le validateur
public @interface OneNullAndOneNotNull {

    String message() default "L'un des attributs (ticket ou lead) doit être non nul et l'autre nul.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

