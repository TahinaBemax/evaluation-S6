package site.easy.to.build.crm.importcsv.model;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvNumber;
import jakarta.validation.constraints.*;
import lombok.Data;
import site.easy.to.build.crm.importcsv.annotation.ValidEmail;
import site.easy.to.build.crm.importcsv.annotation.ValidNumber;
import site.easy.to.build.crm.importcsv.validator.BigDecimalConverter;

import java.math.BigDecimal;


@Data
public class BudgetCsv {
    @CsvBindByName(column = "customer_email")
    @ValidEmail
    String customerEmail;

    @CsvCustomBindByName(column = "budget", converter = BigDecimalConverter.class)
    @ValidNumber
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid number with up to 2 decimal places")
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be greater than or equal to 0.00")
    @DecimalMax(value = "9999999.99", inclusive = true, message = "Amount must be less than or equal to 9999999.99")
    private BigDecimal budget;
}
