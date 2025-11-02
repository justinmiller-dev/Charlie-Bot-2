package com.simplebot;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.*;
import java.util.HashMap;





public class CommandHandler extends TelegramBot {

    private static HashMap<Long, Charmagotchi> botObjects = DataHandler.getBots();
    private static long lastActionTime = 0;
    private static final long coolDown = 6000;

    public void commandParse(Update update) {
        long currentTime = System.currentTimeMillis();
        if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            Charmagotchi activeBot = botObjects.get(update.getCallbackQuery().getMessage().getChatId());
            String callBackData = callbackQuery.getData();
            String userFirstName = callbackQuery.getFrom().getFirstName();
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(callbackQuery.getId());

                if (callBackData.equals("wake") && activeBot.isAsleep()){
                    activeBot.wake();
                }
                if(currentTime > lastActionTime + coolDown) {
                    if(!activeBot.isAsleep() && activeBot.isAlive()){
                        if (callBackData.equals("playball")) {
                        activeBot.playBall(userFirstName);
                        lastActionTime = currentTime;
                        }
                        if (callBackData.equals("treat")) {
                        activeBot.giveTreat();
                        lastActionTime = currentTime;
                        }
                        if (callBackData.equals("feed")) {
                        activeBot.feedMeal(userFirstName);
                        lastActionTime = currentTime;
                        }
                        if (callBackData.equals("walk")) {
                        activeBot.goForWalk(userFirstName);
                        lastActionTime = currentTime;
                        }
                        if (callBackData.equals("bed")) {
                        activeBot.sendToBed();
                        lastActionTime = currentTime;
                        }
                        if (callBackData.equals("speak")) {
                        activeBot.speak();
                        lastActionTime = currentTime;
                        }
                    } else if (activeBot.isAsleep() && activeBot.isAlive()){
                    activeBot.getSleepMessage();
                    lastActionTime = currentTime;
                    }
                    sendCallBackMessage(answerCallbackQuery);
                } else {
                    long secondsLeft = (lastActionTime - currentTime + coolDown)/1000;
                    String remainingSeconds = String.valueOf(secondsLeft);
                    answerCallbackQuery.setText("Cooldown "+ remainingSeconds +" seconds.");
                    sendCallBackMessage(answerCallbackQuery);
                }
        }
        long primitiveChatId;
        Long objectChatID = 0L;
        int messageId = 0;
        String messageText = "";
        String userFistName = "";

        if (update.getMessage() != null){
            primitiveChatId = update.getMessage().getChatId();
            objectChatID = primitiveChatId;
            messageId = update.getMessage().getMessageId();
            userFistName = update.getMessage().getFrom().getFirstName();
            if (update.hasMessage() && update.getMessage().hasText()) {
                messageText = update.getMessage().getText();
            }
        } else {
            primitiveChatId = 0;
        }
        if (messageText.contains("/start")) {
            botObjects.computeIfAbsent(objectChatID, id -> new Charmagotchi(primitiveChatId));
            Charmagotchi activeBot = botObjects.get(objectChatID);
            if (activeBot.isAlive()) {
                sendMessage("CharlieBot is already running use the command /charlie to call him.", activeBot.getBotChatId());
            } else {
                activeBot.startCharmagotchi();
                activeBot.getStartMessage();
                DataHandler.insertNewBot(DataHandler.connectToDatabase(),activeBot);
            }
        }
        if (messageText.contains("/killcharlie")) {
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.killCharlie(userFistName);
            botObjects.remove(objectChatID);
        }
        if (messageText.contains("/charlie")) {
            Charmagotchi activeBot = botObjects.get(objectChatID);
            if (!activeBot.isAsleep() && activeBot.isAlive()){
                activeBot.getActionMessage();
            }else if(activeBot.isAsleep() && activeBot.isAlive()){
                activeBot.getSleepMessage();
            }
            lastActionTime = currentTime;
        }
    }
}







