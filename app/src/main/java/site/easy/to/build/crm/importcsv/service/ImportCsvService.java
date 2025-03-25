package site.easy.to.build.crm.importcsv.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.importcsv.ImportCsvDTO;
import site.easy.to.build.crm.importcsv.ImportCsvServiceDependence;
import site.easy.to.build.crm.importcsv.exception.ImportCsvException;
import site.easy.to.build.crm.importcsv.exception.TableNameNotFoundException;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ImportCsvService {
    private final Validator validator;
    private final CustomerDataPreparator customer;
    private final BudgetDataPreparator budget;
    private final TicketDataPreparator ticket;

    @Autowired
    public ImportCsvService(ImportCsvServiceDependence dependence, BudgetDataPreparator budget) {
        this.validator = dependence.getValidator();
        this.customer = dependence.getCustomerDataPreparator();
        this.ticket = dependence.getTicketDataPreparator();
        this.budget = budget;
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



    @Transactional(rollbackOn = {IOException.class, ImportCsvException.class})
    public void uploadCsvData(ImportCsvDTO info) throws ImportCsvException, IOException, TableNameNotFoundException {
        List<Customer> customers = this.customer.uploadOptionsData(this, info.getCustomerFile(), info.getSeparator());
        this.budget.uploadBudgetData(this, info.getBudgetFile(), info.getSeparator());
        this.ticket.uploadTicketData(this, info.getTicketFile(), info.getSeparator());
        this.ticket.uploadLeadData(this, info.getTicketFile(), info.getSeparator());
    }

    public void uploadCustomersCsvDataFirst(ImportCsvDTO info) throws ImportCsvException, IOException, TableNameNotFoundException {
        this.customer.uploadOptionsData(this, info.getCustomerFile(), info.getSeparator());
    }
}
