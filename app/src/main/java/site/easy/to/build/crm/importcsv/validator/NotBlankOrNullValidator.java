package site.easy.to.build.crm.importcsv.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import site.easy.to.build.crm.importcsv.annotation.NotBlankOrNull;

public class NotBlankOrNullValidator implements ConstraintValidator<NotBlankOrNull, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !value.trim().isEmpty();
    }
}
