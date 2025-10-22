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
    private volatile double fitness;
    private volatile boolean living;
    private volatile boolean asleep;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);



    Charmagotchi(long chatId){
        hunger = 100;
        happiness = 100;
        sleepiness = 100;
        fitness = 100;
        living = false;
        asleep = false;
        botChatId = chatId;
    }
    public void startCharmagotchi(){
        startStatsScheduler();
        statsAlerts();
        living = true;
    }
    public synchronized boolean isAlive(){
        return living;
    }
    public synchronized boolean isAsleep(){
        return asleep;
    }
    public synchronized void addToHunger(double a){
        if (hunger <= 100 && hunger + a <= 100){
            hunger += a;
        } else {
            hunger = 100;
        }
    }
    public synchronized void addToHappiness(double a){
        if (happiness < 100 && happiness + a <= 100){
            happiness += a;
        } else {
            happiness = 100;
        }

    }
    public synchronized void addToSleepiness(double a){
        if (sleepiness <= 100 && sleepiness + a <= 100){
            sleepiness += a;
        } else {
            sleepiness = 100;
        }
    }
    public synchronized void addToFitness(double a){
        if (fitness <= 100 && fitness + a <= 100){
            fitness += a;
        } else {
            fitness = 100;
        }
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
    public double getFitness(){
        return fitness;
    }
    public long getBotChatId(){
        return botChatId;
    }

    public double[] getStatsArray(){
        double[] stats = new double[4];
        stats[0] = hunger;
        stats[1] = happiness;
        stats[2] = sleepiness;
        stats[3] = fitness;
        return stats;
    }
    public String getStatsMessage(){
        double[] stats = getStatsArray();
        long hunger = Math.round(stats[0]);
        long happiness = Math.round(stats[1]);
        long sleep = Math.round(stats[2]);
        long fitness = Math.round(stats[3]);
        return ("\\/(°ᵔᴥᵔ°)\\/"+ "\n" +

                "Hello I'm Charlie! *Woof *Woof* \n\n"  +

                "Here is how I'm feeling."+ "\n" +
                "------------------------\n" +
                "Hunger: "+ hunger +"%"+"\n" +
                "Happiness: "+ happiness +"%"+"\n" +
                "Tiredness: "+ sleep +"%"+"\n" +
                "Fitness: "+ fitness +"%"+"\n" +
                "-------------------------\n" +
                "Use the actions below to interact with me!\nI'll send you notifications when I need anything.");
    }
    public String getActionMessage(){
        double[] stats = getStatsArray();
        long hunger = Math.round(stats[0]);
        long happiness = Math.round(stats[1]);
        long sleep = Math.round(stats[2]);
        long fitness = Math.round(stats[3]);
        return ("\\/(°ᵔᴥᵔ°)\\/"+ "\n" +
                "------------------------\n" +
                "Hunger: "+ hunger +"%"+"\n" +
                "Happiness: "+ happiness +"%"+"\n" +
                "Tiredness: "+ sleep +"%"+"\n" +
                "Fitness: "+ fitness +"%"+"\n" +
                "-------------------------\n" );
    }
    private int getRandomInt(int a, int b) {
        Random rand = new Random();
        return rand.nextInt(a, b);
    }

    public void startStatsScheduler(){
        scheduler.scheduleAtFixedRate(()->{
            addToHunger(-2); System.out.println(hunger);
            if (getHunger() <= 0){
                sendMessage("Charlie has starved to death. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
            }
        }, 1, 10, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(()->{
            addToHappiness(-1); System.out.println(happiness);
            if (getHappiness() <= 0){
                sendMessage("Charlie has died of sadness. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
            }
        }, 1, 10, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(()->{
            addToSleepiness(-2); System.out.println(sleepiness);
            if (getSleepiness() <= 0){
                sendMessage("Charlie has gone insane and died due to lack of sleep. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
            }
        }, 1, 10, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(()->{
            addToFitness(-.05); System.out.println(fitness);
            if (getFitness() <= 0){
                sendMessage("Charlie's heart gave out due to weakness. I hope you're happy >:(",botChatId);
                living = false;
                scheduler.shutdown();
            }
        }, 1, 10, TimeUnit.MINUTES);
    }

    public void playBall(){
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
        double[] stats = getStatsArray();
        if (stats[3]<100){
            int a = getRandomInt(1,10);
            addToFitness(a);
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
        finalMessage = message.toString();
        sendMessage(finalMessage,botChatId);
        message.setLength(0);

    }
    public void goForWalk(){
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
        double[] stats = getStatsArray();
        if (stats[3]<100){
            int a = getRandomInt(5,30);
            addToFitness(a);
            message.append("Charlie gained ").append(a).append(" Fitness").append("\n");
        }
        if (stats[1]<100){
            int a = getRandomInt(7,20);
            addToHappiness(a);
            message.append("Charlie gained ").append(a).append(" Happiness").append("\n");
        }
        if (stats[2]<100){
            int a = getRandomInt(-5,0);
            addToSleepiness(a);
            message.append("Charlie gained ").append(a).append(" Sleepiness").append("\n");
        }
        finalMessage = message.toString();
        sendMessage(finalMessage,botChatId);
        message.setLength(0);

    }
    public void feedMeal(){
        addToHunger(100);
        addToHappiness(7);
        switch (getRandomInt(0,2)){
            case 0:
                message.append("\\/(°ᵔᴥᵔ°)\\/\n"+"OM NOM NOM!").append("\n");
                break;
            case 1:
                message.append("\\/(°ᵔᴥᵔ°)\\/\n"+"MUNCH! CRUNCH! MUNCH!").append("\n");
                break;
            case 2:
                message.append("\\/(°ᵔᴥᵔ°)\\/\n"+"NOM NOM NOM!").append("\n");
                break;
        }
        message.append("Thank you human! I feel well fed.");
        finalMessage = message.toString();
        sendMessage(finalMessage,botChatId);
        message.setLength(0);
    }
    public void giveTreat(){
        addToHunger(1);
        addToHappiness(10);
        sendMessage("\\/(°ᵔᴥᵔ°)\\/\n"+"*Happy Munches*",botChatId);
    }
    public void sendToBed() {
        addToSleepiness(100);
       sendMessage("Charlie is feeling refreshed",botChatId);
    }
    public void speak(){
        switch (getRandomInt(0,2)){
            case 0:
                sendMessage("\\/(°ᵔᴥᵔ°)\\/\n"+"*Woof*",botChatId);
                break;
            case 1:
                sendMessage("\\/(°ᵔᴥᵔ°)\\/\n"+"*Bark*",botChatId);
                break;
            case 2:
                sendMessage("\\/(°ᵔᴥᵔ°)\\/\n"+"*Ruff*",botChatId);
                break;
        }
    }
    public void killCharlie(){
        sendMessage("You are a horrible person... That's what it says... a horrible person...",botChatId);
        living = false;
        scheduler.shutdown();

    }
    public void statsAlerts(){
        scheduler.scheduleAtFixedRate(()->{
            if (hunger < 70 && hunger > 50){
                message.append("I could use a snack :)").append("\n");
            }
            if (hunger < 50){
                message.append("Is it dinner time yet?").append("\n");
            }
            if (happiness > 25 && happiness < 50){
                message.append("I want to play!").append("\n");
            }
            if (happiness < 25){
                message.append("Why won't you play with me :(").append("\n");
            }
            if (sleepiness < 70 && sleepiness > 50){
                message.append("*Yawn* It's time for a nap").append("\n");
            }
            if (sleepiness < 50){
                message.append("I'm ready for bed").append("\n");
            }
            if (fitness > 25 && fitness < 35){
                message.append("Can we go for a walk? :D").append("\n");
            }
            if (fitness < 25){
                message.append("I've been cooped up all day! please take me for a walk").append("\n");
            }
            if (!message.isEmpty()){
                message.insert(0,"\\/(°ᵔᴥᵔ°)\\/\n");
                finalMessage = message.toString();
                sendMessage(finalMessage,botChatId);
                message.setLength(0);
            }
        },1,20,TimeUnit.MINUTES);
    }
}

    


