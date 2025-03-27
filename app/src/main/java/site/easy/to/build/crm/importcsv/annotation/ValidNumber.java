package site.easy.to.build.crm.importcsv.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import site.easy.to.build.crm.importcsv.validator.NumericValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Définir l'annotation
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumericValidator.class) // Lien avec le validateur
public @interface ValidNumber {

    // Message d'erreur
    String message() default "Invalid number format";

    // Groupes de validation (facultatif)
    Class<?>[] groups() default {};

    // Payloads (facultatif)
    Class<? extends Payload>[] payload() default {};
}
