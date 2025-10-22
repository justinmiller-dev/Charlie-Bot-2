package com.simplebot;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandHandler extends TelegramBot {

    private static HashMap<Long, Charmagotchi> botObjects = new HashMap<>();

    public void commandParse(Update update) {

        if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            Charmagotchi activeBot = botObjects.get(update.getCallbackQuery().getMessage().getChatId());
            String callBackData = callbackQuery.getData();
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(callbackQuery.getId());
            int messageId = callbackQuery.getMessage().getMessageId();
            String chatIdString = Long.toString(activeBot.getBotChatId());

            if (callBackData.equals("playball")) {
                activeBot.playBall();
                deleteMessage(chatIdString, messageId);
                InlineKeyboardMarkup keyboardMarkup = inLineButtons(activeBot.getBotChatId());
                sendInlineKeyboard(activeBot.getBotChatId(), keyboardMarkup, activeBot.getActionMessage());
            }
            if (callBackData.equals("treat")) {
                activeBot.giveTreat();
                deleteMessage(chatIdString, messageId);
                InlineKeyboardMarkup keyboardMarkup = inLineButtons(activeBot.getBotChatId());
                sendInlineKeyboard(activeBot.getBotChatId(), keyboardMarkup, activeBot.getActionMessage());
            }
            if (callBackData.equals("feed")) {
                activeBot.feedMeal();
                deleteMessage(chatIdString, messageId);
                InlineKeyboardMarkup keyboardMarkup = inLineButtons(activeBot.getBotChatId());
                sendInlineKeyboard(activeBot.getBotChatId(), keyboardMarkup, activeBot.getActionMessage());
            }
            if (callBackData.equals("walk")) {
                activeBot.goForWalk();
                deleteMessage(chatIdString, messageId);
                InlineKeyboardMarkup keyboardMarkup = inLineButtons(activeBot.getBotChatId());
                sendInlineKeyboard(activeBot.getBotChatId(), keyboardMarkup, activeBot.getActionMessage());
            }
            if (callBackData.equals("bed")) {
                activeBot.sendToBed();
                deleteMessage(chatIdString, messageId);
                InlineKeyboardMarkup keyboardMarkup = inLineButtons(activeBot.getBotChatId());
                sendInlineKeyboard(activeBot.getBotChatId(), keyboardMarkup, activeBot.getActionMessage());
            }
            if (callBackData.equals("speak")) {
                activeBot.speak();
                deleteMessage(chatIdString, messageId);
                InlineKeyboardMarkup keyboardMarkup = inLineButtons(activeBot.getBotChatId());
                sendInlineKeyboard(activeBot.getBotChatId(), keyboardMarkup, activeBot.getActionMessage());
            }
            answerCallbackQuery.setText("Action Passed");
            sendCallBackMessage(answerCallbackQuery);
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
                sendMessage("*Bark Bark*", activeBot.getBotChatId());
                InlineKeyboardMarkup keyboardMarkup = inLineButtons(activeBot.getBotChatId());
                sendInlineKeyboard(activeBot.getBotChatId(), keyboardMarkup, activeBot.getStatsMessage());
            } else {
                activeBot.startCharmagotchi();
                InlineKeyboardMarkup keyboardMarkup = inLineButtons(activeBot.getBotChatId());
                sendInlineKeyboard(activeBot.getBotChatId(), keyboardMarkup, activeBot.getStatsMessage());
            }
        }
        if (messageText.contains("/killCharlie")) {
            Charmagotchi activeBot = botObjects.get(objectChatID);
            activeBot.killCharlie();
            botObjects.remove(objectChatID);
        }

        if (messageText.contains("/charlie")) {
            Charmagotchi activeBot = botObjects.get(objectChatID);
            InlineKeyboardMarkup keyboardMarkup = inLineButtons(activeBot.getBotChatId());
            sendInlineKeyboard(activeBot.getBotChatId(), keyboardMarkup, activeBot.getActionMessage());

        }
    }
    public InlineKeyboardMarkup inLineButtons(long objectChatID){
        List<InlineKeyboardButton> button = new ArrayList<>();
        InlineKeyboardButton buttonOne= new InlineKeyboardButton("buttonOne");
        InlineKeyboardButton buttonTwo= new InlineKeyboardButton("buttonTwo");
        InlineKeyboardButton buttonThree= new InlineKeyboardButton("buttonThree");
        InlineKeyboardButton buttonFour= new InlineKeyboardButton("buttonFour");
        InlineKeyboardButton buttonFive= new InlineKeyboardButton("buttonFive");
        InlineKeyboardButton buttonSix= new InlineKeyboardButton("buttonSix");
        buttonOne.setText("Play ball");
        buttonOne.setCallbackData("playball");
        buttonTwo.setText("Give a treat");
        buttonTwo.setCallbackData("treat");
        buttonThree.setText("Feed");
        buttonThree.setCallbackData("feed");
        buttonFour.setText("Go for a walk");
        buttonFour.setCallbackData("walk");
        buttonFive.setText("Send to bed");
        buttonFive.setCallbackData("bed");
        buttonSix.setText("Speak");
        buttonSix.setCallbackData("speak");
        button.add(buttonOne);
        button.add(buttonTwo);
        button.add(buttonThree);
        button.add(buttonFour);
        button.add(buttonFive);
        button.add(buttonSix);

        List<InlineKeyboardRow> KeyboardRows = new ArrayList<>();
        InlineKeyboardRow row1 = new InlineKeyboardRow(buttonOne);
        InlineKeyboardRow row2 = new InlineKeyboardRow(buttonTwo);
        InlineKeyboardRow row3 = new InlineKeyboardRow(buttonThree);
        InlineKeyboardRow row4 = new InlineKeyboardRow(buttonFour);
        InlineKeyboardRow row5 = new InlineKeyboardRow(buttonFive);
        InlineKeyboardRow row6 = new InlineKeyboardRow(buttonSix);

        KeyboardRows.add(row1);
        KeyboardRows.add(row2);
        KeyboardRows.add(row3);
        KeyboardRows.add(row4);
        KeyboardRows.add(row5);
        KeyboardRows.add(row6);

        return new InlineKeyboardMarkup(KeyboardRows);
    }
}




