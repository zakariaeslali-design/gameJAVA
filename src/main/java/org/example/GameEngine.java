package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameEngine {

    private final Player player;
    private final List<GameEntity> entities;
    private boolean gameOver;
    static final double COLLISION_THRESHOLD = 15.0;

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
        checkCollisions();
        
        cleanupEntities();
        
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
    void checkCollisions() {
        for (GameEntity a : entities) {
            if (!(a instanceof Projectile projectile) || !projectile.isAlive()) {
                continue;
            }
            for (GameEntity b : entities) {
                if (!(b instanceof Enemy enemy) || !enemy.isAlive()) {
                    continue;
                }
                double d = projectile.getPosition().distanceTo(enemy.getPosition());
                if (d <= COLLISION_THRESHOLD) {
                    projectile.deactivate();
                    enemy.die();
                    // score++ : à ajouter quand Salah aura créé le score
                    break;
                }
            }
        }
    }
    
    void cleanupEntities() {
        entities.removeIf(e -> !(e instanceof Player) && !e.isAlive());
    }
}