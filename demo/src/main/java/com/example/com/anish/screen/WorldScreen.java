package com.example.com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;

// import com.anish.monsters.BubbleSorter;
// import com.anish.monsters.Monster;
import com.example.com.anish.monsters.World;
import com.example.com.anish.monsters.Player;
import com.example.com.anish.monsters.Number;
import com.example.com.anish.monsters.Character;
import com.example.com.anish.monsters.Monster;
import com.example.com.anish.monsters.Save;
import com.example.com.anish.monsters.Continue;
// import com.anish.monsters.Matrix;

import com.example.asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    public World world;
    private Thread p;
    private Thread[] m;

    private Save mySave;
    private Continue myContinue;
    // String[] sortSteps;

    public WorldScreen() {
        world = new World();
        final int size = 16;
        world.ifBegin = 0;
        p = new Thread(world.player);
        m = new Thread[10];
        try {
            mySave = new Save(world);
            myContinue = new Continue(world);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startThread() {
        world.newGame();
        world.ifBegin = 1;
        for (int i = 0; i < 3; i++) {
            int tp = world.monsterNum;
            world.monsters[tp] = new Monster(Color.yellow, world);
            if (tp < world.monsterNum) {
                m[tp] = new Thread(world.monsters[tp]);
                m[tp].start();
            }
        }
        while (world.monsterNum < 1) {
            m[0] = new Thread(new Monster(Color.yellow, world));
            m[0].start();
        }
        p.start();
    }

    public void continueThread() throws IOException {
        System.out.println("continue");
        boolean exist = myContinue.readRecord();
        if (exist) {
            world.continueGame();
            for (int i = 0; i < world.monsterNum; i++) {
                m[i] = new Thread(world.monsters[i]);
                m[i].start();
            }
            world.ifBegin = 2;
            p.start();
        } else {
            startThread();
        }
    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    // private void execute(Monster[] bros, String step) {
    // String[] couple = step.split("<->");
    // getBroByRank(bros, Integer.parseInt(couple[0])).swap(getBroByRank(bros,
    // Integer.parseInt(couple[1])));
    // }

    // private Monster getBroByRank(Monster[] bros, int rank) {
    // for (Monster bro : bros) {
    // if (bro.getRank() == rank) {
    // return bro;
    // }
    // }
    // return null;
    // }
    private void printCharacter(AsciiPanel terminal, String s, int x, int y) {
        for (int i = 0; i < s.length(); i++) {
            Character c = new Character(Color.PINK, world, s.charAt(i) - 'a');
            terminal.write(c.getGlyph(), x + i, y, c.getColor());
        }
    }

    public void judgeFinish() {
        if (world.monsterCnt + world.player.COUNT == world.fruitCnt)
            world.ifFinish = true;
    }

    public void judgeSucceed() {
        // System.out.println(world.monsterCnt);
        // System.out.println(world.player.COUNT);
        // System.out.println(world.fruitCnt);
        if (world.ifFinish) {
            if (world.player.COUNT > world.monsterCnt)
                world.ifSucceed = true;
        }
    }

    private void displayBegin(AsciiPanel terminal) {
        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
        int num = world.player.COUNT;
        int a = num / 10, b = num % 10;
        Number aNum = new Number(Color.PINK, world, a), bNum = new Number(Color.PINK, world, b);
        printCharacter(terminal, "player", 32, 20);
        terminal.write(aNum.getGlyph(), 40, 20, aNum.getColor());
        terminal.write(bNum.getGlyph(), 41, 20, bNum.getColor());

        int c = world.monsterCnt / 10, d = world.monsterCnt % 10;
        Number cNum = new Number(Color.PINK, world, c), dNum = new Number(Color.PINK, world, d);
        printCharacter(terminal, "monster", 32, 15);
        terminal.write(cNum.getGlyph(), 40, 15, cNum.getColor());
        terminal.write(dNum.getGlyph(), 41, 15, dNum.getColor());

        if (world.state == false) {
            printCharacter(terminal, "fail", 32, 10);
        }

        judgeFinish();
        judgeSucceed();
        if (world.ifSucceed == true) {
            printCharacter(terminal, "succeed", 32, 10);
        } else if (world.ifFinish)
            printCharacter(terminal, "fail", 32, 10);
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        if (world.ifBegin == 0) {
            printCharacter(terminal, "please", 1, 1);
            printCharacter(terminal, "press", 8, 1);
            printCharacter(terminal, "n", 14, 1);
            printCharacter(terminal, "to", 16, 1);
            printCharacter(terminal, "begin", 19, 1);
            printCharacter(terminal, "a", 25, 1);
            printCharacter(terminal, "new", 27, 1);
            printCharacter(terminal, "game", 31, 1);
            printCharacter(terminal, "or", 36, 1);
            printCharacter(terminal, "c", 39, 1);
            printCharacter(terminal, "to", 41, 1);
            printCharacter(terminal, "continue", 1, 3);
        } else {
            displayBegin(terminal);
        }
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent e) {
        if (world.ifBegin == 0) {
            // new game "N"
            // continue "c"
            switch (e.getKeyCode()) {
                case 78:
                    startThread();
                    break;
                case 67:

                    try {
                        continueThread();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        } else {
            int dir = 0;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    dir = 0;
                    break;
                case KeyEvent.VK_DOWN:
                    dir = 1;
                    break;
                case KeyEvent.VK_LEFT:
                    dir = 2;
                    break;
                case KeyEvent.VK_RIGHT:
                    dir = 3;
                    break;
                case 83:
                    try {
                        mySave.saveRecord();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
            if (e.getKeyCode() != 83) {
                world.player.direction = dir;
                world.player.setKey();
            }
        }
        return this;
    }

}
