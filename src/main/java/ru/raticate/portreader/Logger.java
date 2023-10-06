package ru.raticate.portreader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private final FileWriter fileWriter;

    public Logger() {
        File file = new File("history.txt");
        if (!file.exists()) {
            try {
                assert true :
                        file.createNewFile();
            } catch (IOException e) {
                System.err.println("Проблема создания файла истории");
            }
        }
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void log(String str) {
        try {
            fileWriter.write(str + '\n');
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("Проблема записи в файл истории");
        }
    }
}
