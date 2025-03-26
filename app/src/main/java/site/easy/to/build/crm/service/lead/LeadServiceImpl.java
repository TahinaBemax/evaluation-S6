package site.easy.to.build.crm.service.lead;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.repository.LeadRepository;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.service.lead.dto.DetailStatisticTicketLead;
import site.easy.to.build.crm.service.lead.dto.StatisticTicketLead;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;

    public LeadServiceImpl(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Override
    public List<DetailStatisticTicketLead> getDetailMonthlyStatistiqueLead(Integer year) {
        List<Tuple> detailMonthlyStatistiqueLead = leadRepository.getDetailMonthlyStatistiqueLead(year);

        return detailMonthlyStatistiqueLead.stream()
                .map(tuple -> new DetailStatisticTicketLead(
                        ((Number) tuple.get("leadId")).intValue(),
                        tuple.get("name").toString(),
                        tuple.get("email").toString(),
                        (BigDecimal) tuple.get("amount"),
                        ((Number) tuple.get("expenseId")).intValue()
                ))
                .collect(Collectors.toList());
    }
    @Override
    public DetailStatisticTicketLead getByExpenseIdDetailMonthlyStatLead(Integer id) {
        Tuple tuple = leadRepository.getByExpenseIdDetailMonthlyStatLead(id);

        return new DetailStatisticTicketLead(
                        ((Number) tuple.get("leadId")).intValue(),
                        tuple.get("name").toString(),
                        tuple.get("email").toString(),
                        (BigDecimal) tuple.get("amount"),
                        ((Number) tuple.get("expenseId")).intValue()
                );
    }

    @Override
    public List<StatisticTicketLead> getMonthlyStatistiqueLead(Integer year) {
        List<StatisticTicketLead> statisticTicketLeads = getStatistics(year);
        List<StatisticTicketLead> newStatisticTicketLeads = new ArrayList<>();

        for (int start = 1; start <= 12; start++) {
            final int startMonth = start;
            Optional<StatisticTicketLead> first = (statisticTicketLeads.stream().filter(s -> s.getMonth() == startMonth)).findFirst();

            if (first.isPresent()){
                newStatisticTicketLeads.add(first.get());
            } else {
                newStatisticTicketLeads.add(new StatisticTicketLead(start, 0, BigDecimal.valueOf(0)));
            }
        }

        return newStatisticTicketLeads;
    }
    public List<StatisticTicketLead> getStatistics(Integer year) {
        List<Tuple> result = leadRepository.monthlyStatisticLead(year);

        return result.stream()
                .map(tuple -> new StatisticTicketLead(
                        ((Number) tuple.get("month")).intValue(),
                        ((Long) tuple.get("totalLead")).intValue(),
                        (BigDecimal) tuple.get("totalAmount")
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Lead> saveAll(List<Lead> leads) {
        return leadRepository.saveAll(leads);
    }

    @Override
    public Lead findByLeadId(int id) {
        return leadRepository.findByLeadId(id);
    }

    @Override
    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    @Override
    public List<Lead> findAssignedLeads(int userId) {
        return leadRepository.findByEmployeeId(userId);
    }

    @Override
    public List<Lead> findCreatedLeads(int userId) {
        return leadRepository.findByManagerId(userId);
    }

    @Override
    public Lead findByMeetingId(String meetingId){
        return leadRepository.findByMeetingId(meetingId);
    }
    @Override
    public Lead save(Lead lead) {
        return leadRepository.save(lead);
    }

    @Override
    public void delete(Lead lead) {
        leadRepository.delete(lead);
    }

    @Override
    public List<Lead> getRecentLeadsByEmployee(int employeeId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return leadRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId, pageable);
    }

    @Override
    public List<Lead> getRecentCustomerLeads(int customerId, int limit) {
        Pageable pageable = PageRequest.of(0,limit);
        return leadRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId, pageable);
    }

    @Override
    public void deleteAllByCustomer(Customer customer) {
        leadRepository.deleteAllByCustomer(customer);
    }

    @Override
    public List<Lead> getRecentLeads(int managerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return leadRepository.findByManagerIdOrderByCreatedAtDesc(managerId, pageable);
    }

    @Override
    public List<Lead> getCustomerLeads(int customerId) {
        return leadRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public long countByEmployeeId(int employeeId) {
        return leadRepository.countByEmployeeId(employeeId);
    }

    @Override
    public long countByManagerId(int managerId) {
        return leadRepository.countByManagerId(managerId);
    }

    @Override
    public long countByCustomerId(int customerId) {
        return leadRepository.countByCustomerCustomerId(customerId);
    }
}
