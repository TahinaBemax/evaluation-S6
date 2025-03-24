package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    public Ticket findByTicketId(int ticketId);

    public List<Ticket> findByManagerId(int id);

    public List<Ticket> findByEmployeeId(int id);

    List<Ticket> findByCustomerCustomerId(Integer customerId);

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
