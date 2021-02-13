package com.techelevator.util;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VmLog {
    public static void log(String message) {
        try (FileOutputStream stream = new FileOutputStream("log.txt", true);
             PrintWriter writer = new PrintWriter(stream)) {
            LocalDateTime timeStamp = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/y hh:mm:ss a");

            writer.println(timeStamp.format(formatter) + ":" + message);

        } catch (Exception e) {
            throw new VmLogException(e.getMessage());
        }
    }

}
