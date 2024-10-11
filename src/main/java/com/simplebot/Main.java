package com.simplebot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
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
            if (os.startsWith("Windows")) {
                path = Path.of("windows path here");
                botToken = Files.readString(path);
            } else if (os.startsWith("Mac")) {
                path = Path.of("/Users/justinmiller/IdeaProjects/Charlie-Bot-2/charliebot/files/bottoken.txt");
                botToken = Files.readString(path);
            }
        } catch (IOException ex) {
            System.out.println("File not found");
        }
        return botToken;
    }
}
