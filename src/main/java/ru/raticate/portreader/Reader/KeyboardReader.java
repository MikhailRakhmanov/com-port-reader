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
            if(value.equalsIgnoreCase(exitBarcode)){
                return null;
            }
            return value;
        } catch (Exception e) {
            return null;
        }
    }
}
