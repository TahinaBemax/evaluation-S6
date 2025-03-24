package site.easy.to.build.crm.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.ticket.dto.StatistiqueTicketDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manager/tickets")
public class TicketRestController {
    private final TicketService ticketService;

    @Autowired
    public TicketRestController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> statisqueTicketGlobal(@RequestParam(value = "year",required = false) Integer year)
    {
        List<Map<String, Object>> stat = ticketService.countTicketByYearOrNot(year);
        return ResponseEntity.ok(stat);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<StatistiqueTicketDto>> statisqueTicketByCustomers(
            @RequestParam(value = "customerId",required = false) Integer customerId,
            @RequestParam(value = "start",required = false) LocalDateTime start,
            @RequestParam(value = "end",required = false) LocalDateTime end)
    {
        List<StatistiqueTicketDto> stat = ticketService.statistiqueByCustomerBetweenDate(customerId, start, end);
        return ResponseEntity.ok(stat);
    }
}
