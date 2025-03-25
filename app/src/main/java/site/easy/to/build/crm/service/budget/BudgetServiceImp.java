package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.exception.BudgetNotFoundException;
import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.service.budget.dto.StatistiqueBudgetDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Budget> findAllByCustomer(Integer id, LocalDateTime now) {
        return budgetRepository.findAllByCustomerCustomerId(id, now);
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
        budget.setAchivedAt(LocalDateTime.now());
        budgetRepository.save(budget);
    }

    @Override
    public StatistiqueBudgetDto getTotalBudgetAndExpensesByCustomerOrNull(Integer customerId, LocalDateTime startDate, LocalDateTime endDate) {
        StatistiqueBudgetDto stat = new StatistiqueBudgetDto();
        Map<String, Object> resultat = new HashMap<>();

        if (customerId != null ){
            if (startDate == null && endDate == null){
                resultat = budgetRepository.totalBudgetAndExpensesByCustomer(customerId);
            } else {
                this.normalizeDate(startDate, endDate);
                resultat = budgetRepository.getTotalBudgetAndExpensesByCustomer(customerId, startDate, endDate);
            }

            if(!resultat.isEmpty()) {
                stat.setCustomerName((String) resultat.get("name"));
                stat.setCustomerId((Integer) resultat.get("customer_id"));
            }
        } else if (startDate != null || endDate != null) {
            this.normalizeDate(startDate, endDate);
            resultat = budgetRepository.getTotalBudgetAndExpensesByDate(startDate, endDate);
        } else {
            resultat = budgetRepository.getTotalBudgetAndExpenses();
        }

        if(!resultat.isEmpty()){
            stat.setTotalBudget((BigDecimal) resultat.get("totalBudget"));
            stat.setTotalExpense((BigDecimal) resultat.get("totalExpense"));

            BigDecimal reste = (stat.getTotalBudget().compareTo(stat.getTotalExpense()) > 0) ? stat.getTotalBudget().subtract(stat.getTotalExpense()) : BigDecimal.valueOf(0);
            stat.setTotalRest(reste);
            return stat;
        }
        return null;
    }

    @Override
    public List<Budget> saveAll(List<Budget> budgets) {
        return budgetRepository.saveAll(budgets);
    }

    @Override
    public List<Budget> findCustomerBudgets(Integer customerId) {
        return budgetRepository.findCustomerBudgets(customerId);
    }

    private void normalizeDate(LocalDateTime start, LocalDateTime end){
        if(start != null && end == null) {
            end = LocalDateTime.now();
        } else if (start == null) {
            start = LocalDateTime.now();
        }
    }
}
