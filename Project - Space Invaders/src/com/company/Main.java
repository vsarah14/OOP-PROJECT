package com.company;

import javax.swing.JFrame;

public class Main extends JFrame {                          //JFrame provides a window on the screen

    public Main()
    {
        add(new BoardGame());
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);           //the option for the close button -exit the application
        setSize(500,500);
        setLocationRelativeTo(null);                       //the window is centered on the screen
        setVisible(true);                                  //the window is displayed on the screen
        setResizable(false);                               //prevents the user from resizing the window
    }

    public static void main(String[] args) {
        new Main();
    }
}

