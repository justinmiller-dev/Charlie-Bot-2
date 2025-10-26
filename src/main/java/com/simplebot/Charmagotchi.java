package com.simplebot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Charmagotchi extends TelegramBot{
    private String finalMessage;
    private long botChatId;
    private volatile double hunger;
    private volatile double happiness;
    private volatile double sleepiness;
    private volatile double fitness;
    private volatile boolean living;
    private volatile boolean asleep;
    private volatile boolean cooldown;
    private final ScheduledExecutorService statsSchedular = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService cooldownSchedular = Executors.newScheduledThreadPool(1);
    InlineKeyboardMarkup keyboardMarkup = actionButtons(botChatId);
    StringBuilder message = new StringBuilder();


    Charmagotchi(long chatId){
        hunger = 100;
        happiness = 100;
        sleepiness = 100;
        fitness = 100;
        living = false;
        asleep = false;
        cooldown = false;
        botChatId = chatId;
    }
    public void startCharmagotchi(){
        startScheduler();
        statsAlerts();
        coolDownScheduler();
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
        if (happiness <= 100 && happiness + a <= 100){
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
    public void getStartMessage(){
        double[] stats = getStatsArray();
        long hunger = Math.round(stats[0]);
        long happiness = Math.round(stats[1]);
        long sleep = Math.round(stats[2]);
        long fitness = Math.round(stats[3]);
        sendInlineKeyboard(botChatId,keyboardMarkup,
                "\\/(°ᵔᴥᵔ°)\\/"+ "\n" +

                "Hello I'm Charlie! *Woof* *Woof* \n\n"  +

                "Here is how I'm feeling."+ "\n" +
                "------------------------\n" +
                "Hunger: "+ hunger +"%"+"\n" +
                "Happiness: "+ happiness +"%"+"\n" +
                "Tiredness: "+ sleep +"%"+"\n" +
                "Fitness: "+ fitness +"%"+"\n" +
                "-------------------------\n" +
                "Use the actions below to interact with me!\nI'll send you notifications when I need anything.");
    }
    public void getActionMessage(){
        double[] stats = getStatsArray();
        long hunger = Math.round(stats[0]);
        long happiness = Math.round(stats[1]);
        long sleep = Math.round(stats[2]);
        long fitness = Math.round(stats[3]);
        sendInlineKeyboard(botChatId,keyboardMarkup,
                "\\/(°ᵔᴥᵔ°)\\/"+ "\n" +
                "------------------------\n" +
                "Hunger: "+ hunger +"%"+"\n" +
                "Happiness: "+ happiness +"%"+"\n" +
                "Tiredness: "+ sleep +"%"+"\n" +
                "Fitness: "+ fitness +"%"+"\n" +
                "-------------------------\n");
    }
    public void getActionMessage(String messageText){
        double[] stats = getStatsArray();
        long hunger = Math.round(stats[0]);
        long happiness = Math.round(stats[1]);
        long sleep = Math.round(stats[2]);
        long fitness = Math.round(stats[3]);

        sendInlineKeyboard(botChatId, keyboardMarkup,
                "\\/(°ᵔᴥᵔ°)\\/"+ "\n" +
                "------------------------\n" +
                "Hunger: "+ hunger +"%"+"\n" +
                "Happiness: "+ happiness +"%"+"\n" +
                "Tiredness: "+ sleep +"%"+"\n" +
                "Fitness: "+ fitness +"%"+"\n" +
                "-------------------------\n" + messageText);
    }
    public void getSleepMessage(ReplyKeyboard keyboard){
        sendInlineKeyboard(botChatId,keyboard,"\\/(°ᵔᴥᵔ°)\\/\nZZZzZZzzZZZ");
    }

    private int getRandomInt(int a, int b) {
        long currentTime = System.currentTimeMillis();
        Random rand = new Random();
        rand.setSeed(currentTime);
        return rand.nextInt(a, b + 1);
    }

    public void startScheduler(){
        statsSchedular.scheduleAtFixedRate(()->{
            if(!asleep){
                addToHunger(-2); System.out.println(hunger);
                if (getHunger() <= 0){
                    sendMessage("Charlie has starved to death. I hope you're happy >:(",botChatId);
                    living = false;
                    statsSchedular.shutdown();
                }
            }
        }, 1, 10, TimeUnit.MINUTES);
        statsSchedular.scheduleAtFixedRate(()->{
            if(!asleep){
                addToHappiness(-1); System.out.println(happiness);
                if (getHappiness() <= 0){
                    sendMessage("Charlie has died of sadness. I hope you're happy >:(",botChatId);
                    living = false;
                    statsSchedular.shutdown();
                }
            }
        }, 1, 10, TimeUnit.MINUTES);
        statsSchedular.scheduleAtFixedRate(()->{
            if(!asleep){
                addToSleepiness(-2); System.out.println(sleepiness);
                if (getSleepiness() <= 0){
                    sendMessage("Charlie has gone insane and died due to lack of sleep. I hope you're happy >:(",botChatId);
                    living = false;
                    statsSchedular.shutdown();
                }
            }
        }, 1, 10, TimeUnit.MINUTES);
        statsSchedular.scheduleAtFixedRate(()->{
            if(!asleep){
                addToFitness(-.5); System.out.println(fitness);
                if (getFitness() <= 0){
                    sendMessage("Charlie's heart gave out due to weakness. I hope you're happy >:(",botChatId);
                    living = false;
                    statsSchedular.shutdown();
                }
            }
        }, 1, 10, TimeUnit.MINUTES);
    }
    private void coolDownScheduler(){
        cooldownSchedular.scheduleAtFixedRate(()->{
            if (cooldown){
                cooldown = false;
            }
        }, 0, 8, TimeUnit.SECONDS);
    }

    public void playBall(){
        switch (getRandomInt(1,2)){
            case 1:
                message.append("You throw the ball. Charlie catches it and brings it back\n");
                break;
            case 2:
                message.append("You throw the ball. Charlie misses the ball and it rolls away. He picks it up and brings it back\n");
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
        if (stats[2]<=100){
            int a = getRandomInt(-5,0);
            addToSleepiness(a);
            message.append("Charlie lost ").append(a).append(" Tiredness").append("\n");
        }
        finalMessage = message.toString();
        message.setLength(0);
        getActionMessage(finalMessage);
    }
    public void goForWalk(){
        switch (getRandomInt(1,3)){
            case 1:
                message.append("You go for a walk around the park. Charlie sees a squirrel and chases it up a tree.\n");
                break;
            case 2:
                message.append("You go for a walk around town. Charlie makes a new friend.\n");
                break;
            case 3:
                message.append("You go for a walk around the park. It's fun but uneventful.\n");
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
        if (stats[2]<=100){
            int a = getRandomInt(-5,0);
            addToSleepiness(a);
            message.append("Charlie lost ").append(a).append(" Tiredness").append("\n");
        }
        finalMessage = message.toString();
        message.setLength(0);
        getActionMessage(finalMessage);

    }
    public void feedMeal(){
        addToHunger(100);
        addToHappiness(7);
        switch (getRandomInt(0,2)){
            case 0:
                message.append("OM NOM NOM!\n");
                break;
            case 1:
                message.append("MUNCH! CRUNCH! MUNCH!\n");
                break;
            case 2:
                message.append("NOM NOM NOM!\n");
                break;
        }
        message.append("Thank you human! I feel well fed.");
        finalMessage = message.toString();
        message.setLength(0);
        getActionMessage(finalMessage);
    }
    public void giveTreat(){
        addToHunger(1);
        addToHappiness(10);
        message.append("*Happy Munches*");
        finalMessage = message.toString();
        getActionMessage(finalMessage);
        message.setLength(0);
    }
    public void sendToBed() {
        asleep = true;
        addToSleepiness(100);
    }
    public void wake(){
        asleep = false;
        message.append("Charlie feels refreshed!");
        finalMessage = message.toString();
        getActionMessage(finalMessage);
        message.setLength(0);
    }
    public void speak(){
        switch (getRandomInt(0,2)){
            case 0:
                message.append("*Woof*");
                break;
            case 1:
                message.append("*Bark*");
                break;
            case 2:
                message.append("*Ruff*");
                break;
        }
        finalMessage = message.toString();
        getActionMessage(finalMessage);
        message.setLength(0);
    }
    public void killCharlie(){
        sendMessage("You are a horrible person... That's what it says... a horrible person...",botChatId);
        living = false;
        statsSchedular.shutdown();
    }
    public void statsAlerts(){
        statsSchedular.scheduleAtFixedRate(()->{
            if (hunger < 50 && hunger > 20){
                ReplyKeyboard actionButton = actionButtonMaker(botChatId,"Feed","feed");
                sendInlineKeyboard(botChatId,actionButton,"\\/(°ᵔᴥᵔ°)\\/\nI'm Hungry!");
            }
            if (hunger < 20){
                ReplyKeyboard actionButton = actionButtonMaker(botChatId,"Feed","feed");
                sendInlineKeyboard(botChatId,actionButton,"\\/(°ᵔᴥᵔ°)\\/\nIs it dinner time yet?");
            }
            if (happiness < 50 && happiness > 20){
                ReplyKeyboard actionButton = actionButtonMaker(botChatId,"Play Ball","playball");
                sendInlineKeyboard(botChatId,actionButton,"\\/(°ᵔᴥᵔ°)\\/\nI want to play catch :D");
            }
            if (happiness < 20){
                ReplyKeyboard actionButton = actionButtonMaker(botChatId,"Play Ball","play");
                sendInlineKeyboard(botChatId,actionButton,"\\/(°ᵔᴥᵔ°)\\/\nWhy wont you play with me? :(");
            }
            if (sleepiness < 50 && sleepiness > 20){
                ReplyKeyboard actionButton = actionButtonMaker(botChatId,"Send to bed","bed");
                sendInlineKeyboard(botChatId,actionButton,"\\/(°ᵔᴥᵔ°)\\/\n*Yawn* It's time for a nap");
            }
            if (sleepiness < 20){
                ReplyKeyboard actionButton = actionButtonMaker(botChatId,"Send to bed","bed");
                sendInlineKeyboard(botChatId,actionButton,"\\/(°ᵔᴥᵔ°)\\/\nI'm ready for bed");
            }
            if (fitness < 50 && fitness > 20){
                ReplyKeyboard actionButton = actionButtonMaker(botChatId,"Go for walk","walk");
                sendInlineKeyboard(botChatId,actionButton,"\\/(°ᵔᴥᵔ°)\\/\nCan we go for a walk? :D");
            }
            if (fitness < 20){
                ReplyKeyboard actionButton = actionButtonMaker(botChatId,"Go for walk","walk");
                sendInlineKeyboard(botChatId,actionButton,"\\/(°ᵔᴥᵔ°)\\/\nI've been cooped up all day! please take me for a walk :'(");
            }

        },1,20,TimeUnit.MINUTES);
    }
    public InlineKeyboardMarkup actionButtons(long objectChatID){
        //List<InlineKeyboardButton> button = new ArrayList<>();
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
//        button.add(buttonOne);
//        button.add(buttonTwo);
//        button.add(buttonThree);
//        button.add(buttonFour);
//        button.add(buttonFive);
//        button.add(buttonSix);

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
    public InlineKeyboardMarkup actionButtonMaker(long objectChatID,String buttonText,String callback){
        InlineKeyboardButton buttonOne= new InlineKeyboardButton(buttonText);
        buttonOne.setText(buttonText);
        buttonOne.setCallbackData(callback);

        List<InlineKeyboardRow> KeyboardRows = new ArrayList<>();
        InlineKeyboardRow row1 = new InlineKeyboardRow(buttonOne);

        KeyboardRows.add(row1);

        return new InlineKeyboardMarkup(KeyboardRows);
    }
}

    


