package com.simplebot;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
    private TelegramClient telegramClient = new OkHttpTelegramClient("Bot key here");
    Charmagotchi charmagotchi = new Charmagotchi(0, 50, 50, 50);
    
    String messageText;
    long chatId;
    
    @Override
    public void consume(Update message) {
        
        if (message.hasMessage() && message.getMessage().hasText()){
            messageText = getMessageText(message).toLowerCase();
            chatId = getChatId(message);
            System.out.println(messageText);


            if (messageText.equals("/meal@charlie_the_dog_bot") && charmagotchi.getHunger() > 0){
               charmagotchi.updateHunger(-50);
                sendMessage("OM NOM NOM!", chatId);
            }else if (messageText.equals("/treat@charlie_the_dog_bot") && charmagotchi.getHunger() > 0 ){
                sendMessage("*Happy Munches*", chatId);
                charmagotchi.updateHunger(-10);
            } else if (charmagotchi.getHunger() <= 0 && messageText.equals("/treat@charlie_the_dog_bot")|| messageText.equals("/meal@Charlie_the_dog_bot")) {
                sendMessage("Charlie is Full!", chatId);
            }



            if (messageText.equals("/stats@charlie_the_dog_bot")){
                int[] statsArray = charmagotchi.getStatsArray();
                messageText = "Charlie's current stats are. " +"\n"
                + "Hunger: " + statsArray[0] +"%"+ "\n"
                + "Happiness: " + statsArray[1] +"%"+ "\n"    
                + "Sleepiness: " + statsArray[2] +"%"+ "\n"
                + "Fitness: " + statsArray[3] +"%"+ "\n";
                sendMessage(messageText, chatId);
            }



            if (messageText.equals("/playball@charlie_the_dog_bot")){
                charmagotchi.updateHappiness(4);
                charmagotchi.changeSleepiness(-7);
                charmagotchi.updateExercise(1);
                Random rand = new Random();
                int randInt = rand.nextInt(2);
                switch (randInt) {
                    case 0:
                        sendMessage("You throw the ball. Charlie catches it! but he won't give it back.", chatId);
                        break;
                    case 1:
                        sendMessage("You throw the ball. Charlie misses the ball and runs after it.", chatId);
                        break;
                    case 2:
                        sendMessage("You throw the ball. It lands behind the couch.", chatId);
                        break;
                    default:
                        break;
                }
            }
        }
    }  
        
    
    
  





    public long getChatId(Update message){
        long chatId = message.getMessage().getChatId();
        return chatId;
    }
  
  
    public String getMessageText(Update message) {
        String messageText = message.getMessage().getText();
        return messageText;
    }


    public void sendMessage(String messageText, long chatId) {
        SendMessage message = SendMessage
        .builder()
        .chatId(chatId)
        .text(messageText)
        .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

    
