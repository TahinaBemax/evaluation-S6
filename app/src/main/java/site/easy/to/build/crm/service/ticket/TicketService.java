package site.easy.to.build.crm.service.ticket;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.ticket.dto.StatistiqueTicketDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TicketService {
    public List<StatistiqueTicketDto> statistiqueByCustomerBetweenDate(Integer id, LocalDateTime startDate, LocalDateTime endDate);
    public List<Map<String, Object>> countTicketByYearOrNot(Integer year);

    public Ticket findByTicketId(int id);

    public Ticket save(Ticket ticket);

    public void delete(Ticket ticket);

    public List<Ticket> findManagerTickets(int id);

    public List<Ticket> findEmployeeTickets(int id);

    public List<Ticket> findAll();

    public List<Ticket> findCustomerTickets(int id);
    public List<Ticket> findCustoerTicketsBetweenDate(Integer id, LocalDateTime startDate, LocalDateTime endDate);

    List<Ticket> getRecentTickets(int managerId, int limit);

    List<Ticket> getRecentEmployeeTickets(int employeeId, int limit);

    List<Ticket> getRecentCustomerTickets(int customerId, int limit);

    long countByEmployeeId(int employeeId);

    long countByManagerId(int managerId);

    long countByCustomerCustomerId(int customerId);

    void deleteAllByCustomer(Customer customer);

    List<Ticket> saveAll(List<Ticket> tickets);
}
