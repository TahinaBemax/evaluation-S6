package site.easy.to.build.crm.service.expense;


import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Expense;

import java.util.List;

public interface ExpenseService {
    public List<Expense> findAll();
    public Expense findById(Integer id);
    public Expense save(Expense expense);
    public void delete(Expense expense);

    public List<Expense> findByTicketId(Integer id);
    public List<Expense> findByLeadId(Integer id);
    public List<Expense> findAllByBudgetId(Integer id);
}
