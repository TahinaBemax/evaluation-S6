package site.easy.to.build.crm.importcsv.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketCsv {
    @CsvBindByName(column = "subject_or_name")
    @NotBlank(message = "Subject is required")
    private String subject;

    @CsvBindByName(column = "status")
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(open|assigned|on-hold|in-progress|resolved|closed|reopened|pending-customer-response|escalated|archived)$", message = "Invalid status")
    private String status;

    @CsvBindByName(column = "priority")
    @NotBlank(message = "Priority is required")
    @Pattern(regexp = "^(low|medium|high|closed|urgent|critical)$", message = "Invalid priority")
    private String priority;

    @CsvBindByName(column = "user")
    @Email
    private String userMail; //mettre ceci en tahina.nambinintsoa@passerellesnumeriques.org

    @CsvBindByName(column = "employee")
    @Email
    private String employeeMail; //mettre ceci en tahina.nambinintsoa@passerellesnumeriques.org

    @CsvBindByName(column = "customer")
    @Email
    private String customerMail;

    @CsvBindByName(column = "created_at")
    @CsvDate("dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;
}
