package org.example;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;


/* Flèche gauche / droite : tourner. Espace : tirer.*/

public class KeyboardInputHandler implements EventHandler<KeyEvent> {

    private final GameController controller;

    public KeyboardInputHandler(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT -> controller.moveLeft();
            case RIGHT -> controller.moveRight();
            case SPACE -> controller.shoot();
            default -> { } 
        }
    }
}