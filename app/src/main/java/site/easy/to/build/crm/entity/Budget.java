package site.easy.to.build.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @NotNull
    Customer customer;

    @Column(name = "alert_rate", nullable = false)
    @PositiveOrZero
    double alertRate;

    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    private String description;

    @OneToMany(mappedBy = "budget", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonManagedReference
    @ToString.Exclude()
    private List<Expense> expenses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull
    CategoryBudget categoryBudget;

    @Column(name = "archived_at")
    LocalDateTime achivedAt;


    public BigDecimal getConsomation(){
        if (this.expenses == null || this.expenses.size() == 0)
            return new BigDecimal(0);

        return this.expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal::add)
                .get();
    }

    public BigDecimal getReste(){
        BigDecimal reste = this.amount.subtract(getConsomation());
        return (getConsomation().compareTo(this.amount) <= 0 ) ? reste : new BigDecimal(0);
    }

    public boolean isAlertRateReached(){
        BigDecimal pourcentage = this.getConsomation().multiply(BigDecimal.valueOf(100)).divide(this.getAmount());
        return pourcentage.compareTo(BigDecimal.valueOf(this.alertRate)) >= 0;
    }
}
