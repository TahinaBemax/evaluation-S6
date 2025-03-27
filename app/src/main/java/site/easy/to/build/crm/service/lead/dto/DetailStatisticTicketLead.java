package site.easy.to.build.crm.service.lead.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetailStatisticTicketLead {
    Integer leadId;
    String name;
    String email;
    BigDecimal amount;
    Integer expenseId;
}
