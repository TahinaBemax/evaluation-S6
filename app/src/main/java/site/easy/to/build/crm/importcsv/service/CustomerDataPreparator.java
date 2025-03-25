/*
package site.easy.to.build.crm.importcsv.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.OAuthUser;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.google.service.acess.GoogleAccessService;
import site.easy.to.build.crm.importcsv.exception.ImportCsvException;
import site.easy.to.build.crm.importcsv.model.CustomerCsv;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.EmailTokenUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerDataPreparator {

    private final UserService userService;
    private final CustomerLoginInfoService customerLoginInfoService;
    private final CustomerService customerService;

    @Autowired
    public CustomerDataPreparator( UserService userService, CustomerLoginInfoService customerLoginInfoService, CustomerService customerService) {
        this.userService = userService;
        this.customerLoginInfoService = customerLoginInfoService;
        this.customerService = customerService;
    }

    private List<Customer> prepareCustomerInstance(ImportCsvService importCsvService, MultipartFile file, String separator) throws IOException, ImportCsvException {
        //parse le fichier csv et extraire les données
        List<CustomerCsv> customerCsvList = importCsvService.parseCSVFile(file, CustomerCsv.class, separator);
        //validation des données
        customerCsvList = importCsvService.validateDataCsv(customerCsvList, file);

        Set<Customer> customers = new HashSet<>();

        for (CustomerCsv customerCsv : customerCsvList) {
            Customer customer = new Customer();
            //Utilisateur authentifier (MANAGER OR EMPLOYE)
            User user = userService.findById(customerCsv.getUser());

            customer.setUser(user);
            customer.setName(customerCsv.getName());
            customer.setCreatedAt(customerCsv.getCreatedAt());
            customer.setEmail(customerCsv.getEmail());
            customer.setCountry(customerCsv.getCountry());

            //credentials info for the new customers
            CustomerLoginInfo createdCustomerLoginInfo = this.prepareCustomerLoginInfo(customer);

            customer.setCustomerLoginInfo(createdCustomerLoginInfo);
            customers.add(customer);
        }

        return customers.stream().toList();
    }

    private CustomerLoginInfo prepareCustomerLoginInfo(Customer customer){
        CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();

        customerLoginInfo.setEmail(customer.getEmail());
        String token = EmailTokenUtils.generateToken();
        customerLoginInfo.setToken(token);
        customerLoginInfo.setPasswordSet(false);

        return customerLoginInfoService.save(customerLoginInfo);
    }

    public List<Customer> uploadOptionsData(ImportCsvService importCsvService, MultipartFile file, String separator) throws IOException, ImportCsvException {
        return this.prepareCustomerInstance(importCsvService, file, separator);
        //return this.customerService.saveAllAndFlush(Customers);
    }

}
*/
