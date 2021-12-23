package com.example.com.anish.monsters;

import java.awt.Color;
import java.util.concurrent.*;

public class Player extends Creature implements Runnable {

    boolean check1, check2;
    public int direction;
    public int COUNT = 0;

    public Player(Color color, World world) {
        super(color, (char) 1, world);
        check1 = check2 = true;
    }

    public synchronized void setKey() {
        check2 = !check1;
    }

    public void playerMove(int dir) {
        Move playMove = new Move(world);
        int x = this.getX(), y = this.getY();
        // COUNT +=
        playMove.move(this, x, y, dir, 3);
    }

    @Override
    public void run() {
        while (this.world.state && !this.world.ifFinish) {
            if (check2 != check1) {
                playerMove(direction);
                check2 = check1;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
