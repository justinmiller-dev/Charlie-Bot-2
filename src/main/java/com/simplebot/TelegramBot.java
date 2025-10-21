package com.simplebot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient = new OkHttpTelegramClient(Main.getBotToken());


    @Override
    public void consume(Update message) {
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.messageParse(message);
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
    public void sendInlineKeyboard(long chatId, ReplyKeyboard replyKeyboard, String messageText) {
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(messageText)
                .build();
                message.setReplyMarkup(replyKeyboard);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendCallBackMessage(AnswerCallbackQuery callBack){
        try {
            telegramClient.execute(callBack);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}

    
