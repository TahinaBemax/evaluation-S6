package site.easy.to.build.crm.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.ticket.dto.StatistiqueTicketDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crm/customers")
public class TicketRestController {
    private final TicketService ticketService;

    @Autowired
    public TicketRestController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets/statistic-global")
    public ResponseEntity<List<Map<String, Object>>> statisqueTicketGlobal(@RequestParam(value = "year",required = false) Integer year)
    {
        List<Map<String, Object>> stat = ticketService.countTicketByYearOrNot(year);
        return ResponseEntity.ok(stat);
    }

    @GetMapping("/tickets/statistic")
    public ResponseEntity<List<StatistiqueTicketDto>> statisqueTicketByCustomers(
            @RequestParam(value = "customerId",required = false) Integer customerId,
            @RequestParam(value = "startDate",required = false) LocalDateTime start,
            @RequestParam(value = "endDate",required = false) LocalDateTime end)
    {
        List<StatistiqueTicketDto> stat = ticketService.statistiqueByCustomerBetweenDate(customerId, start, end);
        return ResponseEntity.ok(stat);
    }

    @GetMapping("/{customerId}/tickets")
    public ResponseEntity<List<Ticket>> ticketsByCustomers(
            @PathVariable(value = "customerId") Integer customerId,
            @RequestParam(value = "startDate",required = false) LocalDateTime start,
            @RequestParam(value = "endDate",required = false) LocalDateTime end)
    {
        List<Ticket> stat = ticketService.findCustoerTicketsBetweenDate(customerId, start, end);
        return ResponseEntity.ok(stat);
    }

    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<?> ticketsByCustomers( @PathVariable(value = "id") Integer id)
    {
        if (id == null){
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
        Ticket t = ticketService.findByTicketId(id);

        if (t == null) {
            return new ResponseEntity<>("Ticket not found",HttpStatus.NOT_FOUND);
        }

        ticketService.delete(t);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
