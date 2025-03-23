package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Ticket;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query("SELECT d FROM Expense d WHERE d.ticket.ticketId = :id")
    List<Expense> findAllByTicket(Integer id);

    @Query("SELECT d FROM Expense d WHERE d.lead.leadId = :id")
    List<Expense> findAllByLead(Integer id);
}
