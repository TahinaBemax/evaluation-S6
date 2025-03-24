package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.service.budget.dto.StatistiqueBudgetDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    @Override
    @Query("SELECT b FROM Budget b WHERE b.achivedAt is null")
    List<Budget> findAll();

    @Query("SELECT b FROM Budget b WHERE b.achivedAt is null AND b.customer.customerId = :id AND :now BETWEEN b.startDate and (COALESCE(b.endDate, :now) )")
    List<Budget> findAllByCustomerCustomerId(Integer id, LocalDateTime now);


    @Query(value = "SELECT\n" +
            "    COALESCE(SUM(vbe.budget_amount), 0) as totalBudget,\n" +
            "    COALESCE(SUM(vbe.expense_amount), 0) as totalExpense\n" +
            "FROM view_budget_expense vbe;" , nativeQuery = true)
    Map<String, Object> getTotalBudgetAndExpenses();

    @Query(value = "SELECT COALESCE(sum(vbe.budget_amount), 0) as totalBudget, COALESCE(sum(vbe.expense_amount),0) as totalExpense, vbe.customer_id, c.name\n" +
            "FROM view_budget_expense vbe \n" +
            "JOIN (SELECT c.customer_id, c.name FROM customer c \n" +
            "WHERE c.customer_id = :id) as c ON c.customer_id = vbe.customer_id\n" +
            "GROUP BY vbe.customer_id, c.name;" , nativeQuery = true)
    Map<String, Object> totalBudgetAndExpensesByCustomer(Integer id);

    @Query(value = "SELECT sum(vbe.budget_amount) as totalBudget, sum(vbe.expense_amount) as totalExpense, vbe.customer_id, c.name\n" +
            "FROM view_budget_expense vbe \n" +
            "JOIN (SELECT c.customer_id, c.name FROM customer c \n" +
            "WHERE c.customer_id = :id) as c ON c.customer_id = vbe.customer_id\n" +
            "WHERE vbe.start_date BETWEEN :startDate AND :endDate \n" +
            "GROUP BY vbe.customer_id, c.name;" , nativeQuery = true)
    Map<String, Object> getTotalBudgetAndExpensesByCustomer(Integer id, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT SUM(vbe.budget_amount) as totalBudget, SUM(vbe.expense_amount) as totalExpense\n" +
            "FROM view_budget_expense vbe \n" +
            "WHERE vbe.start_date BETWEEN :startDate AND :endDate;", nativeQuery = true)
    Map<String, Object> getTotalBudgetAndExpensesByDate(LocalDateTime startDate, LocalDateTime endDate);
}
