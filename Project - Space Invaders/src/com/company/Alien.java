package com.company;

public class Alien extends Character{

    boolean moveRight;
    boolean moveLeft;
    boolean visible;
    boolean dying;

    public Alien(int x, int y, int speed) {
        super(x, y, speed);

        moveLeft = false;
        moveRight = true;
        visible = true;
    }

    public boolean isVisible() {

        return visible;
    }

    protected void setVisible(boolean visible) {

        this.visible = visible;
    }
    public void setDying(boolean dying) {

        this.dying = dying;
    }

    public boolean isDying() {

        return this.dying;
    }

    public void die(){
        visible = false;
    }

}