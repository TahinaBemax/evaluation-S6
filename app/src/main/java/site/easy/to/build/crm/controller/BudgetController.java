package site.easy.to.build.crm.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.CategoryBudget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.exception.BudgetNotFoundException;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.budget.BudgetServiceImp;
import site.easy.to.build.crm.service.budget.CategoryBudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.expense.ExpenseService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/manager/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final CustomerService customerService;
    private final CategoryBudgetService categoryBudgetService;
    private final ExpenseService expenseService;

    @Autowired
    public BudgetController(BudgetService budgetService, CustomerService customerService, CategoryBudgetService categoryBudgetService, ExpenseService expenseService) {
        this.budgetService = budgetService;
        this.customerService = customerService;
        this.categoryBudgetService = categoryBudgetService;
        this.expenseService = expenseService;
    }



    @GetMapping
    public String listeBudgetPage(Model model) {
        List<Budget> budgetList = budgetService.findAll();
        model.addAttribute("budgets", budgetList);
        return "/budget/list";
    }

    @GetMapping("/{id}")
    public String showBudgetPage(Model model,@PathVariable("id") Integer id) {
        if (id == null){
            return "error/500";
        }

        Budget budget = budgetService.findById(id);
        if (budget == null) {
            log.error("Budget is null");
            return "error/500";
        }

        List<Expense> expenses = new ArrayList<>();
        expenses = expenseService.findAllByBudgetId(budget.getId());
        model.addAttribute("budget", budget);
        model.addAttribute("expenses", expenses);
        return "/budget/show";
    }



    @GetMapping("/form")
    public String formPage(@RequestParam(value = "id",required = false) Integer id, Model model) {
        Budget b = (id != null) ? budgetService.findById(id) : new Budget();

        List<Customer> customers = this.getAllCustomer();
        List<CategoryBudget> categoryBudgets = categoryBudgetService.findAll();

        model.addAttribute("customers", customers);
        model.addAttribute("categories", categoryBudgets);
        model.addAttribute("budget", b);
        return "/budget/form";
    }
    @PostMapping
    public String saveBudget(@Valid @ModelAttribute("budget") Budget budget, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        try {
            if (bindingResult.hasErrors()){
                List<Customer> customers = this.getAllCustomer();
                List<CategoryBudget> categoryBudgets = categoryBudgetService.findAll();

                model.addAttribute("customers", customers);
                model.addAttribute("categories", categoryBudgets);
                return "/budget/form";
            }

            budget.setCreatedAt(LocalDateTime.now());
            budgetService.save(budget);
            redirectAttributes.addFlashAttribute("successMessage", "Budget created successfuly");
        } catch (Exception e){
            return "error/500";
        }
        return "redirect:/manager/budgets";
    }

    @PostMapping("/delete/{id}")
    public String deleteBudget(@PathVariable("id") Integer id) throws BudgetNotFoundException {
        if (id == null)
            return "error/500";

        Budget budget = budgetService.findById(id);

        if (budget == null)
            throw new BudgetNotFoundException(id);

        budgetService.delete(budget);
        return "redirect:/manager/budgets";
    }

    private List<Customer> getAllCustomer(){
        return customerService.findAll();
    }
}
