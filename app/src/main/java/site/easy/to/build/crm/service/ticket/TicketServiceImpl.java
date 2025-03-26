package site.easy.to.build.crm.service.ticket;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.repository.ExpenseRepository;
import site.easy.to.build.crm.repository.TicketRepository;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.lead.dto.DetailStatisticTicketLead;
import site.easy.to.build.crm.service.lead.dto.StatisticTicketLead;
import site.easy.to.build.crm.service.ticket.dto.StatistiqueTicketDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService{
    private final TicketRepository ticketRepository;
    private final ExpenseRepository expenseRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, ExpenseRepository expenseRepository) {
        this.ticketRepository = ticketRepository;
        this.expenseRepository = expenseRepository;
    }

    private void normalizeDate(LocalDateTime start, LocalDateTime end){
        if(start != null && end == null) {
            end = LocalDateTime.now();
        } else if (start == null) {
            start = LocalDateTime.now();
        }
    }

    @Override
    public List<Ticket> findCustoerTicketsBetweenDate(Integer id, LocalDateTime startDate, LocalDateTime endDate) {
        List<Ticket> resultats = new ArrayList<>();

        if (id != null){
            if (startDate !=null && endDate != null){
                resultats = ticketRepository.findByCustomerCustomerIdAndCreatedAtBetween(id, startDate, endDate);
            } else if (startDate == null && endDate == null){
                resultats = ticketRepository.findByCustomerCustomerId(id);
            } else {
                this.normalizeDate(startDate, endDate);
                resultats = ticketRepository.findByCustomerCustomerIdAndCreatedAtBetween(id, startDate, endDate);
            }
        } else if (startDate !=null || endDate != null){
            this.normalizeDate(startDate, endDate);
            resultats = ticketRepository.findByCreatedAtBetween(startDate, endDate);
        } else {
            resultats = ticketRepository.findAll();
        }

        return resultats;
    }
    @Override
    public DetailStatisticTicketLead getByExpenseIdDetailMonthlyStatTicket(Integer id) {
        Tuple tuple = ticketRepository.getByExpenseIdDetailMonthlyStatTicket(id);

        return new DetailStatisticTicketLead(
                ((Number) tuple.get("ticketId")).intValue(),
                tuple.get("subject").toString(),
                tuple.get("email").toString(),
                (BigDecimal) tuple.get("amount"),
                ((Number) tuple.get("expenseId")).intValue()
        );
    }
    @Override
    public List<DetailStatisticTicketLead> getDetailMonthlyStatisticTicket(Integer year) {
        List<Tuple> detailMonthlyStatistiqueLead = ticketRepository.getDetailMonthlyStatistiqueTicket(year);

        return detailMonthlyStatistiqueLead.stream()
                .map(tuple -> new DetailStatisticTicketLead(
                        ((Number) tuple.get("ticketId")).intValue(),
                        tuple.get("subject").toString(),
                        tuple.get("email").toString(),
                        (BigDecimal) tuple.get("amount"),
                        ((Number) tuple.get("expenseId")).intValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticTicketLead> getMonthlyStatisticTicket(Integer year) {
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
    private List<StatisticTicketLead> getStatistics(Integer year) {
        List<Tuple> result = ticketRepository.monthlyStatisticTicket(year);

        return result.stream()
                .map(tuple -> new StatisticTicketLead(
                        ((Number) tuple.get("month")).intValue(),
                        ((Long) tuple.get("totalTicket")).intValue(),
                        (BigDecimal) tuple.get("totalAmount")
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<StatistiqueTicketDto> statistiqueByCustomerBetweenDate(Integer id, LocalDateTime startDate, LocalDateTime endDate) {
        List<StatistiqueTicketDto> stats = new ArrayList<>();
        List<Map<String, Object>> resultats;

        if (id != null){
            if (startDate !=null && endDate != null){
                resultats = ticketRepository.statisticByCustomerBetweenDate(id, startDate, endDate);
            } else if (startDate == null && endDate == null){
                resultats = ticketRepository.countAllByCustomer(id);
            } else {
                this.normalizeDate(startDate, endDate);
                resultats = ticketRepository.statisticByCustomerBetweenDate(id, startDate, endDate);
            }
        } else if (startDate !=null || endDate != null){
            this.normalizeDate(startDate, endDate);
            resultats = ticketRepository.statisticByDate(startDate, endDate);
        } else {
            resultats = ticketRepository.statisticByCustomers();
        }

        for (Map<String, Object> r : resultats) {
            StatistiqueTicketDto temp = new StatistiqueTicketDto();
            temp.setCustomerId((Integer) r.get("customer_id"));
            temp.setCustomerName((String) r.get("name"));
            temp.setTotalTicket(Math.toIntExact((long) r.get("total")));
            temp.setTotalExpense((BigDecimal) r.get("totalAmount"));

            stats.add(temp);
        }

        return stats;
    }

    @Override
    public List<Map<String, Object>> countTicketByYearOrNot(Integer year) {
        if (year != null){
            return ticketRepository.countAllByYear(year);
        }
        return ticketRepository.countAll();
    }

    @Override
    public Ticket findByTicketId(int id) {
        return ticketRepository.findByTicketId(id);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public void delete(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    @Override
    public List<Ticket> findManagerTickets(int id) {
        return ticketRepository.findByManagerId(id);
    }

    @Override
    public List<Ticket> findEmployeeTickets(int id) {
        return ticketRepository.findByEmployeeId(id);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> findCustomerTickets(int id) {
        return ticketRepository.findByCustomerCustomerId(id);
    }



    @Override
    public List<Ticket> getRecentTickets(int managerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByManagerIdOrderByCreatedAtDesc(managerId, pageable);
    }

    @Override
    public List<Ticket> getRecentEmployeeTickets(int employeeId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId, pageable);
    }

    @Override
    public List<Ticket> getRecentCustomerTickets(int customerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId, pageable);
    }

    @Override
    public long countByEmployeeId(int employeeId) {
        return ticketRepository.countByEmployeeId(employeeId);
    }

    @Override
    public long countByManagerId(int managerId) {
        return ticketRepository.countByManagerId(managerId);
    }

    @Override
    public long countByCustomerCustomerId(int customerId) {
        return ticketRepository.countByCustomerCustomerId(customerId);
    }

    @Override
    public void deleteAllByCustomer(Customer customer) {
        ticketRepository.deleteAllByCustomer(customer);
    }

    @Override
    public List<Ticket> saveAll(List<Ticket> tickets) {
        return this.ticketRepository.saveAll(tickets);
    }
}
