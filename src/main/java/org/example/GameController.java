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
    
    // Fait tourner le joueur vers la gauche (angle négatif)
    public void moveLeft() {
        engine.getPlayer().move(-ROTATION_STEP);
    }

    // Fait tourner le joueur vers la droite (angle positif)
    public void moveRight() {
        engine.getPlayer().move(ROTATION_STEP);
    }
    
    // Déclenche un tir du joueur
    public void shoot() {
        engine.playerShoot();
    }
}
