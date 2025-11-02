package com.simplebot;

import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageHandler extends CommandHandler{

    public void messageParse(Update update){
       if (update.hasCallbackQuery()) {
          commandParse(update);
       } else if (update.getMessage().getText().startsWith("/")){
           commandParse(update);
       }
    }
}
