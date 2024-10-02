package com.simplebot;

import java.io.IOException;
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
        LocalDateTime currTime = LocalDateTime.now();
        DateTimeFormatter formattedTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String botToken = getBotToken();
        
        try {
            @SuppressWarnings("resource")
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }  
    }
    public static String getBotToken(){
    String botToken = "";
    Path path = Paths.get("/Users/justinmiller/Documents/GItHub/Charlie-Bot-2/charliebot/src/main/java/com/simplebot/bottoken.txt");
    try {
            String botKey = Files.readString(path);
            System.out.println(botKey);
            botToken = botKey;
        } catch (IOException ex) {
            System.out.println("File not found");
        }
        return botToken;
    }
}

