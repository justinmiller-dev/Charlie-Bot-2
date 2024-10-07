package com.simplebot;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Charmagotchi extends TelegramBot{

    private volatile long botChatId;
    private volatile int hunger; 
    private volatile int happiness; 
    private volatile int sleepiness;
    private volatile int exercise; 
    private volatile boolean living;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static Charmagotchi charmagotchi = null;

    private Charmagotchi(int hung, int hap, int sle,int exe,long chatId){
        hunger = hung;
        happiness = hap;
        sleepiness = sle;
        exercise = exe;
        living = false;
        botChatId = chatId;
        System.out.println(chatId);

    }
    public void startCharmagotchi(){
        startStatsScheduler(botChatId);
        //startStatsWatcher();
        living = true;
    }
    
    public synchronized void updateHunger(int a){
        hunger += a;
    }
    public synchronized void updateHappiness(int a){
        happiness += a;
    }
    public synchronized void updateSleepiness(int a){
        sleepiness += a;
    }
    public synchronized void updateExercise(int a){
        exercise += a;
    }
    public void changeHunger(int a){
        synchronized(this){
            hunger += a;
        }
    }
    public void changeHappiness(int a){
        happiness += a;
    }
    public void changeSleepiness(int a){
        sleepiness += a;
    }
    public void changeExercise(int a){
        exercise += a;
    }
    public void setBotChatId(long a){
        a = chatId;
    }
    public int getHunger(){
        return hunger;
    }
    public int getHappiness(){
        return happiness;
    }
    public int getSleepiness(){
        return sleepiness;
    }
    public int getExercise(){
        return exercise;
    }
    public long getBotChatId(){
        return botChatId;
    }
    private void changeAlive(boolean alive){
        living = alive;
    }
    public synchronized boolean isAlive(){
        return living;
    }
    public void startStatsScheduler(long chatId){
        scheduler.scheduleAtFixedRate(()->{
            changeHunger(50); System.out.println(hunger);
            System.out.println(chatId);
            if (getHunger() >= 100){
                sendMessage("Charlie has starved to death. I hope you're happy >:(",chatId);
                living = false;
                scheduler.shutdown();
                charmagotchi = null;
            }
        }, 0, 8, TimeUnit.HOURS);
        scheduler.scheduleAtFixedRate(()->{
            changeHappiness(-5); System.out.println(happiness);
            if (getHappiness() <= 0){
                sendMessage("Charlie has died of sadness. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
                charmagotchi = null;
            }
        }, 0, 10, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(()->{
            changeSleepiness(5); System.out.println(sleepiness);
            if (getSleepiness() >= 100){
                sendMessage("Charlie has gone insane and died due to lack of sleep. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
                charmagotchi = null;
            }
        }, 0, 10, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(()->{
            changeExercise(-5); System.out.println(exercise);
            if (getExercise() <= 0){
                sendMessage("Charlie's heart gave out due to weakness. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
                charmagotchi = null;
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
    public int[] getStatsArray(){
        int[] stats = new int[4];
        stats[0] = hunger;
        stats[1] = happiness;
        stats[2] = sleepiness;
        stats[3] = exercise;
        return stats;
    }
    public static synchronized Charmagotchi getInstance(long a) {
        if (charmagotchi == null) {
            charmagotchi = new Charmagotchi(0, 50, 50, 50, a);
        }
        return charmagotchi;
    }
    private int getRandomInt(int a, int b){
       Random rand = new Random();
       int randInt = rand.nextInt(a,b);
       return randInt;
    }
    public void playBall(){

        int[] stats = getStatsArray();
        if (stats[3]<100){
            int a = getRandomInt(1,10);
            updateExercise(a); sendMessage("Charlie gained " + a + " Fitness",botChatId);
        }
        if (stats[1]<100){
            int a = getRandomInt(1,20);
            updateHappiness(a); sendMessage("Charlie gained " + a + " Happiness",botChatId);
        }
        if (stats[2]<100){
            int a = getRandomInt(0,5);
            updateSleepiness(a); sendMessage("Charlie gained " + a + " Sleepiness",botChatId);
        }
        switch (getRandomInt(0,1)){
            case 0:
                sendMessage("You throw the ball. Charlie catches it and brings it back",botChatId);
                break;
            case 1:
                sendMessage("You throw the ball. Charlie misses the ball and it rolls away. He picks it up and brings it back",botChatId);
        }


    }
    public void goForWalk(){

    }
    public void feedMeal(){

    }
    public void giveTreat(){

    }
    public void sendToBed() {

    }
    public void speak(){

    }
    public void showStats(){
        int[] stats = getStatsArray();
        sendMessage("Charlie's current stats are."+ "\n" +
                " Hunger: "+ stats[0] +"\n" +
                " Happiness: "+ stats[1] +"\n" +
                " Sleepiness: "+ stats[2] +"\n" +
                " Fitness: "+ stats[3] +"\n"
                ,botChatId);
    }
}

    


