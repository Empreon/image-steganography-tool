package core;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {
    private String filePath = "Log.txt";
    private BufferedWriter writer;
    File logFile = new File(filePath);

    public LogManager() {
        try {
            writer = new BufferedWriter(new FileWriter(logFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createLogFile() {
        try {
            // Check whether the file exists
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLog(String message) {
        try {
            String time = getFormattedDateTime();
            int numberOfSpaces = 60 - message.length();
            StringBuilder spaces = new StringBuilder();

            for (int i = 0; i < numberOfSpaces; i++) {
                spaces.append(" ");
            }

            writer.write(message + spaces.toString() + time + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFormattedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss  dd MMM yyyy");
        return now.format(formatter);
    }

    public void closeLogFile() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
