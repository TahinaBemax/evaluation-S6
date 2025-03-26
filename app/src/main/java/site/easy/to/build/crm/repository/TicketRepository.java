package site.easy.to.build.crm.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Query("""
    SELECT COUNT(t) AS totalTicket, 
           EXTRACT(MONTH FROM t.createdAt) AS month, 
           COALESCE(SUM(e.amount), 0) AS totalAmount
    FROM Ticket t
    LEFT JOIN t.expense e 
    WHERE (:year IS NULL OR EXTRACT(YEAR FROM t.createdAt) = :year)
    GROUP BY month
""")
    List<Tuple> monthlyStatisticTicket(@Param("year") Integer year);

    @Query("""
    SELECT l.ticketId as ticketId, l.subject as subject, l.customer.email as email, COALESCE(e.amount, 0) as amount, COALESCE(e.id, 0) as expenseId
    FROM Ticket l 
    LEFT JOIN l.expense e 
    WHERE (:year IS NULL OR EXTRACT(YEAR FROM l.createdAt) = :year)
""")
    List<Tuple> getDetailMonthlyStatistiqueTicket(@Param("year") Integer year);

    @Query("""
        SELECT l.ticketId as ticketId, l.subject as subject, l.customer.email as email, COALESCE(e.amount, 0) as amount, COALESCE(e.id, 0) as expenseId
        FROM Ticket l 
        LEFT JOIN l.expense e 
        WHERE e.id = :id
    """)
    Tuple getByExpenseIdDetailMonthlyStatTicket(@Param("id") Integer id);

    public Ticket findByTicketId(int ticketId);

    public List<Ticket> findByManagerId(int id);

    public List<Ticket> findByEmployeeId(int id);

    List<Ticket> findByCustomerCustomerId(Integer customerId);
    List<Ticket> findByCustomerCustomerIdAndCreatedAtBetween(Integer customerId, LocalDateTime startDate, LocalDateTime endDate);
    List<Ticket> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Ticket> findByManagerIdOrderByCreatedAtDesc(int managerId, Pageable pageable);

    List<Ticket> findByEmployeeIdOrderByCreatedAtDesc(int managerId, Pageable pageable);

    List<Ticket> findByCustomerCustomerIdOrderByCreatedAtDesc(int customerId, Pageable pageable);

    long countByEmployeeId(int employeeId);

    long countByManagerId(int managerId);

    long countByCustomerCustomerId(int customerId);

    void deleteAllByCustomer(Customer customer);

    @Query("SELECT count(t) as total, extract(MONTH FROM t.createdAt) as month\n" +
            "FROM Ticket t GROUP BY month " +
            "ORDER BY extract(MONTH FROM t.createdAt)")
    List<Map<String, Object>> countAll();

    @Query("SELECT count(t) as total, extract(MONTH FROM t.createdAt) as month \n" +
            "FROM Ticket t WHERE extract(YEAR FROM t.createdAt) = :year\n" +
            "GROUP BY month \n" +
            "ORDER BY extract(MONTH FROM t.createdAt)")
    List<Map<String, Object>> countAllByYear(Integer year);

    @Query(value = "select count(*) as total, COALESCE(sum(vte.amount), 0) as totalAmount, vte.customer_id, c.name\n" +
            "FROM view_ticket_expense vte JOIN customer c on vte.customer_id = c.customer_id\n" +
            "WHERE c.customer_id = :id\n" +
            "GROUP BY c.customer_id;",nativeQuery = true)
    List<Map<String, Object>> countAllByCustomer(Integer id);

    @Query(value = "select count(*) as total, COALESCE(sum(vte.amount), 0) as totalAmount, vte.customer_id, c.name\n" +
            "FROM view_ticket_expense vte JOIN customer c on vte.customer_id = c.customer_id\n" +
            "GROUP BY c.customer_id;",nativeQuery = true)
    List<Map<String, Object>> statisticByCustomers();

    @Query(value = "select count(*) as total, COALESCE(sum(vte.amount), 0) as totalAmount, vte.customer_id, c.name\n" +
            "FROM view_ticket_expense vte JOIN customer c on vte.customer_id = c.customer_id\n" +
            "WHERE c.customer_id =:id AND vte.created_at BETWEEN :startDate AND :endDate \n" +
            "GROUP BY c.customer_id;",nativeQuery = true)
    List<Map<String, Object>> statisticByCustomerBetweenDate(Integer id, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "select count(*) as total, COALESCE(sum(vte.amount), 0) as totalAmount, vte.customer_id, c.name\n" +
            "FROM view_ticket_expense vte JOIN customer c on vte.customer_id = c.customer_id\n" +
            "WHERE vte.created_at BETWEEN :startDate AND :endDate \n" +
            "GROUP BY c.customer_id;",nativeQuery = true)
    List<Map<String, Object>> statisticByDate(LocalDateTime startDate, LocalDateTime endDate);
}
