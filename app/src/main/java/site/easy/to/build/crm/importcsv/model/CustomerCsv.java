package site.easy.to.build.crm.importcsv.model;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.Email;

import java.time.LocalDateTime;


@Data
public class CustomerCsv {
    @CsvBindByName
    @NotNull
    String name;

    @CsvBindByName
    @NotBlank
    String country;

    @CsvBindByName(column = "user_id")
    @NotNull
    Integer user; // mettre l'user = 52

    @CsvBindByName(column = "created_at")
    @CsvDate("dd-MM-yyy HH:mm")
    LocalDateTime createdAt;

    @CsvBindByName(column = "email")
    @Email
    String email;
}
