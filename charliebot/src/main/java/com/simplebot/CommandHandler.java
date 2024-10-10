package com.simplebot;

import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandHandler extends TelegramBot {

    public void commandParse(Update update) {
        long chatId = update.getMessage().getChatId();
        String user = update.getMessage().getFrom().getFirstName();
        String messageText = update.getMessage().getText();
        Charmagotchi charmagotchi = Charmagotchi.getInstance(chatId);
        if (messageText.contains("/startCharmagotchi")&& user.equals("Justin")) {
            if (!charmagotchi.isAlive()){
                charmagotchi.setBotChatId(chatId);
                charmagotchi.startCharmagotchi();
                sendMessage("Charmagotchi has been started",chatId);
            } else if (charmagotchi.isAlive()) {
                sendMessage("Charlie is already running", chatId);
            }
        } else if (messageText.contains("/startCharmagotchi")){
            sendMessage("Silly " + user + " you're not Justin",chatId);
        }
        if (messageText.contains("/playball") && charmagotchi.isAlive()){
            charmagotchi.playBall();
        }
        if (messageText.contains("/stats")&& charmagotchi.isAlive()){
            charmagotchi.showStats();
        }
        if (messageText.contains("/meal")&& charmagotchi.isAlive()){
            charmagotchi.feedMeal();
        }
        if (messageText.contains("/treat")&& charmagotchi.isAlive()){
            charmagotchi.giveTreat();
        }
        if (messageText.contains("/speak")&& charmagotchi.isAlive()){
            charmagotchi.speak();
        }
        if (messageText.contains("/sleep")&& charmagotchi.isAlive()){
            charmagotchi.sendToBed();
        }
        if (messageText.contains("/walk")&& charmagotchi.isAlive()){
            charmagotchi.goForWalk();
        }
        if (messageText.contains("/killCharlie")&& charmagotchi.isAlive()){
            charmagotchi.killCharlie();
        }

    }
}


