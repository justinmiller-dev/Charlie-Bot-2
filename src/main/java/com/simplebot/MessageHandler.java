package com.simplebot;

import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageHandler extends CommandHandler{
    String messageText;
    long chatID;

    public void messageParse(Update update){

        chatID = update.getMessage().getChatId();
        messageText = update.getMessage().getText();

        if (messageText.startsWith("/")){
            commandParse(update);
        }
    }
}
