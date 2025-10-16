package com.simplebot;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.IntStream;

public class CommandHandler extends TelegramBot {

    private static ArrayList<Charmagotchi> botInstances = new ArrayList<Charmagotchi>();
    private static HashMap<Long, Charmagotchi> botObjects = new HashMap<Long, Charmagotchi>();

    public void commandParse(Update update) {

        long primitiveChatId = update.getMessage().getChatId();
        Long objectChatID = primitiveChatId;
        String user = update.getMessage().getFrom().getFirstName();
        String messageText = update.getMessage().getText();


        if (messageText.contains("/start")) {
           Charmagotchi newBot = addIfNotExists(botInstances, primitiveChatId);
           if (newBot != null){
               botInstances.add(newBot);
               System.out.println("yay");
           } else {
               System.out.println("bot already exists");
           }
        }
        if (messageText.contains("/Begin")){
            Charmagotchi activeBot = botInstances.get(getCurrentInstance(botInstances,primitiveChatId));
            activeBot.startCharmagotchi();
        }
    }

    private Charmagotchi addIfNotExists(ArrayList<Charmagotchi> charm, long newChatId){
        boolean exists = charm.stream().anyMatch(Charmagotchi -> Charmagotchi.getBotChatId() == newChatId);
        System.out.println("list size: " + charm.size());
        System.out.println("Looking for: " + newChatId);
        if (!exists) {
            Charmagotchi newChar = new Charmagotchi(2,50,50,50,newChatId);
            System.out.println("New Bot added");
            System.out.println(newChatId);
            return newChar;
        }else{
            return null;
        }
    }
    private int getCurrentInstance(ArrayList<Charmagotchi> charm, long targetChatId){
        System.out.println(targetChatId);
        for (int i = 0; i < charm.size(); i++){
            if (charm.get(i).getBotChatId() == targetChatId) {
                return i;
            }
        }
        return -1;
    }
}




