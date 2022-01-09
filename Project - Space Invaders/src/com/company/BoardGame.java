package com.company;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class BoardGame extends JPanel implements Runnable, MouseListener {

    private String finalScore = null;
    private int score = 0;
    private int level;
    private int deaths;

    private boolean ingame = false;
    private boolean restartGame = false;
    private boolean help = false;
    private boolean christmas = true;
    private boolean summer = false;
    private boolean galaxy = false;

    private final Dimension d;
    private final int BOARD_WIDTH = 500;
    private final int BOARD_HEIGHT = 500;

    private Thread animator;
    private final Player p;
    private Bullet b;
    private final Alien[] a = new Alien[10];

    private final String message1 = "Winner, winner, chicken dinner!";
    private final String message2 = "Looooser!";
    private final String restartMessage1 = "Press 'R' to restart the game";
    private final String restartMessage2 = "Press 'M' to go back to the Menu";

    private final Rectangle easyButton = new Rectangle(BOARD_WIDTH / 2 - 70, 150, 150, 50);
    private final Rectangle mediumButton = new Rectangle(BOARD_WIDTH / 2 - 70, 210, 150, 50);
    private final Rectangle hardButton = new Rectangle(BOARD_WIDTH / 2 - 70, 270, 150, 50);
    private final Rectangle helpButton = new Rectangle(BOARD_WIDTH / 2 - 70, 330, 150, 50);

    public BoardGame() {

        addKeyListener(new TAdapter());
        addMouseListener(this);
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);
        //creating the player
        p = new Player(BOARD_WIDTH / 2, BOARD_HEIGHT - 60, 3);
        //creating the bullet
        b = new Bullet(p.x + 6, p.y - 21, 3);
        //creating the aliens
        int ax = 10;
        int ay = 10;
        for (int i = 0; i < a.length; i++) {
            a[i] = new Alien(ax, ay, 10);
            ax += 40;
            if (i == 4) {
                ax = 10;
                ay += 40;
            }
        }

        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }

    }

    public void paint(Graphics g) {
        //creating the HELP window
        if (help) {
            g.setColor(Color.black);
            g.fillRect(0, 0, d.width, d.height);

            var small3 = new Font("Helvetica", Font.BOLD, 20);
            var fontMetrics3 = this.getFontMetrics(small3);

            g.setColor(Color.green);
            g.setFont(small3);
            g.drawString("How to play?", BOARD_WIDTH / 2 - fontMetrics3.stringWidth("How to play?") / 2, 100);
            g.drawString("Controls: →, ← ", BOARD_WIDTH / 2 - fontMetrics3.stringWidth("Controls: →, ← ") / 2, 150);
            g.drawString("Shoot: SPACEBAR", BOARD_WIDTH / 2 - fontMetrics3.stringWidth("Controls: →, ← ") / 2, 180);
            g.drawString("Themes:", BOARD_WIDTH / 2 - fontMetrics3.stringWidth("How to play?") / 2, 230);
            g.drawString("Christmas: 1", BOARD_WIDTH / 2 - fontMetrics3.stringWidth("Controls: →, ← ") / 2, 280);
            g.drawString("Galaxy: 2", BOARD_WIDTH / 2 - fontMetrics3.stringWidth("Controls: →, ← ") / 2, 310);
            g.drawString("Summer: 3", BOARD_WIDTH / 2 - fontMetrics3.stringWidth("Controls: →, ← ") / 2, 340);
            g.drawString(restartMessage2, BOARD_WIDTH / 2 - fontMetrics3.stringWidth(restartMessage2) / 2, 400);
        }
        //creating the MENU window
        if (!restartGame) {

            Graphics2D g2d = (Graphics2D) g;
            g.setColor(Color.black);
            g.fillRect(0, 0, d.width, d.height);

            var fontTitle = new Font("Helvetica", Font.BOLD, 30);
            var fontMetricsTitle = this.getFontMetrics(fontTitle);

            g.setFont(fontTitle);
            g.setColor(Color.green);
            g.drawString("SPACE INVADERS", BOARD_WIDTH / 2 - fontMetricsTitle.stringWidth("SPACE INVADERS") / 2, 100);

            g2d.draw(easyButton);
            g2d.drawString("EASY", easyButton.x + 35, easyButton.y + 35);

            g2d.draw(mediumButton);
            g2d.drawString("MEDIUM", mediumButton.x + 15, mediumButton.y + 35);

            g2d.draw(hardButton);
            g2d.drawString("HARD", hardButton.x + 35, hardButton.y + 35);

            g2d.draw(helpButton);
            g2d.drawString("HELP", helpButton.x + 35, helpButton.y + 35);
        }
        //creating the game window
        if (ingame) {

            super.paint(g);

            g.setColor(Color.black);
            g.fillRect(0, 0, d.width, d.height);

            //representing the player
            if (christmas) {
                g.setColor(Color.red);
                g.fillRect(p.x, p.y, 20, 20);
            }
            if (summer) {
                g.setColor(Color.yellow);
                g.fillRect(p.x, p.y, 20, 20);
            }
            if (galaxy) {
                g.setColor(Color.magenta);
                g.fillRect(p.x, p.y, 20, 20);
            }

            //representing the bullet
            if (b.isVisible()) {
                if (christmas) {
                    g.setColor(Color.GREEN);
                    g.fillRect(b.x, b.y, 10, 10);
                }
                if (summer) {
                    g.setColor(Color.red);
                    g.fillRect(b.x, b.y, 10, 10);
                }
                if (galaxy) {
                    g.setColor(Color.blue);
                    g.fillRect(b.x, b.y, 10, 10);
                }
            }
            //representing the movement of the player
            if (p.moveRight) {
                p.x += p.speed;
                //the bullet will follow the player
                if (!b.moveUp) {
                    b.x += p.speed;
                }
                //making sure the player doesn't fall out of the board
                if (p.x > BOARD_WIDTH - 40) {
                    p.moveLeft = true;
                    p.moveRight = false;
                }
                //making sure the bullet doesn't fall out of the board
                if (b.x > BOARD_WIDTH - 40) {
                    b.moveLeft = true;
                    b.moveRight = false;
                }
            }
            //the same principle as above
            if (p.moveLeft) {
                p.x -= p.speed;
                if (!b.moveUp) {
                    b.x -= p.speed;
                }
                if (p.x < 0) {
                    p.moveRight = true;
                    p.moveLeft = false;
                }
                if (b.x < 0) {
                    b.moveRight = true;
                    b.moveLeft = false;
                }
            }
            //representing the movement of the bullet(upwards)
            if (b.moveUp) {
                b.y -= b.speed;
            }
            //representing the difficulty
            if (level == 0) {
                easy();
            } else if (level == 1) {
                medium();
            } else if (level == 2) {
                hard();
            }

            //representing the shot
            if (b.isVisible()) {

                for (Alien alien : a) {

                    if (alien.isVisible() && b.isVisible()) {
                        //if the bullet hits the alien
                        if (b.x >= (alien.x) && b.x <= (alien.x + 30) && b.y >= (alien.y) && b.y <= (alien.y + 30)) {
                            alien.setDying(true);
                            b.die();
                            alien.die();
                            deaths++;
                            b.x = p.x + 6;
                            b.y = p.y - 21;
                            b.speed = 3;
                            b.visible = true;
                            b.moveUp = false;
                            score += 10;
                        }
                        //if the bullet goes out of the board
                        if (b.y < 0) {
                            b.die();
                            b.x = p.x + 6;
                            b.y = p.y - 21;
                            b.speed = 3;
                            b.visible = true;
                            b.moveUp = false;
                            score -= 5;
                        }
                    }
                }
            }

            //if the player wins the game
            if (deaths == 10) {
                g.setColor(Color.black);
                g.fillRect(0, 0, 600, 600);

                var small = new Font("Helvetica", Font.BOLD, 20);
                var fontMetrics = this.getFontMetrics(small);
                //displaying the message for the winner
                g.setColor(Color.green);
                g.setFont(small);
                g.drawString(message1, BOARD_WIDTH / 2 - fontMetrics.stringWidth(message1) / 2, BOARD_HEIGHT / 2 - 10);
                //displaying the score
                finalScore = String.valueOf(score);
                g.setColor(Color.green);
                g.setFont(small);
                g.drawString("SCORE: " + finalScore, BOARD_WIDTH / 2 - fontMetrics.stringWidth(finalScore + "SCORE: ") / 2, BOARD_HEIGHT / 2 + 20);

                var small2 = new Font("Helvetica", Font.BOLD, 15);
                var fontMetrics2 = this.getFontMetrics(small2);
                //displaying the restart message
                g.setColor(Color.green);
                g.setFont(small2);
                g.drawString(restartMessage1, BOARD_WIDTH / 2 - fontMetrics2.stringWidth(restartMessage1) / 2, BOARD_HEIGHT / 2 + 100);

                ingame = false;
                restartGame = true;
            }

            //if the player loses the game
            for (Alien alien : a) {
                if (deaths != 10) {
                    if (50 >= (alien.x) && 50 <= (alien.x + 30) && 400 >= (alien.y) && 400 <= (alien.y + 30)) {
                        for (Alien aliens : a) {
                            aliens.setVisible(false);
                        }
                        g.setColor(Color.black);
                        g.fillRect(0, 0, 600, 600);

                        var small = new Font("Helvetica", Font.BOLD, 20);
                        var fontMetrics = this.getFontMetrics(small);
                        //displaying the message for the loser
                        g.setColor(Color.green);
                        g.setFont(small);
                        g.drawString(message2, BOARD_WIDTH / 2 - fontMetrics.stringWidth(message2) / 2, BOARD_HEIGHT / 2 - 10);
                        //displaying the score
                        finalScore = String.valueOf(score);
                        g.setColor(Color.green);
                        g.setFont(small);
                        g.drawString("SCORE: " + finalScore, BOARD_WIDTH / 2 - fontMetrics.stringWidth(finalScore + "SCORE: ") / 2, BOARD_HEIGHT / 2 + 20);

                        var small2 = new Font("Helvetica", Font.BOLD, 15);
                        var fontMetrics2 = this.getFontMetrics(small2);
                        //displaying the restart message
                        g.setColor(Color.green);
                        g.setFont(small2);
                        g.drawString(restartMessage1, BOARD_WIDTH / 2 - fontMetrics2.stringWidth(restartMessage1) / 2, BOARD_HEIGHT / 2 + 100);

                        ingame = false;
                        restartGame = true;
                    }
                }
            }

            //representing the aliens
            for (Alien alien : a) {
                if (alien.isVisible()) {
                    if (christmas) {
                        g.setColor(Color.RED);
                        g.fillRect(alien.x, alien.y, 30, 30);
                    }
                    if (summer) {
                        g.setColor(Color.yellow);
                        g.fillRect(alien.x, alien.y, 30, 30);
                    }
                    if (galaxy) {
                        g.setColor(Color.magenta);
                        g.fillRect(alien.x, alien.y, 30, 30);
                    }
                }
            }
            g.dispose();
        }
    }

    //representing the easy level
    public void easy() {
        for (Alien alien : a) {
            if (alien.moveLeft) {
                alien.x -= 1;
            }
            if (alien.moveRight) {
                alien.x += 1;
            }
        }
        for (Alien alien : a) {
            if (alien.x > BOARD_WIDTH - 40) {
                for (Alien value : a) {
                    value.moveLeft = true;
                    value.moveRight = false;
                    value.y += 5;
                }
            }
            if (alien.x < 0) {
                for (Alien value : a) {
                    value.moveRight = true;
                    value.moveLeft = false;
                    value.y += 5;
                }
            }
        }
    }

    //representing the medium level
    public void medium() {
        for (Alien alien : a) {
            if (alien.moveLeft) {
                alien.x -= 2;
            }
            if (alien.moveRight) {
                alien.x += 2;
            }
        }
        for (Alien value : a) {
            if (value.x > BOARD_WIDTH - 40) {
                for (Alien alien : a) {
                    alien.moveLeft = true;
                    alien.moveRight = false;
                    alien.y += 7;
                }
            }
            if (value.x < 0) {
                for (Alien alien : a) {
                    alien.moveRight = true;
                    alien.moveLeft = false;
                    alien.y += 7;
                }
            }
        }
    }

    //representing the hard level
    public void hard() {
        for (Alien alien : a) {
            if (alien.moveLeft) {
                alien.x -= 3;
            }
            if (alien.moveRight) {
                alien.x += 3;
            }
        }
        for (Alien value : a) {
            if (value.x > BOARD_WIDTH - 40) {
                for (Alien alien : a) {
                    alien.moveLeft = true;
                    alien.moveRight = false;
                    alien.y += 10;
                }
            }
            if (value.x < 0) {
                for (Alien alien : a) {
                    alien.moveRight = true;
                    alien.moveLeft = false;
                    alien.y += 10;
                }
            }
        }
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            p.moveRight = false;
            p.moveLeft = false;

        }

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == 39) {
                p.moveRight = true;
            }
            if (key == 37) {
                p.moveLeft = true;
            }
            if (key == 32) {
                b.moveUp = true;
            }
            if (key == 82) {
                restartGame = false;
            }
            if (key == 77) {
                help = false;
                restartGame = false;
            }
            if (key == 49) {
                christmas = true;
                summer = false;
                galaxy = false;
            }
            if (key == 50) {
                christmas = false;
                galaxy = true;
                summer = false;
            }
            if (key == 51) {
                christmas = false;
                galaxy = false;
                summer = true;
            }
        }
    }

    //we use this method when we restart the game
    public void reset() {
        deaths = 0;
        ingame = true;
        score = 0;
        int ax = 10;
        int ay = 10;

        for (int i = 0; i < a.length; i++) {
            a[i] = new Alien(ax, ay, 10);
            ax += 40;
            if (i == 4) {
                ax = 10;
                ay += 40;
            }
        }

        b = new Bullet(p.x + 6, p.y - 21, 3);
        restartGame = true;
    }

    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        //easy button
        if (mouseX >= BOARD_WIDTH / 2 - 70 && mouseX <= BOARD_WIDTH / 2 + 80) {
            if (mouseY >= 150 && mouseY <= 200) {
                reset();
                level = 0;
            }
        }

        //medium button
        if (mouseX >= BOARD_WIDTH / 2 - 70 && mouseX <= BOARD_WIDTH / 2 + 80) {
            if (mouseY >= 210 && mouseY <= 260) {
                reset();
                level = 1;
            }
        }

        //hard button
        if (mouseX >= BOARD_WIDTH / 2 - 70 && mouseX <= BOARD_WIDTH / 2 + 80) {
            if (mouseY >= 270 && mouseY <= 320) {
                reset();
                level = 2;
            }
        }

        //help button
        if (mouseX >= 250 - 70 && mouseX <= 250 + 80) {
            if (mouseY >= 330 && mouseY <= 380) {
                ingame = false;
                restartGame = true;
                help = true;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void run() {

        int animationDelay = 5;
        long time = System.currentTimeMillis();
        while (true) {
            repaint();
            try {
                time += animationDelay;
                Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

         
  