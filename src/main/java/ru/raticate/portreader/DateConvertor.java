package ru.raticate.portreader;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateConvertor {
    private static DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public double dateToDouble(LocalDateTime now) {
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0, 0, 0);
        Duration duration = Duration.between(then, now.plusDays(2));
        return (double) duration.toSeconds() / 86400;
    }
    public LocalDateTime doubleToDate(double date) {
        Duration duration = Duration.ofSeconds((long) (date*86400));
        LocalDateTime then = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0, 0, 0);
        return then.plus(duration).minusDays(2);
    }
    public String doubleToStr(double date) {
        return doubleToDate(date).format(formatter);
    }
}
