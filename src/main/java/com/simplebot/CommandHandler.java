package com.simplebot;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Set;

public class CommandHandler extends TelegramBot {

    public void commandParse(Update update) {

        long chatId = update.getMessage().getChatId();
        String user = update.getMessage().getFrom().getFirstName();
        String messageText = update.getMessage().getText();
        ArrayList<Charmagotchi> botInstances = new ArrayList<Charmagotchi>();




        if (messageText.contains("/start")) {
           addIfNotExists(botInstances,chatId);
        }else{
            System.out.println("Didn't Work");
        }





    }

    public void addIfNotExists(ArrayList<Charmagotchi> item, long newChatId){
        boolean exists = item.stream().anyMatch(Charmagotchi -> Charmagotchi.getBotChatId() == newChatId);
        if (!exists) {
            item.add(new Charmagotchi(1, 50, 50, 50, newChatId));
            System.out.println("New Bot added");
        }else{
            System.out.println("Bot already exists");
        }

    }

}




