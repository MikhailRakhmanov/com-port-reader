package ru.raticate.portreader;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateConvertor {
    private static DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//    String formattedDateTime = dateTime.format(formatter); // "1986-04-08 12:30"
    public double dateToDouble(LocalDateTime now) {
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0, 0, 0);
        Duration duration = Duration.between(then, now.plusDays(2));
        return (double) duration.toSeconds() / 86400;
    }

    public LocalDateTime doubleToDate(double date) {
        Duration duration = Duration.ofSeconds((long) (date*86400));
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0, 0, 0);
        LocalDateTime now = then.plus(duration).minusDays(2);
        return now;
    }
    public String doubleToStr(double date) {
        Duration duration = Duration.ofSeconds((long) (date*86400));
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0, 0, 0);
        LocalDateTime now = then.plus(duration).minusDays(2);
        return now.format(formatter);
    }

}
