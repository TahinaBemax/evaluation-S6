package site.easy.to.build.crm.service.budget;

import site.easy.to.build.crm.entity.CategoryBudget;

import java.util.List;

public interface CategoryBudgetService {
    public List<CategoryBudget> findAll();
    public CategoryBudget findById(String id);
    public CategoryBudget save(CategoryBudget categoryBudget);
    public void delete(CategoryBudget categoryBudget);
}
