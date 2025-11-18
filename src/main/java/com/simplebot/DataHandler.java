package com.simplebot;

import java.sql.*;
import java.util.HashMap;


public class DataHandler {

    private static final HashMap<Long, Charmagotchi> botObjects = new HashMap<>();

    public static HashMap<Long, Charmagotchi> getBots(){
        return botObjects;
    }

    public static Connection connectToDatabase() {
        Connection connection = null;
        try{
            System.out.println("Attempting to connect to Database...");
            connection = DriverManager.getConnection(Main.getDbURL(),Main.getDBUsername(),Main.getDbPassword());
            if (connection.isValid(10)){
                System.out.println("Connected");
            } else {
                System.out.println("Connection Failed");
            }
        } catch (SQLException e) {
            System.err.println("Error reading resource: " + e.getMessage());
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
        int messageId;

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
                messageId = resultSet.getInt("lastMessageId");
                activeBot = new Charmagotchi(hunger,happiness,sleepiness,fitness, (long) chatId,isAlive,isAsleep,messageId);
                botObjects.putIfAbsent(activeBot.getBotChatId(),activeBot);
                System.out.println("Added Bot " + activeBot.getBotChatId() + " To hashmap");
            }
        } catch (SQLException e){
            System.out.println("Unable to connect to database");
            System.err.println(e.getMessage());
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
            System.out.println("Insert Failed");
            System.err.println(e.getMessage());
        }

    }
    public static void updateBotData(Connection con, double[] inStatsArray, double inChatId, boolean inIsAsleep, boolean inIsAlive, int inMessageId){
        String query = "UPDATE bots SET statHunger = ?, statHappiness = ?, statSleepiness = ?, statFitness = ?, isAlive = ?, isAsleep = ?, lastMessageId = ? WHERE idBots = ?";
        try(PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setDouble(1, inStatsArray[0]);
            preparedStatement.setDouble(2, inStatsArray[1]);
            preparedStatement.setDouble(3,inStatsArray[2]);
            preparedStatement.setDouble(4,inStatsArray[3]);
            preparedStatement.setBoolean(5,inIsAlive);
            preparedStatement.setBoolean(6,inIsAsleep);
            preparedStatement.setInt(7,inMessageId);
            preparedStatement.setDouble(8,inChatId);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Unable to connect to database");
            System.err.println(e.getMessage());
        }
    }
    public static void deleteBotData(Connection con, double inChatId){
        String query = "DELETE FROM bots WHERE idBots = ?";
        try(PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setDouble(1,inChatId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Bot data deleted");
            }
        } catch (SQLException e){
            System.out.println("Unable to connect to database");
            System.err.println(e.getMessage());
        }
    }

}
