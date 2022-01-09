package com.company;

public class Bullet extends Character{

    boolean moveUp;
    boolean moveRight;
    boolean moveLeft;
    boolean visible;
    boolean dying;

    public Bullet(int x, int y, int speed) {
        super(x, y, speed);

        moveUp = false;
        moveRight = false;
        moveLeft = false;
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
