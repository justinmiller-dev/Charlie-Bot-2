package com.simplebot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final String botToken = System.getenv("apiToken");
    public static void main(String[] args) {
        try {
            @SuppressWarnings("resource")
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public static String getBotToken() {
        return botToken;
    }
}
