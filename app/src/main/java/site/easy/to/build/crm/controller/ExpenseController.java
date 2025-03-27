package site.easy.to.build.crm.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.exception.BudgetNotFoundException;
import site.easy.to.build.crm.exception.InsufficientBudgetException;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.budget.CategoryBudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.expense.ExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Slf4j
@Controller
@RequestMapping("/manager/expense")
public class ExpenseController {
    private final TicketService ticketService;
    private final LeadService leadService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;
    private final CustomerService customerService;
    private final CategoryBudgetService categoryBudgetService;

    @Autowired
    public ExpenseController(TicketService ticketService, LeadService leadService, BudgetService budgetService, ExpenseService expenseService, CustomerService customerService, CategoryBudgetService categoryBudgetService) {
        this.ticketService = ticketService;
        this.leadService = leadService;
        this.budgetService = budgetService;
        this.expenseService = expenseService;
        this.customerService = customerService;
        this.categoryBudgetService = categoryBudgetService;
    }



    @GetMapping()
    public String allExpensePage(Model model) {
        List<Expense> expenses = expenseService.findAll();
        model.addAttribute("expenses", expenses);
        return "/expense/list";
    }

    @GetMapping("/ticket/{id}")
    public String listeExpenseByTicketPage(@PathVariable("id")Integer id, Model model) {
        if (id == null)
            return "error/500";

        List<Expense> expenses = expenseService.findByTicketId(id);
        model.addAttribute("expenses", expenses);
        return "/expense/list";
    }

    @GetMapping("/lead/{id}")
    public String listeExpenseByLeadPage(@PathVariable("id")Integer id, Model model) {
        if (id == null)
            return "error/500";

        List<Expense> expenses = expenseService.findByLeadId(id);
        model.addAttribute("expenses", expenses);
        return "/expense/list";
    }

    @GetMapping("/{id}")
    public String showExpensePage(Model model, @PathVariable("id") Integer id) {
        if (id == null){
            return "error/500";
        }

        Expense e = expenseService.findById(id);
        if (e == null)
            return "error/500";

        model.addAttribute("expense", e);
        return "/expense/show";
    }

    @GetMapping("/{id}/form")
    public String updateFormPage(@PathVariable("id") Integer id, Model model) throws BudgetNotFoundException {
        if (id == null){
            log.error("Expense id is null");
            return "error/500";
        }

        Expense b = expenseService.findById(id);
        if (b == null){
            log.error(String.format("Expense id: %s not found", String.valueOf(id)));
            return "error/500";
        }

        Budget budget = budgetService.findById(b.getBudget().getId());
        if (budget == null){
            throw new BudgetNotFoundException(String.format("Bugdet id: %s not found", b.getBudget().getId()));
        }

        model.addAttribute("budgets", budgetService.findAllByCustomer(budget.getCustomer().getCustomerId(), LocalDateTime.now()));
        model.addAttribute("expense", b);

        return "/expense/update-form";
    }

    @GetMapping("/ticket/{ticketId}/form")
    public String ticketformPage(
            @PathVariable("ticketId") Integer ticketId,
            @RequestParam(value = "expenseId",required = false) Integer expenseId, Model model)
    {
        Expense b = new Expense();
        if (expenseId == null){
            if(ticketId == null)
                return "error/500";

            Ticket t = ticketService.findByTicketId(ticketId);
            if (t == null)
                return "error/500";

            b.setTicket(t);
        } else {
            b = expenseService.findById(expenseId);
        }

        model.addAttribute("budgets", budgetService.findAllByCustomer(b.getTicket().getCustomer().getCustomerId(), LocalDateTime.now()));
        model.addAttribute("expense", b);

        return "/expense/form";
    }

    @GetMapping("/lead/{leadId}/form")
    public String expenseformPage(
            @PathVariable("leadId") Integer leadId,
            @RequestParam(value = "expenseId",required = false) Integer expenseId, Model model)
    {
        Expense e = new Expense();
        if (expenseId == null){
            if (leadId == null) {
                return "error/500";
            }

            Lead l = leadService.findByLeadId(leadId);
            if (l == null) {
                return "error/500";
            }
            e.setLead(l);
        } else {
            e = expenseService.findById(expenseId);
        }

        model.addAttribute("budgets", budgetService.findAllByCustomer(e.getLead().getCustomer().getCustomerId(), LocalDateTime.now()));
        model.addAttribute("expense", e);

        return "/expense/form";
    }

