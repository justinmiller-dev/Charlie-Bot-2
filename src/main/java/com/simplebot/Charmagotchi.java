package com.simplebot;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Charmagotchi extends TelegramBot{
    StringBuilder message = new StringBuilder();
    private String finalMessage;
    private long botChatId;
    private volatile double hunger;
    private volatile double happiness;
    private volatile double sleepiness;
    private volatile double exercise;
    private volatile boolean living;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static Charmagotchi charmagotchi = null;


    Charmagotchi(double hung, double hap, double sle, double exe, long chatId){
        hunger = hung;
        happiness = hap;
        sleepiness = sle;
        exercise = exe;
        living = false;
        botChatId = chatId;
    }
    public void startCharmagotchi(){
        startStatsScheduler(botChatId);
        statsAlerts();
        living = true;
        showStats();
    }
    
    public synchronized void addToHunger(double a){
        hunger += a;
    }
    public synchronized void addToHappiness(double a){
        happiness += a;
    }
    public synchronized void addToSleepiness(double a){
        sleepiness += a;
    }
    public synchronized void addToExercise(double a){
        exercise += a;
    }
    public void changeHunger(double a){
        synchronized(this){
            hunger += a;
        }
    }
    public void changeHappiness(double a){
        happiness += a;
    }
    public void changeSleepiness(double a){
        sleepiness += a;
    }
    public void changeExercise(double a){
        exercise += a;
    }
    public void setBotChatId(long a){
        botChatId = a;
    }
    public double getHunger(){
        return hunger;
    }
    public double getHappiness(){
        return happiness;
    }
    public double getSleepiness(){
        return sleepiness;
    }
    public double getExercise(){
        return exercise;
    }
    public long getBotChatId(){
        return botChatId;
    }
    public synchronized boolean isAlive(){
        return living;
    }
    public void startStatsScheduler(long chatId){
        scheduler.scheduleAtFixedRate(()->{
            changeHunger(2); System.out.println(hunger);
            if (getHunger() >= 100){
                sendMessage("Charlie has starved to death. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
                charmagotchi = null;
            }
        }, 0, 10, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(()->{
            changeHappiness(-1); System.out.println(happiness);
            if (getHappiness() <= 0){
                sendMessage("Charlie has died of sadness. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
                charmagotchi = null;
            }
        }, 0, 10, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(()->{
            changeSleepiness(2); System.out.println(sleepiness);
            if (getSleepiness() >= 100){
                sendMessage("Charlie has gone insane and died due to lack of sleep. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
                charmagotchi = null;
            }
        }, 0, 10, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(()->{
            changeExercise(-.05); System.out.println(exercise);
            if (getExercise() <= 0){
                sendMessage("Charlie's heart gave out due to weakness. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
                charmagotchi = null;
            }
        }, 0, 10, TimeUnit.MINUTES);
    }
    public double[] getStatsArray(){
        double[] stats = new double[4];
        stats[0] = hunger;
        stats[1] = happiness;
        stats[2] = sleepiness;
        stats[3] = exercise;
        return stats;
    }
    public static synchronized Charmagotchi getInstance(long a) {
        if (charmagotchi == null) {
            charmagotchi = new Charmagotchi(0, 55, 0, 71, a);
        }
        return charmagotchi;
    }
    private int getRandomInt(int a, int b){
       Random rand = new Random();
        return rand.nextInt(a,b);
    }
    public void playBall(){

        double[] stats = getStatsArray();
        if (stats[3]<100){
            int a = getRandomInt(1,10);
            addToExercise(a);
            message.append("Charlie gained ").append(a).append(" Fitness").append("\n");
        }
        if (stats[1]<100){
            int a = getRandomInt(1,20);
            addToHappiness(a);
            message.append("Charlie gained ").append(a).append(" Happiness").append("\n");
        }
        if (stats[2]<100){
            int a = getRandomInt(0,5);
            addToSleepiness(a);
            message.append("Charlie gained ").append(a).append(" Sleepiness").append("\n");
        }
        switch (getRandomInt(0,1)){
            case 0:
                message.append("You throw the ball. Charlie catches it and brings it back");
                finalMessage = message.toString();
                sendMessage(finalMessage,botChatId);
                message.setLength(0);
                break;
            case 1:
                message.append("You throw the ball. Charlie misses the ball and it rolls away. He picks it up and brings it back");
                finalMessage = message.toString();
                sendMessage(finalMessage,botChatId);
                message.setLength(0);
                break;
        }
    }
    public void goForWalk(){
        double[] stats = getStatsArray();
        if (stats[3]<100){
            int a = getRandomInt(5,30);
            addToExercise(a);
            message.append("Charlie gained ").append(a).append(" Fitness").append("\n");
        }
        if (stats[1]<100){
            int a = getRandomInt(7,20);
            addToHappiness(a);
            message.append("Charlie gained ").append(a).append(" Happiness").append("\n");
        }
        if (stats[2]<100){
            int a = getRandomInt(0,5);
            addToSleepiness(a);
            message.append("Charlie gained ").append(a).append(" Sleepiness").append("\n");
        }
        switch (getRandomInt(0,2)){
            case 0:
                message.append("You go for a walk around the park. Charlie sees a squirrel and chases it up a tree.");
                finalMessage = message.toString();
                sendMessage(finalMessage,botChatId);
                message.setLength(0);
                break;
            case 1:
                message.append("You go for a walk around town. Charlie makes a new friend.");
                finalMessage = message.toString();
                sendMessage(finalMessage,botChatId);
                message.setLength(0);
                break;
            case 2:
                message.append("You go for a walk around the park. It's fun but uneventful.");
                finalMessage = message.toString();
                sendMessage(finalMessage,botChatId);
                message.setLength(0);
                break;
        }
    }
    public void feedMeal(){
        double currentHunger = getHunger();
        double updateHunger =-currentHunger;
        addToHunger(updateHunger);
        addToHappiness(7);
        switch (getRandomInt(0,2)){
            case 0:
                message.append("OM NOM NOM!").append("\n");
                break;
            case 1:
                message.append("MUNCH! CRUNCH! MUNCH!").append("\n");
                break;
            case 2:
                message.append("NOM NOM NOM").append("\n");
                break;
        }
        message.append("Charlie feels well fed.");
        finalMessage = message.toString();
        sendMessage(finalMessage,botChatId);
        message.setLength(0);
    }
    public void giveTreat(){
        addToHunger(-1);
        addToHappiness(10);
        sendMessage("*Happy Munches*",botChatId);
    }
    public void sendToBed() {
        double currentSleep = getSleepiness();
        double updateSleep =-currentSleep;
        addToSleepiness(updateSleep);
       sendMessage("Charlie is feeling refreshed",botChatId);
    }
    public void speak(){
        switch (getRandomInt(0,2)){
            case 0:
                sendMessage("*Woof*",botChatId);
                break;
            case 1:
                sendMessage("*Bark*",botChatId);
                break;
            case 2:
                sendMessage("*Ruff*",botChatId);
                break;
        }
    }
    public void killCharlie(){
        sendMessage("You're a monster",botChatId);
        living = false;
        scheduler.shutdown();
        charmagotchi = null;

    }
    public void showStats(){
        double[] stats = getStatsArray();
        long hunger = Math.round(stats[0]);
        long happiness = Math.round(stats[1]);
        long sleep = Math.round(stats[2]);
        long fitness = Math.round(stats[3]);
        sendMessage("Charlie's current stats are."+ "\n" +
                " Hunger: "+ hunger +"%"+"\n" +
                " Happiness: "+ happiness +"%"+"\n" +
                " Tiredness: "+ sleep +"%"+"\n" +
                " Fitness: "+ fitness +"%"+"\n"
                ,botChatId);
    }
    public void statsAlerts(){
        scheduler.scheduleAtFixedRate(()->{
            //showStats();
            double[] stats = getStatsArray();
            if (stats[0] > 50 && stats[0]< 70){
                message.append("*Tummy Grumble*").append("\n");
            }
            if (stats[0] > 70){
                message.append("*wimper* *Tummy Grumble*").append("\n");
            }
            if (stats[1] > 25 && stats[1] < 50){
                message.append("Charlie brings you his ball *Sad Woof*").append("\n");
            }
            if (stats[1] > 10 && stats[1] < 25){
                message.append("Charlie lays his head on you and lets out a sigh. You see tears in his eyes.").append("\n");
            }
            if (stats[2] > 50 && stats[2]< 70){
                message.append("Charlie is looking pretty tired.").append("\n");
            }
            if (stats[0] > 70){
                message.append("Charlie can't keep his eyes open.").append("\n");
            }
            if (stats[3] > 25 && stats[3]< 35){
                message.append("Charlie looks a little weak.").append("\n");
            }
            if (stats[3] > 10 && stats[3]< 25){
                message.append("Charlie looks very weak.").append("\n");
            }
            finalMessage = message.toString();
            sendMessage(finalMessage,botChatId);
            message.setLength(0);
        },0,20,TimeUnit.MINUTES);
    }
}

    


