package site.easy.to.build.crm.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.budget.dto.StatistiqueBudgetDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/crm/budgets")
public class BudgetRestController {
    private final BudgetService budgetService;

    @Autowired
    public BudgetRestController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<StatistiqueBudgetDto> statisqueBudget(
            @RequestParam(value = "customerId",required = false) Integer customerId,
            @RequestParam(value = "startDate",required = false) LocalDateTime startDate,
            @RequestParam(value = "endDate",required = false) LocalDateTime endDate)
    {
        StatistiqueBudgetDto stat = budgetService.getTotalBudgetAndExpensesByCustomerOrNull(customerId, startDate, endDate);
        return ResponseEntity.ok(stat);
    }

    @GetMapping("/update-alert-rate")
    public ResponseEntity<?> statisqueBudget(@RequestParam(value = "rate", required = false) double rate)
    {
        try {
            budgetService.updateAlertRate(rate);
            return ResponseEntity.ok(new ApiResponse<>(200, "Update successfuly", true));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<String>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }
}
