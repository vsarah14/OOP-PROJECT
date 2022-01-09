package com.company;

public class Player extends Character{

    boolean moveRight;
    boolean moveLeft;

    public Player(int x, int y, int speed) {
        super(x, y, speed);

        moveLeft = false;
        moveRight = false;
    }


}
