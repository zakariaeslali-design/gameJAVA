package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameEngine {

    private final Player player;
    private final List<GameEntity> entities;
    private boolean gameOver;

    public GameEngine(Player player) {
        this.player = player;
        this.entities = new ArrayList<>();
        this.entities.add(player);
        this.gameOver = false;
    }

    public Player getPlayer() {
        return player;
    }

    public List<GameEntity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    public void addEntity(GameEntity entity) {
        entities.add(entity);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public void tick(double delta) {
        if (gameOver) {
            return;
        }

        for (GameEntity entity : entities) {
            entity.update();
        }
    }
    
     // Crée un projectile à la position du joueur, qui partira vers le centre.
    public void playerShoot() {
        if (gameOver) {
            return;
        }

        PolarPosition playerPos = player.getPosition();
        Projectile projectile = new Projectile(
                playerPos.rayon(),
                playerPos.angle(),
                15.0); // vitesse du projectile, même valeur que le test sprint 1

        entities.add(projectile);
    }
}