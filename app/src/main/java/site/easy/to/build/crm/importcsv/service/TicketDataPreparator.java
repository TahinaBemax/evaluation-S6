/*
package site.easy.to.build.crm.importcsv.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.importcsv.exception.ImportCsvException;
import site.easy.to.build.crm.importcsv.model.CustomerCsv;
import site.easy.to.build.crm.importcsv.model.TicketCsv;
import site.easy.to.build.crm.importcsv.model.TicketLeadCsv;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.expense.ExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.EmailTokenUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TicketDataPreparator {
    private final TicketService ticketService;
    private final UserService userService;
    private final CustomerService customerService;
    private final LeadService leadService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    @Autowired
    public TicketDataPreparator(UserService userService, BudgetService budgetService,
                                TicketService ticketService, CustomerService customerService, LeadService leadService, ExpenseService expense) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.customerService = customerService;
        this.leadService = leadService;
        this.expenseService = expense;
        this.budgetService = budgetService;
    }

    private List<Map<String, Object>> prepareTicketInstance(ImportCsvService importCsvService, MultipartFile file, String separator) throws IOException, ImportCsvException {
        //parse le fichier csv et extraire les données
        List<TicketLeadCsv> ticketCsvList = importCsvService.parseCSVFile(file, TicketLeadCsv.class, separator);
        //validation des données
        ticketCsvList = importCsvService.validateDataCsv(ticketCsvList, file);

        Set<Map<String, Object>> tickets = new HashSet<>();

        for (TicketLeadCsv ticketCsv : ticketCsvList) {
            if (ticketCsv.getType().equalsIgnoreCase("ticket")){
                Ticket temp = new Ticket();
                Map<String, Object> map = new HashMap<>();

                ticketCsv.setUserMail(ticketCsv.getEmployeeMail());
                //Utilisateur authentifier (MANAGER OR EMPLOYE)
                User user = userService.findByEmail(ticketCsv.getUserMail());
                if (user == null)
                    throw new UsernameNotFoundException(ticketCsv.getUserMail() + " not found");

                Customer customer = customerService.findByEmail(ticketCsv.getCustomerMail());
                if (customer == null)
                    throw new UsernameNotFoundException(ticketCsv.getCustomerMail() + " not found");

                temp.setSubject(ticketCsv.getSubject());
                temp.setStatus(ticketCsv.getStatus());
                temp.setPriority(ticketCsv.getPriority());
                temp.setCustomer(customer);
                temp.setManager(user);
                temp.setEmployee(user);
                temp.setCreatedAt(LocalDateTime.now());
                map.put("ticket", temp);
                map.put("expense", ticketCsv.getExpense());

                tickets.add(map);
            }
        }

        return tickets.stream().toList();
    }

    private List<Map<String, Object>> prepareLeadInstance(ImportCsvService importCsvService, MultipartFile file, String separator) throws IOException, ImportCsvException {
        //parse le fichier csv et extraire les données
        List<TicketLeadCsv> leadsCsv = importCsvService.parseCSVFile(file, TicketLeadCsv.class, separator);
        //validation des données
        leadsCsv = importCsvService.validateDataCsv(leadsCsv, file);

        Set<Map<String, Object>> resultats = new HashSet<>();

        for (TicketLeadCsv lead : leadsCsv) {
            if (lead.getType().equalsIgnoreCase("lead")){
                Lead temp = new Lead();
                Map<String, Object> map = new HashMap<>();

                temp.setName(lead.getSubject());
                temp.setStatus(lead.getStatus());

                //Utilisateur authentifier (MANAGER OR EMPLOYE)
                User user = userService.findByEmail(lead.getEmployeeMail());
                if (user == null)
                    throw new UsernameNotFoundException(lead.getEmployeeMail() + " not found");

                Customer customer = customerService.findByEmail(lead.getCustomerMail());
                if (customer == null)
                    throw new UsernameNotFoundException(lead.getCustomerMail() + " not found");

                temp.setCustomer(customer);
                temp.setManager(user);
                temp.setEmployee(user);
                temp.setCreatedAt(LocalDateTime.now());

                map.put("lead", temp);
                map.put("expense", lead.getExpense());
                resultats.add(map);
            }
        }

        return resultats.stream().toList();
    }

    @Transactional(rollbackOn = {IOException.class, ImportCsvException.class})
    public List<Ticket> uploadTicketData(ImportCsvService importCsvService, MultipartFile file, String separator) throws IOException, ImportCsvException {
        List<Map<String, Object>> tickets = this.prepareTicketInstance(importCsvService, file, separator);
        List<Ticket> finalTickets = new ArrayList<>();

        for (Map<String, Object> map : tickets) {
            Ticket ticket = (Ticket) map.get("ticket");
            finalTickets.add(ticket);

            Customer customer = ticket.getCustomer();
            List<Budget> customerBudget = budgetService.findCustomerBudgets(customer.getCustomerId());

            for (Budget budget : customerBudget) {
                if (budget.getConsomation().compareTo(budget.getAmount()) < 0){
                    Expense ex = new Expense();
                    ex.setAmount((BigDecimal) map.get("expense"));
                    ex.setExpenseDate(LocalDateTime.now());
                    ex.setBudget(budget);
                    ex.setTicket(ticket);
                    expenseService.save(ex);
                    break;
                }
            }
        }
        return this.ticketService.saveAll(finalTickets);
    }

    @Transactional(rollbackOn = {IOException.class, ImportCsvException.class})
    public List<Lead> uploadLeadData(ImportCsvService importCsvService, MultipartFile file, String separator) throws IOException, ImportCsvException {
        List<Map<String, Object>> resultats = this.prepareLeadInstance(importCsvService, file, separator);

        List<Lead> finalLeads = new ArrayList<>();

        for (Map<String, Object> map : resultats) {
            Lead ticket = (Lead) map.get("lead");
            finalLeads.add(ticket);

            Customer customer = ticket.getCustomer();
            List<Budget> customerBudget = budgetService.findCustomerBudgets(customer.getCustomerId());

            for (Budget budget : customerBudget) {
                if (budget.getConsomation().compareTo(budget.getAmount()) < 0){
                    Expense ex = new Expense();
                    ex.setAmount((BigDecimal) map.get("expense"));
                    ex.setExpenseDate(LocalDateTime.now());
                    ex.setBudget(budget);
                    ex.setLead(ticket);
                    expenseService.save(ex);
                    break;
                }
            }
        }
        return this.leadService.saveAll(finalLeads);
    }

}
*/
