package site.easy.to.build.crm.service.budget;


import site.easy.to.build.crm.entity.Budget;

import java.util.List;

public interface BudgetService {
    public List<Budget> findAll();
    public Budget findById(Integer id);
    public Budget save(Budget budget);
    public void delete(Budget budget);
}
