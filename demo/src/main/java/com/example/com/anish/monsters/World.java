package com.example.com.anish.monsters;

import java.awt.Color;

public class World {

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    public Player player;
    public boolean state;
    public boolean ifSucceed;
    public int monsterCnt;
    public int fruitCnt;

    private Tile<Thing>[][] tiles;
    MazeGenerator mg;

    public World() {

        player = new Player(Color.RED, this);
        state = true;
        ifSucceed = false;
        monsterCnt = 0;

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        // set the maze including floor and wall
        mg = new MazeGenerator(WIDTH);
        fruitCnt = mg.generateMaze();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(new Floor(this));
                if (mg.maze[i][j] == 0) {
                    tiles[i][j].setThing(new Wall(this));
                } else if (mg.maze[i][j] == 2) {
                    tiles[i][j].setThing(new Fruit(Color.GRAY, this));
                }
            }
        }

        tiles[0][0].setThing(player);
        player.setPosition(0, 0);
        mg.maze[0][0] = 3;

        // mg.dfsMaze(0, 0, mg.visited);
    }

    public void execute(int dir) {
        // if (COUNT < mg.node.size()) {
        // Node tp = mg.node.elementAt(COUNT);
        // tiles[tp.x][tp.y].setThing(new Monster(new Color(255, 0, 0), this));
        // COUNT++;
        // }
        player.setKey();
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public synchronized void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

}
