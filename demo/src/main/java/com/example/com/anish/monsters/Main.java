package com.example.com.anish.monsters;

public class Main {
    public static void main(String[] args) {
        MazeGenerator mazeGenerator = new MazeGenerator(30);
        mazeGenerator.generateMaze();

        // System.out.println("RAW MAZE\n" + mazeGenerator.getRawMaze());
        System.out.println(mazeGenerator.getSymbolicMaze());
        // System.out.println("SYMBOLIC MAZE\n" + mazeGenerator.getSymbolicMaze());
    }
}