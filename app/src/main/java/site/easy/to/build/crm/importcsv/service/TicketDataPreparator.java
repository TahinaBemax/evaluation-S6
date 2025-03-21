package site.easy.to.build.crm.importcsv.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.importcsv.exception.ImportCsvException;
import site.easy.to.build.crm.importcsv.model.CustomerCsv;
import site.easy.to.build.crm.importcsv.model.TicketCsv;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.EmailTokenUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TicketDataPreparator {
    private final TicketService ticketService;
    private final UserService userService;
    private final CustomerService customerService;

    @Autowired
    public TicketDataPreparator(UserService userService, TicketService ticketService, CustomerService customerService) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.customerService = customerService;
    }

    private List<Ticket> prepareTicketInstance(ImportCsvService importCsvService, MultipartFile file, String separator) throws IOException, ImportCsvException {
        //parse le fichier csv et extraire les données
        List<TicketCsv> ticketCsvList = importCsvService.parseCSVFile(file, TicketCsv.class, separator);
        //validation des données
        ticketCsvList = importCsvService.validateDataCsv(ticketCsvList);

        Set<Ticket> tickets = new HashSet<>();

        for (TicketCsv ticketCsv : ticketCsvList) {
            Ticket temp = new Ticket();

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
            temp.setCreatedAt(ticketCsv.getCreatedAt());

            tickets.add(temp);
        }

        return tickets.stream().toList();
    }


    @Transactional(rollbackOn = {IOException.class, ImportCsvException.class})
    public List<Ticket> uploadTicketData(ImportCsvService importCsvService, MultipartFile file, String separator) throws IOException, ImportCsvException {
        List<Ticket> Tickets = this.prepareTicketInstance(importCsvService, file, separator);
        return this.ticketService.saveAll(Tickets);
    }

}
