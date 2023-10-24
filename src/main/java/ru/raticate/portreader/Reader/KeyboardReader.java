package ru.raticate.portreader.Reader;

import jakarta.annotation.Nullable;
import ru.raticate.portreader.Loggers.Logger;

import java.util.Scanner;

public class KeyboardReader extends Reader {
    Scanner bufferedReader;
    public KeyboardReader(Logger logger, String exitBarcode) {
        super(logger,exitBarcode);
        bufferedReader = new Scanner(System.in);
    }
    @Nullable
    public String getValue() {
        try {
            String value = bufferedReader.nextLine();
            return value.length() == 9 || value.equals(exitBarcode) ? value : null;
        } catch (Exception e) {
            return null;
        }
    }
}
