package com.simplebot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class Main {
    private static String botToken;
    public static void main(String[] args) {
        Properties prop = new Properties();
        try{
            FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\.properties");
            prop.load(fileInputStream);
            botToken = prop.getProperty("api.token");
            fileInputStream.close();
            DataHandler.initializeBotMap(DataHandler.connectToDatabase());
        }catch (IOException e){
            e.printStackTrace();
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
}
