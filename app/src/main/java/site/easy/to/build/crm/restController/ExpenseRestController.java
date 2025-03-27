package site.easy.to.build.crm.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.exception.ResourceNotFoundException;
import site.easy.to.build.crm.restController.formDTO.UpdateExpenseAmountDto;
import site.easy.to.build.crm.service.expense.ExpenseService;
import site.easy.to.build.crm.service.lead.dto.DetailStatisticTicketLead;

@RestController
@RequestMapping("/api/crm/expenses")
public class ExpenseRestController {
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseRestController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<?>> update(@RequestBody UpdateExpenseAmountDto data)
    {
        Expense pastExpense = expenseService.findById(data.getExpenseId());
        if(pastExpense == null){
            return ResponseEntity.ok(new ApiResponse<>(500, "Not found", new ResourceNotFoundException()));
        }
        pastExpense.setAmount(data.getAmount());
        pastExpense = expenseService.save(pastExpense);
        Integer id = (pastExpense.getLead() != null) ? pastExpense.getId() : pastExpense.getTicket().getTicketId();
        String name = (pastExpense.getLead() != null) ? pastExpense.getLead().getName() : pastExpense.getTicket().getSubject();
        String email = (pastExpense.getLead() != null) ? pastExpense.getLead().getCustomer().getEmail() : pastExpense.getTicket().getCustomer().getEmail();
        DetailStatisticTicketLead newAmount = new DetailStatisticTicketLead(
                id,
                name,
                email,
                pastExpense.getAmount(),
                pastExpense.getId()
        );
        return ResponseEntity.ok(new ApiResponse<>(200, "Expense updated successfully", newAmount));
    }
}
