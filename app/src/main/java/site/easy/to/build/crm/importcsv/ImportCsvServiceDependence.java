package site.easy.to.build.crm.importcsv;

import jakarta.validation.Validator;
import lombok.Data;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.importcsv.service.CustomerDataPreparator;

@Service
@Data
public class ImportCsvServiceDependence {
    private final Validator validator;
    private final CustomerDataPreparator customerDataPreparator;
}
