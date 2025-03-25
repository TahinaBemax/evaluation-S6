package site.easy.to.build.crm.importcsv.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.importcsv.exception.ImportCsvException;
import site.easy.to.build.crm.importcsv.model.BudgetCsv;
import site.easy.to.build.crm.importcsv.model.CustomerCsv;
import site.easy.to.build.crm.importcsv.model.TicketLeadCsv;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.expense.ExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.EmailTokenUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DataPreparator {
    private final Validator validator;
    private final UserService userService;
    private final CustomerLoginInfoService customerLoginInfoService;
    private final CustomerService customerService;
    private final BudgetService budgetService;
    private final TicketService ticketService;
    private final LeadService leadService;
    private final ExpenseService expenseService;

    private Set<Customer> customers = new HashSet<>();
    private Set<Budget> budgets = new HashSet<>();
    private Set<Expense> expenses = new HashSet<>();
    private Set<Ticket> tickets = new HashSet<>();
    private Set<Lead> leads = new HashSet<>();

    private List<TicketLeadCsv> ticketLeadFromCsv = new ArrayList<>();
    private List<BudgetCsv> budgetsFromCsv  = new ArrayList<>();
    private List<TicketLeadCsv> leadsFromCsv  = new ArrayList<>();
    private List<CustomerCsv> customerFromCsv  = new ArrayList<>();
    private User user;
    @Autowired
    public DataPreparator(Validator validator, UserService userService, CustomerLoginInfoService customerLoginInfoService, CustomerService customerService, BudgetService budgetService, TicketService ticketService, LeadService leadService, ExpenseService expenseService) {
        this.validator = validator;
        this.userService = userService;
        this.customerLoginInfoService = customerLoginInfoService;
        this.customerService = customerService;
        this.budgetService = budgetService;
        this.ticketService = ticketService;
        this.leadService = leadService;
        this.expenseService = expenseService;

        this.user = this.userService.findByEmail("tahina.nambinintsoa@passerellesnumeriques.org");
        this.customers.addAll(this.customerService.findAll());
    }

    public void initParse(MultipartFile customerFile, MultipartFile budgetFile, MultipartFile tickedLeadFile, String separator) throws IOException, ImportCsvException {
        //parse le fichier csv et extraire les données
        this.ticketLeadFromCsv = validateDataCsv(parseCSVFile(tickedLeadFile, TicketLeadCsv.class, separator), tickedLeadFile);
        this.budgetsFromCsv = validateDataCsv(parseCSVFile(budgetFile, BudgetCsv.class, separator), budgetFile);
        this.leadsFromCsv = validateDataCsv(parseCSVFile(tickedLeadFile, TicketLeadCsv.class, separator), tickedLeadFile);
        this.customerFromCsv = validateDataCsv(parseCSVFile(customerFile, CustomerCsv.class, separator), customerFile);
    }

    @Transactional(rollbackOn = {IOException.class, ImportCsvException.class})
    public void uploadData() throws ImportCsvException, IOException {
        this.prepareBudgetInstance();
        this.prepareTicketInstance();
        this.prepareLeadInstance();
        this.prepareCustomerInstance();

        customerService.saveAll(this.customers.stream().toList());
        //budgetService.saveAll(this.budgets.stream().toList());
        //ticketService.saveAll(this.tickets.stream().toList());
        //leadService.saveAll(this.leads.stream().toList());
    }

    public <T> List<T> parseCSVFile(MultipartFile file, Class<T> type, String separator) throws IOException {
        if (separator == null)
            separator = ";";

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(separator.charAt(0))
                    .build();

            return csvToBean.parse();
        }
    }

    public <T> List<T> validateDataCsv(List<T> list, MultipartFile file) throws ImportCsvException {
        int ligne = 1;
        List<T> valides = new ArrayList<>();

        for (T t : list) {
            Set<ConstraintViolation<T>> violations = validator.validate(t);
            if (!violations.isEmpty()) {
                List<String> messages = new ArrayList<>();
                for (ConstraintViolation<T> violation : violations) {
                    messages.add(violation.getMessage());
                }
                String fileName = file.getOriginalFilename();
                throw new ImportCsvException(messages, ligne, fileName);
            }

            valides.add(t);
            ligne++;
        }

        return valides;
    }


    private void prepareBudgetInstance() {
        for (BudgetCsv b : budgetsFromCsv) {
            Budget temp = new Budget();

            String tempEmail = b.getCustomerEmail();
            Customer c = this.getCustomer(tempEmail);

            temp.setCustomer(c);
            temp.setAlertRate(80);
            temp.setAmount(b.getBudget());
            temp.setCreatedAt(LocalDateTime.now());
            temp.setStartDate(LocalDateTime.now().withYear(2010));
            temp.setDescription("");

            CategoryBudget cb = new CategoryBudget();
            cb.setId("CAT001");
            cb.setCategoryName("Service");
            temp.setCategoryBudget(cb);

            c.getBudgets().add(temp);
        }
    }
    private void prepareTicketInstance() throws IOException, ImportCsvException {
        for (TicketLeadCsv ticketCsv : ticketLeadFromCsv) {
            Customer newCustomer;
            if (ticketCsv.getType().equalsIgnoreCase("ticket")){
                Ticket temp = new Ticket();

                User managerUser = getManagerUser();
                if (managerUser == null)
                    throw new UsernameNotFoundException("Username not found");

                temp.setSubject(ticketCsv.getSubject());
                temp.setStatus(ticketCsv.getStatus());
                temp.setPriority(ticketCsv.getPriority());

                newCustomer = getCustomer(ticketCsv.getCustomerMail());
                temp.setCustomer(newCustomer);
                temp.setManager(managerUser);
                temp.setEmployee(managerUser);
                temp.setCreatedAt(LocalDateTime.now());

                temp.getExpense().add(createNewExpense(ticketCsv, newCustomer, temp, null));
            }
        }
    }
    private void prepareLeadInstance() throws IOException, ImportCsvException {
        for (TicketLeadCsv lead : this.leadsFromCsv) {
            if (lead.getType().equalsIgnoreCase("lead")){
                Lead temp = new Lead();

                temp.setName(lead.getSubject());
                temp.setStatus(lead.getStatus());

                //Utilisateur authentifier (MANAGER OR EMPLOYE)
                User user = getManagerUser();
                if (user == null)
                    throw new UsernameNotFoundException(lead.getEmployeeMail() + " not found");

                Customer customer = this.getCustomer(lead.getCustomerMail());

                temp.setCustomer(customer);
                temp.setManager(user);
                temp.setEmployee(user);
                temp.setCreatedAt(LocalDateTime.now());
                temp.getExpenses().add(createNewExpense(lead, customer, null, temp));
            }
        }
    }
    private List<Customer> prepareCustomerInstance() throws IOException, ImportCsvException {
        for (CustomerCsv customerCsv : customerFromCsv) {
            boolean isExiste = true;
            Customer customer = getCustomerByEmail(customerCsv.getEmail());

            if (customer == null){
                customer = new Customer();
                isExiste = false;
            }
            //Utilisateur authentifier (MANAGER OR EMPLOYE)
            User user =  getManagerUser();

            customer.setUser(user);
            customer.setName(customerCsv.getName());
            customer.setCreatedAt(customerCsv.getCreatedAt());
            customer.setEmail(customerCsv.getEmail());
            customer.setCountry(customerCsv.getCountry());

            //credentials info for the new customers
            CustomerLoginInfo createdCustomerLoginInfo = this.prepareCustomerLoginInfo(customer);

            customer.setCustomerLoginInfo(createdCustomerLoginInfo);

            if (!isExiste){
                this.customers.add(customer);
            }
        }

        return customers.stream().toList();
    }


    private Budget getBudget(Customer customer){
        Optional<Budget> budget = customer.getBudgets()
                .stream()
                .filter(b -> b.getConsomation().compareTo(b.getAmount()) < 0)
                .findFirst();

        return budget.orElse(null);
    }
    private Customer getCustomer(String email){
        Optional<Customer> customer = this.customers.stream()
                .filter(client -> client.getEmail().equalsIgnoreCase(email))
                .findFirst();

        if (customer.isEmpty()){
            Customer c = this.createNewCustomer(email);
            this.customers.add(c);
            return c;
        }
        return customer.get();
    }
    private Customer getCustomerByEmail(String email){
        Optional<Customer> customer = this.customers.stream()
                .filter(client -> client.getEmail().equalsIgnoreCase(email))
                .findFirst();

        return customer.orElse(null);
    }
    private Expense createNewExpense(TicketLeadCsv ticketCsv, Customer customer, Ticket ticket, Lead lead){
        Expense expense = new Expense();
        expense.setAmount(ticketCsv.getExpense());
        expense.setExpenseDate(LocalDateTime.now());

        Budget budget = getBudget(customer);
        expense.setBudget(budget);
        expense.setDescription("");
        expense.setTicket(ticket);
        expense.setLead(lead);

        budget.getExpenses().add(expense);
        return expense;
    }
    private Customer createNewCustomer(String email){
        Customer c = new Customer();
        c.setName("temp");
        c.setEmail(email);
        c.setCountry("temp");

        c.setUser(this.getManagerUser());

        //CustomerLoginInfo customerLoginInfo = prepareCustomerLoginInfo(c);
        //c.setCustomerLoginInfo(customerLoginInfo);

        return c;
    }
    private CustomerLoginInfo prepareCustomerLoginInfo(Customer customer){
        CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();

        customerLoginInfo.setEmail(customer.getEmail());
        String token = EmailTokenUtils.generateToken();
        customerLoginInfo.setToken(token);
        customerLoginInfo.setPasswordSet(false);

        return customerLoginInfoService.save(customerLoginInfo);
    }
    private User getManagerUser(){
        return this.user;
    }
}
