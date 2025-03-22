package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.exception.BudgetNotFoundException;
import site.easy.to.build.crm.repository.BudgetRepository;

import java.util.List;

@Service
public class BudgetServiceImp implements BudgetService{
    private final BudgetRepository budgetRepository;

    @Autowired
    public BudgetServiceImp(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    @Override
    public Budget findById(Integer id) {
        return budgetRepository.findById(id).orElseThrow();
    }

    @Override
    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public void delete(Budget budget) {
        budgetRepository.delete(budget);
    }
}
