package site.easy.to.build.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Integer id;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Amount is required")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid number with up to 2 decimal places")
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be greater than or equal to 0.00")
    @DecimalMax(value = "9999999.99", inclusive = true, message = "Amount must be less than or equal to 9999999.99")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable=false)
    @JsonIgnoreProperties("budgets")
    Customer customer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @OneToMany()
    @JoinColumn(name = "expense_id")
    private List<Expense> expenses;

}
