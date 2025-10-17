package com.simplebot;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.IntStream;

public class CommandHandler extends TelegramBot {

    private static HashMap<Long, Charmagotchi> botObjects = new HashMap<Long, Charmagotchi>();

    public void commandParse(Update update) {

        long primitiveChatId = update.getMessage().getChatId();
        Long objectChatID = primitiveChatId;
        String user = update.getMessage().getFrom().getFirstName();
        String messageText = update.getMessage().getText();

        if (messageText.contains("/startCharmagotchi")){
            addToHashmap(botObjects,objectChatID);
            Charmagotchi activeBot = botObjects.get(objectChatID);
            if (activeBot.isAlive()){
                System.out.println("bot already running");
            } else {
                activeBot.startCharmagotchi();
            }
        }
        if (messageText.contains("/killCharlie")){
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.killCharlie();
            botObjects.remove(objectChatID);
        }
        if (messageText.contains("/playball")){
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.playBall();
        }
        if (messageText.contains("/stats")){
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.showStats();
        }
        if (messageText.contains("/meal")){
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.feedMeal();
        }
        if (messageText.contains("/treat")){
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.giveTreat();
        }
        if (messageText.contains("/speak")){
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.speak();
        }
        if (messageText.contains("/sleep")){
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.sendToBed();
        }
        if (messageText.contains("/walk")){
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.goForWalk();
        }
    }
    private Charmagotchi addToHashmap(HashMap<Long, Charmagotchi> bot, Long chatID){
        bot.computeIfAbsent(chatID, id -> new Charmagotchi(chatID));
        return bot.get(chatID);
    }
}




