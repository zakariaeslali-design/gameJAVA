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
    private WeaponType weapon = WeaponType.SIMPLE;
    static final double COLLISION_THRESHOLD = 15.0;
    static final double ENEMY_SPEED = 10.0;
    static final double PROJECTILE_SPEED = 5.0;
    static final double SPAWN_INTERVAL = 2.0;
    // Ecart d'angle entre les 3 projectiles du tir triple (15 degres)
    static final double TRIPLE_SHOT_SPREAD = Math.PI / 12;

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

    public WeaponType getWeapon() {
        return weapon;
    }

    // Passe a l'arme suivante (SIMPLE -> TRIPLE -> SIMPLE ...)
    public void switchWeapon() {
        WeaponType[] weapons = WeaponType.values();
        weapon = weapons[(weapon.ordinal() + 1) % weapons.length];
    }

    // Tire avec l'arme choisie : 1 projectile (SIMPLE) ou 3 projectiles (TRIPLE)
    public void playerShoot() {
        if (gameOver) {
            return;
        }

        PolarPosition playerPos = player.getPosition();
        switch (weapon) {
            case SIMPLE -> spawnProjectile(playerPos.angle());
            case TRIPLE -> {
                spawnProjectile(playerPos.angle() - TRIPLE_SHOT_SPREAD);
                spawnProjectile(playerPos.angle());
                spawnProjectile(playerPos.angle() + TRIPLE_SHOT_SPREAD);
            }
        }
    }

    // Cree un projectile qui part de l'orbite du joueur avec l'angle donne
    private void spawnProjectile(double angle) {
        entities.add(new Projectile(Player.ORBIT_RADIUS, angle, PROJECTILE_SPEED));
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
                if (d <= enemy.getCollisionRadius()) {
                    projectile.deactivate();
                    enemy.takeDamage();
                    if (!enemy.isAlive()) {
                        addScore(enemy.getScorePoints());
                    }
                    break;
                }
            }
        }
    }

    // Crée un ennemi au centre du cercle
    void spawnEnemy(double angle) {
        EnemyType[] types = EnemyType.values();
        EnemyType type = types[random.nextInt(types.length)];
        entities.add(EnemyFactory.creerEnemy(type, angle));
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