package org.example;

/**
 * Reçoit les commandes du joueur (bouger, tirer) et les transmet au moteur.
 */

public class GameController {

    private static final double ROTATION_STEP = Math.PI / 16; // pas de rotation par appui

    private final GameEngine engine;

    public GameController(GameEngine engine) {
        this.engine = engine;
    }
}
