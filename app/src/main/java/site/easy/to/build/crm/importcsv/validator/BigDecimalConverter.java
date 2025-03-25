package site.easy.to.build.crm.importcsv.validator;

import com.opencsv.bean.AbstractBeanField;
import java.math.BigDecimal;

public class BigDecimalConverter extends AbstractBeanField<BigDecimal, String> {
    @Override
    protected BigDecimal convert(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO; // Valeur par défaut si vide
        }

        // Utiliser un regex pour valider la chaîne (chiffres uniquement avec option pour décimales)
        if (!value.matches("^\\d+(\\.\\d+)?$")) {
            throw new IllegalArgumentException("Invalid number format: " + value);
        }

        // Remplace la virgule par un point pour gérer les nombres au format "1,23"
        return new BigDecimal(value.replace(",", "."));
    }
}

