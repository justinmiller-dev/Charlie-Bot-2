package com.simplebot;

import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageHandler extends CommandHandler{

    public void messageParse(Update update){

        long chatID = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        if (messageText.startsWith("/")){
            commandParse(update);
        }
    }
}
