package site.easy.to.build.crm.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("dates")
public class DateUtil {

    public static String convertDateFormat(LocalDate inputDate) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return inputDate.format(outputFormatter);
    }

    public String convertDateTimeFormat(LocalDateTime inputDateTime) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return inputDateTime.format(outputFormatter);
    }
}
