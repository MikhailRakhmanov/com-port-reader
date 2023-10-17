package ru.raticate.portreader.Loggers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Logger {

    public List<String> data;
    private final FileWriter fileWriter;

    public Logger() {
        data = new ArrayList<>();
        File file = new File("history.txt");
        if (!file.exists()) try {
            assert true :
                    file.createNewFile();
        } catch (IOException e) {
            System.err.println("Проблема создания файла истории");
        }
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void log(String str, LoggerLevel... loggerLevels) {
        if (Arrays.asList(loggerLevels).contains(LoggerLevel.Browser))
            data.add(str);
        if (Arrays.asList(loggerLevels).contains(LoggerLevel.File))
            try {
                fileWriter.write(str + '\n');
                fileWriter.flush();
            } catch (IOException e) {
                System.err.println("Проблема записи в файл истории");
            }
        if (Arrays.asList(loggerLevels).contains(LoggerLevel.Console))
            System.out.println(str);
    }
}