    @PostMapping("/save")
    public String saveExpense(@Valid @ModelAttribute("expense") Expense expense,
                              @RequestParam(name = "confirmation", required = false) Integer confirmation,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, Model model)
    {
        try {
            if (bindingResult.hasErrors()){
                model.addAttribute("budgets", budgetService.findAllByCustomer(expense.getBudget().getCustomer().getCustomerId(), LocalDateTime.now()));
                return "expense/form";
            }

            Budget budget = budgetService.findById(expense.getBudget().getId());
            if (budget == null){
                throw new BudgetNotFoundException(String.format("Bugdet id: %s not found", expense.getBudget().getId()));
            }

            BigDecimal totalExpense = budget.getConsomation().add(expense.getAmount());
            if (confirmation == null && totalExpense.compareTo(budget.getAmount()) > 0){
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
                String message = String.format("Budget insufficent! Budget amount: %s but expense amount: %s", format.format(budget.getAmount()), format.format(totalExpense));

                model.addAttribute("expense", expense);
                model.addAttribute("alertConfirmation", message);
                model.addAttribute("needConfirmation", false);
                model.addAttribute("budgets", budgetService.findAllByCustomer(budget.getCustomer().getCustomerId(), LocalDateTime.now()));
                return "expense/form";
            }

            if ((confirmation !=null && confirmation == 1) || totalExpense.compareTo(budget.getAmount()) <=0){
                expense = expenseService.save(expense);
                redirectAttributes.addFlashAttribute("successMessage", "Expense attributed successfuly");
            }
        } catch (Exception e){
            log.error(e.getMessage());
            return "error/500";
        }

        return "redirect:/manager/expense/" + expense.getId();
    }

    @PostMapping("/save-update")
    public String saveUpdateExpense(@Valid @ModelAttribute("expense") Expense expense,
                              @RequestParam(name = "confirmation", required = false) Integer confirmation,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, Model model)
    {
        try {
            if (bindingResult.hasErrors()){
                model.addAttribute("budgets",
                        budgetService.findAllByCustomer(expense.getBudget().getCustomer().getCustomerId(), LocalDateTime.now()));
                return "expense/form";
            }

            Budget budget = budgetService.findById(expense.getBudget().getId());
            if (budget == null){
                throw new BudgetNotFoundException(String.format("Bugdet id: %s not found", expense.getBudget().getId()));
            }

            final Integer newExpenseId = expense.getId();
/*            List<Expense> expenses = budget.getExpenses()
                    .stream().filter(exp -> !exp.getId().equals(newExpenseId)).toList();*/
            List<Expense> expenses = new ArrayList<>(budget.getExpenses());  // Rend la liste modifiable
            expenses.removeIf(exp -> exp.getId().equals(newExpenseId));

            budget.setExpenses(expenses);
            BigDecimal totalExpense = budget.getConsomation();

            if (confirmation == null && totalExpense.compareTo(budget.getAmount()) > 0){
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
                String message = String.format("Budget insufficent! Budget amount: %s but expense amount: %s", format.format(budget.getAmount()), format.format(totalExpense));

                model.addAttribute("expense", expense);
                model.addAttribute("alertConfirmation", message);
                model.addAttribute("needConfirmation", false);
                model.addAttribute("budgets", budgetService.findAllByCustomer(budget.getCustomer().getCustomerId(), LocalDateTime.now()));
                return "expense/form";
            }

            if ((confirmation !=null && confirmation == 1) || totalExpense.compareTo(budget.getAmount()) <=0){
                expense = expenseService.save(expense);
                redirectAttributes.addFlashAttribute("successMessage", "Expense attributed successfuly");
            }
        } catch (Exception e){
            log.error(e.getMessage());
            return "error/500";
        }

        return "redirect:/manager/expense/" + expense.getId();
    }

    @GetMapping("/confirmation")
    public String confirmationPage(@Validated @ModelAttribute("expense")Expense expense, BindingResult bindingResult)
    {
        if (expense == null || bindingResult.hasErrors())
            return "error/500";

        return "expense/confirmation";
    }

    @PostMapping("/confirmation")
    public String formPage(@Validated @ModelAttribute("expense")Expense expense, BindingResult bindingResult
            ,RedirectAttributes redirectAttributes)
    {
        if (expense == null || bindingResult.hasErrors())
            return "error/500";

        expense = expenseService.save(expense);
        redirectAttributes.addFlashAttribute("successMessage", "Expense attributed successfuly");
        return "redirect:/manager/expense/" + expense.getId();
    }
}
