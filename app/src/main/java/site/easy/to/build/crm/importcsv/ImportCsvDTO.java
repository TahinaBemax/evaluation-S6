package site.easy.to.build.crm.importcsv;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportCsvDTO {
    @NotNull
    MultipartFile customerFile;

    @NotNull
    MultipartFile ticketFile;

    @NotNull
    MultipartFile budgetFile;

    @NotNull
    @NotEmpty
    String separator;
}
