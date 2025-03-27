package site.easy.to.build.crm.service.lead.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticTicketLead {
    int month;
    Integer total;
    BigDecimal totalAmount;
}
