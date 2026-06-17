package main;

import Database.InitDB;

public class Main {
    public static void main(String[] args) {

        // 🔥 INIT DATABASE DULU
        InitDB.init();

        // 🔥 START GAME
        Game game = new Game();
        game.start();
    }
}