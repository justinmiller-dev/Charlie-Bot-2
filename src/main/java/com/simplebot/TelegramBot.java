package com.simplebot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient = new OkHttpTelegramClient(Main.getBotToken());

    String messageText;
    long chatId;

    @Override
    public void consume(Update message) {

        if (message.hasMessage() && message.getMessage().hasText()) {
            MessageHandler messageHandler = new MessageHandler();
            messageText = getMessageText(message).toLowerCase();
            chatId = getChatId(message);
            System.out.println(messageText);
            messageHandler.messageParse(message);
        }
    }

    public long getChatId(Update message) {
        return message.getMessage().getChatId();
    }


    public String getMessageText(Update message) {
        return message.getMessage().getText();
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

    
