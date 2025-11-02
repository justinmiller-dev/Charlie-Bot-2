package com.simplebot;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;


public class CommandHandler extends TelegramBot {

    private static HashMap<Long, Charmagotchi> botObjects = new HashMap<>();
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
            try {
              Charmagotchi activeBot = selectExistingBot(connectToDatabase(),primitiveChatId);
              if (activeBot == null){
                  botObjects.computeIfAbsent(objectChatID, id -> new Charmagotchi(primitiveChatId));
                  activeBot = botObjects.get(objectChatID);
                  insertNewBot(connectToDatabase(),activeBot);
              } else {
                  botObjects.putIfAbsent(objectChatID,activeBot);
              }
              if (activeBot.isAlive()) {
                    sendMessage("CharlieBot is already running use the command /charlie to call him.", activeBot.getBotChatId());
              } else {
                  activeBot.startCharmagotchi();
                  activeBot.getStartMessage();
              }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        if (messageText.contains("/killCharlie")) {
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
        if (messageText.contains("/saveTest")){
            Charmagotchi activeBot = botObjects.get(objectChatID);
            try{
                updateBotData(connectToDatabase(),activeBot);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    public Connection connectToDatabase() throws SQLException{
        Properties prop = new Properties();
        String url = "";
        String username = "";
        String password = "";
        try{
            FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\.properties");
            prop.load(fileInputStream);
            url = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
            fileInputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Attempting to connect to Database...");
        Connection connection = DriverManager.getConnection(url,username,password);
        if (connection.isValid(10)){
            System.out.println("Connected");
        } else {
            System.out.println("Connection Failed");
        }
        return connection;
    }

    public Charmagotchi selectExistingBot(Connection con, double inputChatId) throws SQLException{
        Charmagotchi activeBot = null;
        String query = "SELECT * FROM bots WHERE idBots = ?";
        double hunger;
        double happiness;
        double sleepiness;
        double fitness;
        boolean isAlive;

        try(PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setDouble(1,inputChatId);
            ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    hunger = resultSet.getDouble("statHunger");
                    happiness = resultSet.getDouble("statHunger");
                    sleepiness = resultSet.getDouble("statHunger");
                    fitness = resultSet.getDouble("statHunger");
                    activeBot = new Charmagotchi(hunger,happiness,sleepiness,fitness, (long) inputChatId);
                }
        } catch (SQLException e){
         e.printStackTrace();
        }
        return activeBot;
    }

    public static void insertNewBot(Connection con, Charmagotchi activeBot){
        System.out.println("Gathering bot data...");
        double hunger = activeBot.getHunger();
        double happiness = activeBot.getHappiness();
        double sleepiness = activeBot.getSleepiness();
        double fitness = activeBot.getFitness();
        double chatId = activeBot.getBotChatId();
        System.out.println("Saving bot data");
        String query = "INSERT INTO bots (idBots,statHunger,statHappiness,statSleepiness,statFitness) VALUES (?,?,?,?,?)";
        try(PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setDouble(1,chatId);
            preparedStatement.setDouble(2,hunger);
            preparedStatement.setDouble(3,happiness);
            preparedStatement.setDouble(4,sleepiness);
            preparedStatement.setDouble(5,fitness);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Save Successful");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void updateBotData(Connection con, Charmagotchi activeBot){
        System.out.println("Gathering bot data...");
        double hunger = activeBot.getHunger();
        double happiness = activeBot.getHappiness();
        double sleepiness = activeBot.getSleepiness();
        double fitness = activeBot.getFitness();
        double chatId = activeBot.getBotChatId();
        System.out.println("Updating bot data");
        String query = "UPDATE bots SET statHunger = ?, statHappiness = ?, statSleepiness = ?, statFitness = ? WHERE idBots = ?";
        try(PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setDouble(1,hunger);
            preparedStatement.setDouble(2,happiness);
            preparedStatement.setDouble(3,sleepiness);
            preparedStatement.setDouble(4,fitness);
            preparedStatement.setDouble(5,chatId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Update Successful");
                con.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void updateBotData(Connection con, double inHunger, double inHappiness, double inSleepiness, double inFitness,double inChatId){
        System.out.println("Updating bot data");
        String query = "UPDATE bots SET statHunger = ?, statHappiness = ?, statSleepiness = ?, statFitness = ? WHERE idBots = ?";
        try(PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setDouble(1, inHunger);
            preparedStatement.setDouble(2, inHappiness);
            preparedStatement.setDouble(3,inSleepiness);
            preparedStatement.setDouble(4,inFitness);
            preparedStatement.setDouble(5,inHappiness);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Update Successful");
                con.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

}




