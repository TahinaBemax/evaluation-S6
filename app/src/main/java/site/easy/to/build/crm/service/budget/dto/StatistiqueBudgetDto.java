package site.easy.to.build.crm.service.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatistiqueBudgetDto {
    Integer budgetId;
    BigDecimal totalBudget;
    BigDecimal totalExpense;
    BigDecimal totalRest;
    String customerName;
    Integer customerId;
}
