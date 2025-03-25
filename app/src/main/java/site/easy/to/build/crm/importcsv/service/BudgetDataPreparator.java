/*
package site.easy.to.build.crm.importcsv.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.importcsv.exception.ImportCsvException;
import site.easy.to.build.crm.importcsv.model.BudgetCsv;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BudgetDataPreparator {
    private final BudgetService budgetService;
    private final CustomerService customerService;

    @Autowired
    public BudgetDataPreparator(BudgetService budgetService, CustomerService customerService) {
        this.budgetService = budgetService;
        this.customerService = customerService;
    }

    private List<Budget> prepareBudgetInstance(ImportCsvService importCsvService, MultipartFile file, String separator) throws IOException, ImportCsvException {
        //parse le fichier csv et extraire les données
        List<BudgetCsv> budgetsCsv = importCsvService.parseCSVFile(file, BudgetCsv.class, separator);
        //validation des données
        budgetsCsv = importCsvService.validateDataCsv(budgetsCsv, file);

        Set<Budget> budgets = new HashSet<>();

        for (BudgetCsv b : budgetsCsv) {
            Budget temp = new Budget();

            final String tempEmail = b.getCustomerEmail();
            Customer c = customerService.findByEmail(tempEmail);

            if (c == null) {
                c = new Customer();
                c.setName("temp");
                c.setEmail(tempEmail);
                c.setCountry("temp");
                //Utilisateur authentifier (MANAGER OR EMPLOYE)
                User user = userService.findById(customerCsv.getUser());
                c = customerService.save(c);
            }

            temp.setCustomer(c);
            temp.setAmount(b.getBudget());
            temp.setCreatedAt(LocalDateTime.now());
            temp.setStartDate(LocalDateTime.now().withYear(2010));
            temp.setDescription("");

            CategoryBudget cb = new CategoryBudget();
            cb.setId("CAT001");
            cb.setCategoryName("Service");
            temp.setCategoryBudget(cb);

            budgets.add(temp);
        }

        return budgets.stream().toList();
    }

    public List<Budget> uploadBudgetData(ImportCsvService importCsvService, MultipartFile file, String separator) throws IOException, ImportCsvException {
        return  this.prepareBudgetInstance(importCsvService, file, separator);
        //return this.budgetService.saveAll(budgets);
    }

}
*/
