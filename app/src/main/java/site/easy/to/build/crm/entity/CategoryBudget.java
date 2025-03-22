package site.easy.to.build.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CategoryBudget {
    @Id
    @Column(name = "category_id", unique = true, nullable = false)
    String id;

    @Column(name = "category_name", unique = true, nullable = false)
    String categoryName;

    @OneToMany
    List<Budget> budgets;
}
