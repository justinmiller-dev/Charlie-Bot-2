package com.simplebot;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.HashMap;


public class CommandHandler extends TelegramBot {

    private static HashMap<Long, Charmagotchi> botObjects = new HashMap<>();
    private static long lastActionTime = 0;
    private static final long coolDown = 5000;
    public void commandParse(Update update) {

        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime);
        System.out.println(lastActionTime + coolDown);
        if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            Charmagotchi activeBot = botObjects.get(update.getCallbackQuery().getMessage().getChatId());
            String callBackData = callbackQuery.getData();
            String chatIdString = Long.toString(activeBot.getBotChatId());
            int messageId = callbackQuery.getMessage().getMessageId();
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(callbackQuery.getId());

            if(currentTime > lastActionTime + coolDown){
                if (callBackData.equals("wake")&& activeBot.isAsleep()){
                    activeBot.wake();
                }
                if(!activeBot.isAsleep()) {
                    if (callBackData.equals("playball")) {
                        deleteMessage(chatIdString, messageId);
                        activeBot.playBall();
                        lastActionTime = currentTime;
                    }
                    if (callBackData.equals("treat")) {
                        deleteMessage(chatIdString, messageId);
                        activeBot.giveTreat();
                        lastActionTime = currentTime;
                    }
                    if (callBackData.equals("feed")) {
                        deleteMessage(chatIdString, messageId);
                        activeBot.feedMeal();
                        lastActionTime = currentTime;
                    }
                    if (callBackData.equals("walk")) {
                        deleteMessage(chatIdString, messageId);
                        activeBot.goForWalk();
                        lastActionTime = currentTime;
                    }
                    if (callBackData.equals("bed")) {
                        deleteMessage(chatIdString, messageId);
                        activeBot.sendToBed();
                        ReplyKeyboard  keyboard = activeBot.actionButtonMaker(activeBot.getBotChatId(), "Wake Charlie?","wake");
                        activeBot.getSleepMessage(keyboard);
                        lastActionTime = currentTime;
                    }
                    if (callBackData.equals("speak")) {
                        deleteMessage(chatIdString, messageId);
                        activeBot.speak();
                        lastActionTime = currentTime;
                    }
                } else {
                    ReplyKeyboard  keyboard = activeBot.actionButtonMaker(activeBot.getBotChatId(), "Wake Charlie?","wake");
                    activeBot.getSleepMessage(keyboard);
                    lastActionTime = currentTime;
                }
            }
            answerCallbackQuery.setText("*Woof!*");
        }

        long primitiveChatId = update.getMessage().getChatId();
        Long objectChatID = primitiveChatId;
        String messageText = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            messageText = update.getMessage().getText();
        }

        if (messageText.contains("/start")) {
            botObjects.computeIfAbsent(objectChatID, id -> new Charmagotchi(primitiveChatId));
            Charmagotchi activeBot = botObjects.get(objectChatID);
            if (activeBot.isAlive()) {
                sendMessage("CharlieBot is already running use the command /charlie to call him.", activeBot.getBotChatId());
            } else {
                activeBot.startCharmagotchi();
                activeBot.getStartMessage();
            }
        }
        if (messageText.contains("/killCharlie")) {
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.killCharlie();
            botObjects.remove(objectChatID);
        }

        if (messageText.contains("/charlie")&& currentTime > lastActionTime + coolDown) {
            Charmagotchi activeBot = botObjects.get(objectChatID);
            if (activeBot.isAsleep()){
                ReplyKeyboard  keyboard = activeBot.actionButtonMaker(activeBot.getBotChatId(), "Wake Charlie?","wake");
                activeBot.getSleepMessage(keyboard);
                lastActionTime = currentTime;
            }else{
                activeBot.getActionMessage();
                lastActionTime = currentTime;
            }
        }
    }
}




