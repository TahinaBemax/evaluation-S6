package site.easy.to.build.crm.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Integer> {
    public Lead findByLeadId(int id);

    public List<Lead> findByCustomerCustomerId(int customerId);
    public List<Lead> findByManagerId(int userId);

    public List<Lead> findByEmployeeId(int userId);

    Lead findByMeetingId(String meetingId);

    public List<Lead> findByEmployeeIdOrderByCreatedAtDesc(int employeeId, Pageable pageable);

    public List<Lead> findByManagerIdOrderByCreatedAtDesc(int managerId, Pageable pageable);

    public List<Lead> findByCustomerCustomerIdOrderByCreatedAtDesc(int customerId, Pageable pageable);

    long countByEmployeeId(int employeeId);

    long countByManagerId(int managerId);
    long countByCustomerCustomerId(int customerId);

    void deleteAllByCustomer(Customer customer);
    @Query("""
    SELECT COUNT(l) AS totalLead, 
           EXTRACT(MONTH FROM l.createdAt) AS month, 
           COALESCE(SUM(e.amount), 0) AS totalAmount
    FROM Lead l 
    LEFT JOIN l.expenses e 
    WHERE (:year IS NULL OR EXTRACT(YEAR FROM l.createdAt) = :year)
    GROUP BY month
""")
    List<Tuple> monthlyStatisticLead(@Param("year") Integer year);

    @Query("""
    SELECT l.leadId as leadId, l.name as name, l.customer.email as email, COALESCE(e.amount, 0) as amount, COALESCE(e.id, 0) as expenseId
    FROM Lead l 
    LEFT JOIN l.expenses e 
    WHERE (:year IS NULL OR EXTRACT(YEAR FROM l.createdAt) = :year)
""")
    List<Tuple> getDetailMonthlyStatistiqueLead(@Param("year") Integer year);

    @Query("""
    SELECT l.leadId as leadId, l.name as name, l.customer.email as email, COALESCE(e.amount, 0) as amount, COALESCE(e.id, 0) as expenseId
    FROM Lead l 
    LEFT JOIN l.expenses e 
    WHERE e.id = :id
""")
    Tuple getByExpenseIdDetailMonthlyStatLead(@Param("id") Integer id);
}
