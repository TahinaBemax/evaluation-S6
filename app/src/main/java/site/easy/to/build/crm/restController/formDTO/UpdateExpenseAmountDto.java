package site.easy.to.build.crm.restController.formDTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateExpenseAmountDto {
    Integer expenseId;

    private BigDecimal amount;
}
