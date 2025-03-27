package site.easy.to.build.crm.importcsv.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import jakarta.validation.constraints.*;
import lombok.Data;
import site.easy.to.build.crm.importcsv.annotation.ValidNumber;
import site.easy.to.build.crm.importcsv.validator.BigDecimalConverter;
import site.easy.to.build.crm.importcsv.annotation.NotBlankOrNull;
import site.easy.to.build.crm.importcsv.annotation.ValidEmail;

import java.math.BigDecimal;

@Data
public class TicketLeadCsv {
    @CsvBindByName(column = "customer_email")
    @ValidEmail
    private String customerMail;

    @CsvBindByName(column = "subject_or_name")
    @NotBlank(message = "Subject is required")
    @NotBlankOrNull
    private String subject;

    @CsvBindByName(column = "type")
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(lead|ticket)$", message = "Invalid type")
    @NotBlankOrNull
    private String type;

    @CsvBindByName(column = "status")
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(open|assigned|on-hold|in-progress|resolved|closed|reopened|pending-customer-response|escalated|archived|" +
            "meeting-to-schedule|scheduled|archived|success|assign-to-sales)$", message = "Invalid status")
    private String status;

    @CsvCustomBindByName(column = "expense", converter = BigDecimalConverter.class)
    @ValidNumber
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid number with up to 2 decimal places")
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be greater than or equal to 0.00")
    @DecimalMax(value = "9999999.99", inclusive = true, message = "Amount must be less than or equal to 9999999.99")
    private BigDecimal expense;

    private String priority = "medium";


    @Email
    private String employeeMail = "tahina.nambinintsoa@passerellesnumeriques.org";
}
