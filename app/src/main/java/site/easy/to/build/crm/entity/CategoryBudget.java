package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Data
public class CategoryBudget {
    @Id
    @Column(name = "category_id", unique = true, nullable = false)
    String id;

    @Column(name = "category_name", unique = true, nullable = false)
    String categoryName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    @JsonIgnore()
    List<Budget> budgets;
}
