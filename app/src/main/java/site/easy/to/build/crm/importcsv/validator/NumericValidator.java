package site.easy.to.build.crm.importcsv.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import site.easy.to.build.crm.importcsv.annotation.ValidNumber;

import java.math.BigDecimal;

public class NumericValidator implements ConstraintValidator<ValidNumber, BigDecimal> {

    @Override
    public void initialize(ValidNumber constraintAnnotation) {
        // Initialisation (vide pour cet exemple)
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        // Vérifier si la valeur est nulle ou inférieure à zéro
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        // Vérifier si la valeur correspond à un nombre valide avec deux décimales
        return value.scale() <= 2; // Vérifie que le nombre a au maximum deux décimales
    }
}
