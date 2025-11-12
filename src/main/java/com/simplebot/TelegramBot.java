package com.simplebot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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
            System.out.println("Message failed to send");
            System.err.println(e.getMessage());
        }
    }
    public void sendMessage(long chatId, ReplyKeyboard replyKeyboard, String messageText) {
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(messageText)
                .build();
        try {
            message.setReplyMarkup(replyKeyboard);
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Message with keyboard failed to send");
            System.err.println(e.getMessage());
        }
    }
    public int sendMessageAndGetMessageID(long chatId, ReplyKeyboard replyKeyboard, int lastMessageId ,String messageText ) {
        int messageId = 0;
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(messageText)
                .build();
        try {
            message.setReplyMarkup(replyKeyboard);
            messageId = telegramClient.execute(message).getMessageId();
        } catch (TelegramApiException e) {
            System.out.println("sendMessageAndGetMessageID with keyboard failed to send");
            System.err.println(e.getMessage());
        }
        deleteMessage(chatId,lastMessageId);
        return messageId;
    }
    public int sendMessageAndGetMessageID(long chatId,String messageText ) {
        int messageId = 0;
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(messageText)
                .build();
        try {
            messageId = telegramClient.execute(message).getMessageId();
        } catch (TelegramApiException e) {
            System.out.println("sendMessageAndGetMessageID failed to send");
            System.err.println(e.getMessage());
        }
        return messageId;
    }

    public void sendCallBackMessage(AnswerCallbackQuery callBack){
        try {
            telegramClient.execute(callBack);
        } catch (TelegramApiException e){
            System.out.println("Message callback failed to send");
            System.err.println(e.getMessage());
        }
    }
    public void deleteMessage(long chatId, int messageId){
        String input =  Long.toString(chatId);
        DeleteMessage deleteMessage = new DeleteMessage(input,messageId);
        try {
            telegramClient.execute(deleteMessage);
        } catch (TelegramApiException e){
            System.out.println("deleteMessage failed");
            System.err.println(e.getMessage());
        }
    }
    public void editMessage(long chatId, int messageId, InlineKeyboardMarkup keyboard, String message){
        EditMessageText editMessageText = new EditMessageText("");
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(message);
        editMessageText.setReplyMarkup(keyboard);
        try{
            telegramClient.execute(editMessageText);
        } catch (TelegramApiException e){
            System.out.println("editMessage failed");
            System.err.println(e.getMessage());
        }
    }
    public void sendChatAction(String chatId){
        SendChatAction chatAction = new SendChatAction(chatId,"typing");
        try{
            telegramClient.execute(chatAction);
        } catch (TelegramApiException e){
            System.out.println("editMessage failed");
            System.err.println(e.getMessage());
        }
    }
    public String getBotUsername(){
      try{
          GetMe getMe = new GetMe();
          User bot = telegramClient.execute(getMe);
          return bot.getUserName();
      } catch (TelegramApiException e){
          System.out.println("getBotUsername failed");
          System.err.println(e.getMessage());
      }
      return null;
    }
}

    
