package site.easy.to.build.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import site.easy.to.build.crm.customValidations.expense.OneNullAndOneNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@OneNullAndOneNotNull
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Integer id;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Amount is required")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid number with up to 2 decimal places")
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be greater than or equal to 0.00")
    @DecimalMax(value = "9999999.99", inclusive = true, message = "Amount must be less than or equal to 9999999.99")
    private BigDecimal amount;

    @Column(name = "expense_date", nullable = false)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expenseDate;

    @ManyToOne
    @JoinColumn(name = "budget_id", nullable = false)
    @JsonIgnoreProperties("expenses")
    @NotNull
    Budget budget;

    private String description;

    @ManyToOne()
    @JoinColumn(name = "ticket_id")
    @JsonIgnoreProperties("expense")
    Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    @JsonIgnoreProperties("expenses")
    Lead lead;

}
