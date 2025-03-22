package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.easy.to.build.crm.entity.CategoryBudget;

public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, String> {
}
