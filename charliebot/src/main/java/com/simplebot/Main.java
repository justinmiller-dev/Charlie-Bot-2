package com.simplebot;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        //LocalDateTime currTime = LocalDateTime.now();
        //DateTimeFormatter formattedTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String botToken = getBotToken();
        try {
            @SuppressWarnings("resource")
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public static String getBotToken() {
        String botToken = "";
        Path path;
        try {
            String os = System.getProperty("os.name");
            //System.out.println(os);
            if (os.startsWith("Windows")) {
                path = Path.of("windows path here");
                botToken = Files.readString(path);
                //System.out.println(botToken);
            } else if (os.startsWith("Mac")) {
                path = Path.of("/Users/justinmiller/IdeaProjects/Charlie-Bot-2/charliebot/files/bottoken.txt");
                botToken = Files.readString(path);
                //System.out.println(botToken);
            }
        } catch (IOException ex) {
            System.out.println("File not found");
        }
        return botToken;
    }
}
