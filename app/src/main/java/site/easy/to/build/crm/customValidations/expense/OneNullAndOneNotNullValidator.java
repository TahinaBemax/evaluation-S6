package site.easy.to.build.crm.customValidations.expense;

import site.easy.to.build.crm.entity.Expense;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OneNullAndOneNotNullValidator implements ConstraintValidator<OneNullAndOneNotNull, Expense> {

    @Override
    public void initialize(OneNullAndOneNotNull constraintAnnotation) {
        // Initialisation (aucune initialisation spécifique ici)
    }

    @Override
    public boolean isValid(Expense entity, ConstraintValidatorContext context) {
        // Si l'un des deux (ticket ou lead) est non nul et l'autre est nul, la validation est valide
        return (entity.getTicket() != null && entity.getLead() == null) || (entity.getTicket() == null && entity.getLead() != null);
    }
}
