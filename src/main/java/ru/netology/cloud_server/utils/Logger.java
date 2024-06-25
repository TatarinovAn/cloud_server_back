package ru.netology.cloud_server.utils;

import lombok.NoArgsConstructor;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class Logger {
    private static Logger logger;
    private String dateTime = DateTimeFormatter.ofPattern("dd.mm.yyyy hh:mm:ss")
            .format(LocalDateTime.now());


    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public String log(String msg) {
        return "[" + dateTime + "] " + msg;
    }

    public void writeLog(String message) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(log(message));
            writer.append('\n');
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
