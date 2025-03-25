package site.easy.to.build.crm.service.budget;


import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.service.budget.dto.StatistiqueBudgetDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BudgetService {
    public List<Budget> findAll();
    public List<Budget> findAllByCustomer(Integer id, LocalDateTime now);
    public Budget findById(Integer id);
    public Budget save(Budget budget);
    public void delete(Budget budget);
    public StatistiqueBudgetDto getTotalBudgetAndExpensesByCustomerOrNull(Integer id, LocalDateTime startDate, LocalDateTime endDate);

    List<Budget> saveAll(List<Budget> budgets);

    List<Budget> findCustomerBudgets(Integer customerId);
}
