package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EnemyCollisionRadiusTest {

    // Ennemi immobile au rayon 50, projectile au rayon 75 sur le même angle : distance = 25
    @Test
    void bigCollisionRadiusHitsAtDistance25() {
        GameEngine engine = new GameEngine(new Player(0.0, 3));
        Enemy bigEnemy = new Enemy(50.0, 0.0, new RadialMovement(0.0), 30.0);
        Projectile projectile = new Projectile(75.0, 0.0, 15.0);
        engine.addEntity(bigEnemy);
        engine.addEntity(projectile);

        engine.checkCollisions();

        assertFalse(bigEnemy.isAlive());
        assertFalse(projectile.isAlive());
    }

    @Test
    void defaultCollisionRadiusMissesAtDistance25() {
        GameEngine engine = new GameEngine(new Player(0.0, 3));
        Enemy normalEnemy = new Enemy(50.0, 0.0, 0.0);
        Projectile projectile = new Projectile(75.0, 0.0, 15.0);
        engine.addEntity(normalEnemy);
        engine.addEntity(projectile);

        engine.checkCollisions();

        assertTrue(normalEnemy.isAlive());
        assertTrue(projectile.isAlive());
    }
}