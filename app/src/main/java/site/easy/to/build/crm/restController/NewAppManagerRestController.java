package site.easy.to.build.crm.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.exception.ResourceNotFoundException;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.lead.dto.DetailStatisticTicketLead;
import site.easy.to.build.crm.service.lead.dto.StatisticTicketLead;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/crm/customers")
public class NewAppManagerRestController {
    private final CustomerService customerService;
    private final LeadService leadService;
    private final TicketService ticketService;
    private final BudgetService budgetService;

    @Autowired
    public NewAppManagerRestController(CustomerService customerService, LeadService leadService, TicketService ticketService, BudgetService budgetService) {
        this.customerService = customerService;
        this.leadService = leadService;
        this.ticketService = ticketService;
        this.budgetService = budgetService;
    }
    @GetMapping
    public ResponseEntity<?> getAll(){
        List<Customer> all = customerService.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/leads/monthly-statistic")
    public ResponseEntity<ApiResponse<List<StatisticTicketLead>>> monthlyStatisticLead(
            @RequestParam(value = "year", required = false) Integer year){
        List<StatisticTicketLead> monthlyStatisticTicketLead = leadService.getMonthlyStatistiqueLead(year);
        ApiResponse<List<StatisticTicketLead>> apiResponse = new ApiResponse<>(200, "Data retrieved successfully", monthlyStatisticTicketLead);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/leads/monthly-statistic-details")
    public ResponseEntity<ApiResponse<List<DetailStatisticTicketLead>>> monthlyStatisticLeadDetails(
            @RequestParam(value = "year", required = false) Integer year){
        List<DetailStatisticTicketLead> monthlyStatistiqueLead = leadService.getDetailMonthlyStatistiqueLead(year);
        ApiResponse<List<DetailStatisticTicketLead>> apiResponse = new ApiResponse<>(200, "Data retrieved successfully", monthlyStatistiqueLead);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/leads/monthly-statistic-details/expenses/{id}")
    public ResponseEntity<ApiResponse<DetailStatisticTicketLead>> getByExpenseMonthlyStatisticLeadDetails(
            @PathVariable("id") Integer id) throws ResourceNotFoundException {
        DetailStatisticTicketLead monthlyStatistiqueLead = leadService.getByExpenseIdDetailMonthlyStatLead(id);

        if (monthlyStatistiqueLead == null ){
            throw new ResourceNotFoundException();
        }

        ApiResponse<DetailStatisticTicketLead> apiResponse = new ApiResponse<>(200, "Data retrieved successfully", monthlyStatistiqueLead);
        return ResponseEntity.ok(apiResponse);
    }

   @DeleteMapping("/leads/{leadId}")
    public ResponseEntity<?> deleteLead(@PathVariable(value = "leadId") Integer leadId)
    {
        Lead temp = leadService.findByLeadId(leadId);
        leadService.delete(temp);
        return ResponseEntity.ok(new ApiResponse<>(200, "Delete successfly", true));
    }


    @GetMapping("/tickets/monthly-statistic")
    public ResponseEntity<ApiResponse<List<StatisticTicketLead>>> monthlyStatisticTicket(
            @RequestParam(value = "year", required = false) Integer year){
        List<StatisticTicketLead> monthlyStatisticTicketLead = ticketService.getMonthlyStatisticTicket(year);
        ApiResponse<List<StatisticTicketLead>> apiResponse = new ApiResponse<>(200, "Data retrieved successfully", monthlyStatisticTicketLead);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/tickets/monthly-statistic-details")
    public ResponseEntity<ApiResponse<List<DetailStatisticTicketLead>>> monthlyStatisticTicketDetails(
            @RequestParam(value = "year", required = false) Integer year){
        List<DetailStatisticTicketLead> monthlyStatistiqueLead = ticketService.getDetailMonthlyStatisticTicket(year);
        ApiResponse<List<DetailStatisticTicketLead>> apiResponse = new ApiResponse<>(200, "Data retrieved successfully", monthlyStatistiqueLead);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/tickets/monthly-statistic-details/expenses/{id}")
    public ResponseEntity<ApiResponse<DetailStatisticTicketLead>> getByExpenseMonthlyStatisticTicketDetails(
            @PathVariable("id") Integer id) throws ResourceNotFoundException {
        DetailStatisticTicketLead monthlyStatistiqueLead = ticketService.getByExpenseIdDetailMonthlyStatTicket(id);

        if (monthlyStatistiqueLead == null ){
            throw new ResourceNotFoundException();
        }

        ApiResponse<DetailStatisticTicketLead> apiResponse = new ApiResponse<>(200, "Data retrieved successfully", monthlyStatistiqueLead);
        return ResponseEntity.ok(apiResponse);
    }
}
