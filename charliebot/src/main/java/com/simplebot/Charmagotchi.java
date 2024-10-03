package com.simplebot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Charmagotchi{

    private volatile int hunger; 
    private volatile int happiness; 
    private volatile int sleepiness;
    private volatile int exercise; 
    private volatile boolean living;

    private final ScheduledExecutorService schedular = Executors.newScheduledThreadPool(1);

    public Charmagotchi(int hung, int hap, int sle,int exe){
        hunger = hung;
        happiness = hap;
        sleepiness = sle;
        exercise = exe;
        changeAlive(true);
        startStatsScheduler();
    }
    
    public synchronized void updateHunger(int a){
        hunger += a;
    }
    public synchronized void updateHappiness(int a){
        happiness += a;
    }
    public synchronized void updateSleepiness(int a){
        happiness += a;
    }
    public synchronized void updateExercise(int a){
        happiness += a;
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
    private void changeAlive(boolean alive){
        living = alive;
    }
    public boolean isAlive(){
        return living;
    }
    public void startStatsScheduler(){
        schedular.scheduleAtFixedRate(()->{
            changeHunger(5); System.out.println(hunger);
        }, 0, 1, TimeUnit.SECONDS);
        schedular.scheduleAtFixedRate(()->{
            changeHappiness(-5); System.out.println(happiness);
        }, 0, 10, TimeUnit.SECONDS);
        schedular.scheduleAtFixedRate(()->{
            changeSleepiness(5); System.out.println(sleepiness);
        }, 0, 10, TimeUnit.SECONDS);
        schedular.scheduleAtFixedRate(()->{
            changeExercise(-5); System.out.println(exercise);
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
}

    


