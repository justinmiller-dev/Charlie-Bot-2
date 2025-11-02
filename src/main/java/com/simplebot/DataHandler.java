package com.simplebot;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

public class DataHandler {

    private static final HashMap<Long, Charmagotchi> botObjects = new HashMap<>();

    public static HashMap<Long, Charmagotchi> getBots(){
        return botObjects;
    }

    public static Connection connectToDatabase() {
        Properties prop = new Properties();
        String url;
        String username;
        String password;
        Connection connection = null;
        try{
            FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\.properties");
            prop.load(fileInputStream);
            url = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
            fileInputStream.close();
            System.out.println("Attempting to connect to Database...");
            connection = DriverManager.getConnection(url,username,password);
            if (connection.isValid(10)){
                System.out.println("Connected");
            } else {
                System.out.println("Connection Failed");
            }
        }catch (IOException | SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    public static void initializeBotMap(Connection con) {
        Charmagotchi activeBot;
        String query = "SELECT * FROM bots "; // WHERE idBots = ?
        double hunger;
        double happiness;
        double sleepiness;
        double fitness;
        double chatId;
        boolean isAsleep;
        boolean isAlive;

        try(PreparedStatement preparedStatement = con.prepareStatement(query)) {
            System.out.println("Gathering bot data from database");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                hunger = resultSet.getDouble("statHunger");
                happiness = resultSet.getDouble("statHappiness");
                sleepiness = resultSet.getDouble("statSleepiness");
                fitness = resultSet.getDouble("statFitness");
                isAlive = resultSet.getBoolean("isAlive");
                chatId = resultSet.getDouble("idBots");
                isAsleep = resultSet.getBoolean("isAsleep");
                activeBot = new Charmagotchi(hunger,happiness,sleepiness,fitness, (long) chatId,isAlive,isAsleep);
                botObjects.putIfAbsent(activeBot.getBotChatId(),activeBot);
                System.out.println("Added Bot " + activeBot.getBotChatId() + " To hashmap");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void insertNewBot(Connection con, Charmagotchi activeBot){
        double hunger = activeBot.getHunger();
        double happiness = activeBot.getHappiness();
        double sleepiness = activeBot.getSleepiness();
        double fitness = activeBot.getFitness();
        double chatId = activeBot.getBotChatId();
        String query = "INSERT INTO bots (idBots,statHunger,statHappiness,statSleepiness,statFitness) VALUES (?,?,?,?,?)";
        try(PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setDouble(1,chatId);
            preparedStatement.setDouble(2,hunger);
            preparedStatement.setDouble(3,happiness);
            preparedStatement.setDouble(4,sleepiness);
            preparedStatement.setDouble(5,fitness);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("New bot added to database");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void updateBotData(Connection con, Charmagotchi activeBot){
        double hunger = activeBot.getHunger();
        double happiness = activeBot.getHappiness();
        double sleepiness = activeBot.getSleepiness();
        double fitness = activeBot.getFitness();
        double chatId = activeBot.getBotChatId();
        boolean alive = activeBot.isAlive();
        boolean asleep = activeBot.isAsleep();

        String query = "UPDATE bots SET statHunger = ?, statHappiness = ?, statSleepiness = ?, statFitness = ?, isAlive = ?, isAsleep = ? WHERE idBots = ?";
        try(PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setDouble(1,hunger);
            preparedStatement.setDouble(2,happiness);
            preparedStatement.setDouble(3,sleepiness);
            preparedStatement.setDouble(4,fitness);
            preparedStatement.setBoolean(5,alive);
            preparedStatement.setBoolean(6,asleep);
            preparedStatement.setDouble(7,chatId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Bot data updated");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void updateBotData(Connection con, double[] inStatsArray, double inChatId, boolean inIsAsleep, boolean inIsAlive){
        String query = "UPDATE bots SET statHunger = ?, statHappiness = ?, statSleepiness = ?, statFitness = ?, isAlive = ?, isAsleep = ? WHERE idBots = ?";
        try(PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setDouble(1, inStatsArray[0]);
            preparedStatement.setDouble(2, inStatsArray[1]);
            preparedStatement.setDouble(3,inStatsArray[2]);
            preparedStatement.setDouble(4,inStatsArray[3]);
            preparedStatement.setBoolean(5,inIsAlive);
            preparedStatement.setBoolean(6,inIsAsleep);
            preparedStatement.setDouble(7,inChatId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Bot data updated");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
