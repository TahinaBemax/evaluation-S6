package site.easy.to.build.crm.importcsv.model;


import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import site.easy.to.build.crm.importcsv.annotation.ValidEmail;

import java.time.LocalDateTime;


@Data
public class CustomerCsv {
    @CsvBindByName(column = "customer_name")
    @NotBlank
    String name;

    @NotBlank
    String country = "Madagascar";

    @NotNull
    Integer user = 52; // mettre l'user = 52

    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
    LocalDateTime createdAt = LocalDateTime.now();

    @CsvBindByName(column = "customer_email")
    @ValidEmail
    String email;
}
