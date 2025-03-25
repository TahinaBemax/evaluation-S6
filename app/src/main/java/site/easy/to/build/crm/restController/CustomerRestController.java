package site.easy.to.build.crm.restController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.expense.ExpenseService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/crm/customers")
public class CustomerRestController {
    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<Customer> all = customerService.findAll();
        return ResponseEntity.ok(all);
    }

/*    @GetMapping("{customerId}/expenses")
    public ResponseEntity<?> customerExpense(
            @PathVariable(value = "customerId") Integer customerId,
            @RequestParam(value = "startDate",required = false) LocalDateTime start,
            @RequestParam(value = "endDate",required = false) LocalDateTime end)
    {
        //List<Expense> all = expenseService.findCustomerExpenseBetweenDate(customerId, start, end);
        return ResponseEntity.ok();
    }*/
}
