package com.simplebot;

import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandHandler extends TelegramBot {

    public void commandParse(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        Charmagotchi charmagotchi = Charmagotchi.getInstance(chatId);
        if (messageText.contains("/startCharmagotchi")) {
            if (charmagotchi.getBotChatId() == chatId && !charmagotchi.isAlive()){
                charmagotchi.setBotChatId(chatId);
                charmagotchi.startCharmagotchi();
            } else {
                sendMessage("Charlie is already running",chatId);
            }

        }
        if (messageText.contains("/playball") && charmagotchi.isAlive()){
            charmagotchi.playBall();
        }
        if (messageText.contains("/stats")&& charmagotchi.isAlive()){
            charmagotchi.showStats();
        }
    }
}


