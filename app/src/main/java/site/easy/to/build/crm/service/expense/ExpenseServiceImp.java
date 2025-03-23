package site.easy.to.build.crm.service.expense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.repository.ExpenseRepository;

import java.util.List;

@Service
public class ExpenseServiceImp implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseServiceImp(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Override
    public Expense findById(Integer id) {
        return expenseRepository.findById(id).orElseThrow();
    }

    @Override
    public Expense save(Expense budget) {
        return expenseRepository.save(budget);
    }

    @Override
    public void delete(Expense budget) {
        expenseRepository.delete(budget);
    }

    @Override
    public List<Expense> findByTicketId(Integer id) {
        return expenseRepository.findAllByTicket(id);
    }

    @Override
    public List<Expense> findByLeadId(Integer id) {
        return expenseRepository.findAllByLead(id);
    }
}
