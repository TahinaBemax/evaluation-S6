package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Budget;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    @Override
    @Query("SELECT b FROM Budget b WHERE b.achivedAt is null")
    List<Budget> findAll();


    @Query("SELECT b FROM Budget b WHERE b.achivedAt is null AND b.customer.customerId = :id AND :now BETWEEN b.startDate and (COALESCE(b.endDate, :now) )")
    List<Budget> findAllByCustomerCustomerId(Integer id, LocalDateTime now);
}
