package com.simplebot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            botObjects.computeIfAbsent(objectChatID, id -> new Charmagotchi(primitiveChatId));
            Charmagotchi activeBot = botObjects.get(objectChatID);
            if (activeBot.isAlive()){
                sendMessage("*Bark Bark*",activeBot.getBotChatId());
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
            if (activeBot.isAsleep()){
                sendMessage("This is a message",activeBot.getBotChatId());
            } else {
                activeBot.playBall();
            }
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
            activeBot.sendToBed(8);
        }
        if (messageText.contains("/walk")){
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.goForWalk();
        }
    }
}




