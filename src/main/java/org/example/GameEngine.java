package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class GameEngine {

    private int score;
    private final Player player;
    private final List<GameEntity> entities;
    private boolean gameOver;
    static final double COLLISION_THRESHOLD = 15.0;
    static final double ENEMY_SPEED = 10.0;
    static final double SPAWN_INTERVAL = 2.0;

    private final Random random;
    private double spawnTimer;

    public GameEngine(Player player) {
        this(player, new Random());
    }

    public GameEngine(Player player, Random random) {
        this.player = player;
        this.random = random;
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

    public int getScore() {
        return score;
    }

    public void damagePlayer() {
        if (gameOver) {
            return;
        }
        player.takeDamage();
        if (!player.isAlive()) {
            gameOver = true;
        }
    }

    public void addScore(int points) {
        score += points;
    }

    public void tick(double delta) {
        if (gameOver) {
            return;
        }

        for (GameEntity entity : entities) {
            entity.update();
        }

        // Toutes les 2 secondes, un nouvel ennemi apparaît
        spawnTimer += delta;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnEnemy(random.nextDouble() * 2 * Math.PI);
            spawnTimer -= SPAWN_INTERVAL;
        }

        checkCollisions();
        checkEscapes();
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
                    addScore(1);
                    break;
                }
            }
        }
    }

    // Crée un ennemi au centre du cercle
    void spawnEnemy(double angle) {
        entities.add(new Enemy(0.0, angle, ENEMY_SPEED));
    }

    // Si un ennemi atteint le bord du cercle : il meurt et le joueur perd une vie
    void checkEscapes() {
        for (GameEntity e : entities) {
            if (e instanceof Enemy enemy
                    && enemy.isAlive()
                    && enemy.getPosition().getRayon() >= Player.ORBIT_RADIUS) {
                enemy.die();
                damagePlayer();
            }
        }
    }

    void cleanupEntities() {
        entities.removeIf(e -> !(e instanceof Player) && !e.isAlive());
    }
}