package com.backvuebato.utils;

public class GenerateUsers implements Runnable{
    @Override
    public void run() {
        java.lang.System.out.println("Thread started");
        String name = Thread.currentThread().getName();
        long id = Thread.currentThread().getId();
        java.lang.System.out.println( "name: " + name + " id: " + id);
        java.lang.System.out.println("Thread ended");
    }
}
