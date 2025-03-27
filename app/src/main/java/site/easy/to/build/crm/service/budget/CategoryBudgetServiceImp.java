package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.CategoryBudget;
import site.easy.to.build.crm.repository.CategoryBudgetRepository;

import java.util.List;

@Service
public class CategoryBudgetServiceImp implements CategoryBudgetService{
    private final CategoryBudgetRepository budgetRepository;

    @Autowired
    public CategoryBudgetServiceImp(CategoryBudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public List<CategoryBudget> findAll() {
        return budgetRepository.findAll();
    }

    @Override
    public CategoryBudget findById(String id) {
        return budgetRepository.findById(id).orElseThrow();
    }

    @Override
    public CategoryBudget save(CategoryBudget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public void delete(CategoryBudget budget) {
        budgetRepository.delete(budget);
    }
}
