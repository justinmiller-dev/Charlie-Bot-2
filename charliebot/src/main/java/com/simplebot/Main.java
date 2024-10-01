package com.simplebot;

import java.time.*;
import java.time.format.DateTimeFormatter;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        
        LocalDateTime currTime = LocalDateTime.now();
        DateTimeFormatter formattedTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        
        try {
            String botToken = "5893606622:AAGzkJk0lRxnKrxMz3_OI9Rjcg6iYGhBSx8";
            @SuppressWarnings("resource")
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }  
    }
 }
    
