package site.easy.to.build.crm.service.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatistiqueTicketDto {
    Integer totalTicket;
    BigDecimal totalExpense;
    String customerName;
    Integer customerId;
}
