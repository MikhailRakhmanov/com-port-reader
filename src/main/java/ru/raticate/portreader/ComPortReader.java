package ru.raticate.portreader;

import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ComPortReader {
    public static void main(String[] args) throws IOException {
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            System.out.println("No COM ports available.");
            return;
        }

        int i = 0;
        for (SerialPort serialPort : ports) {
            System.out.println(Integer.toString(i++) + ") " + serialPort);
        }

        SerialPort port = ports[1]; // Choose the port you want to use
        port.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100000, 0);

        while (port.openPort()) {
            InputStream inputStream = port.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            System.out.println(bufferedReader.readLine());

        }
        port.closePort();
        System.out.println("Port closed.");
    }
}