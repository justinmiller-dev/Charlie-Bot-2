package com.simplebot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.sql.*;
import java.util.Properties;


public class Main {
    private static String botToken;
    private static String url;
    private static String username;
    private static String password;
    public static void main(String[] args) {
        Properties prop = new Properties();
        InputStream input;

        try{
             input = Main.class.getResourceAsStream("/.properties");
             prop.load(input);
             botToken = prop.getProperty("devApi.token");
             username = prop.getProperty("db.username");
             password = prop.getProperty("db.password");
             url = prop.getProperty("db.devurl");
             DataHandler.initializeBotMap(DataHandler.connectToDatabase());
        } catch (IOException e) {
            System.err.println("Error reading resource: " + e.getMessage());
        }

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
    public static String getDBUsername() {
        return username;
    }
    public static String getDbPassword() {
        return password;
    }
    public static String getDbURL() {
        return url;
    }
}
