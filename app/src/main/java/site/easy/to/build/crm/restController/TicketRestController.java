package site.easy.to.build.crm.restController;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.ticket.dto.StatistiqueTicketDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crm/customers")
public class TicketRestController {
    private final TicketService ticketService;
    private final LeadService leadService;

    @Autowired
    public TicketRestController(TicketService ticketService, LeadService leadService) {
        this.ticketService = ticketService;
        this.leadService = leadService;
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

    @DeleteMapping("/TicketLead/{id}")
    public ResponseEntity<?> ticketsByCustomers(@PathVariable(value = "id") Integer id, @RequestParam("isTicket") int isTicket)
    {
        try{
            if (id == null){
                return (ResponseEntity<?>) ResponseEntity.badRequest();
            }

            if (isTicket == 1){
                Ticket t = ticketService.findByTicketId(id);
                if (t == null) {
                    return new ResponseEntity<>(new ApiResponse<>(500, "Internal server error", true), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                ticketService.delete(t);

            } else {
                Lead byLeadId = leadService.findByLeadId(id);
                if (byLeadId == null){
                    return new ResponseEntity<>(new ApiResponse<>(500, "Internal server error", true), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                leadService.delete(byLeadId);
            }
        } catch (Exception e){
            return new ResponseEntity<>(new ApiResponse<>(500, "Internal server error", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return ResponseEntity.ok(new ApiResponse<>(200, "Deleting successfuly", true));
    }
}
