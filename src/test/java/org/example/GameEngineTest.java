package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GameEngineTest {

    @Test
    void scoreStartsAtZero() {
        GameEngine engine = new GameEngine(new Player(0.0, 3));
        assertEquals(0, engine.getScore());
    }

    @Test
    void addScoreAccumulatesPoints() {
        GameEngine engine = new GameEngine(new Player(0.0, 3));
        engine.addScore(1);
        engine.addScore(2);
        assertEquals(3, engine.getScore());
    }

    @Test
    void damagePlayerRemovesOneLife() {
        Player player = new Player(0.0, 3);
        GameEngine engine = new GameEngine(player);

        engine.damagePlayer();

        assertEquals(2, player.getHealth());
        assertFalse(engine.isGameOver());
    }

    @Test
    void gameOverWhenLastLifeIsLost() {
        Player player = new Player(0.0, 1);
        GameEngine engine = new GameEngine(player);

        engine.damagePlayer();

        assertEquals(0, player.getHealth());
        assertTrue(engine.isGameOver());
    }

    @Test
    void noMoreDamageAfterGameOver() {
        Player player = new Player(0.0, 1);
        GameEngine engine = new GameEngine(player);
        engine.damagePlayer(); // game over

        engine.damagePlayer();

        assertEquals(0, player.getHealth()); // la vie ne passe pas à -1
    }

    @Test
    void tickDoesNothingAfterGameOver() {
        Player player = new Player(0.0, 1);
        GameEngine engine = new GameEngine(player);
        Enemy enemy = new Enemy(0.0, 1.0, 10.0);
        engine.addEntity(enemy);
        engine.damagePlayer(); // game over

        engine.tick(0.1);

        assertEquals(0.0, enemy.getPosition().rayon(), 1e-9); // l'ennemi n'a pas bougé
    }


    @Test
    void checkCollisionsHitsWhenDistanceWithinThreshold() {
        GameEngine engine = new GameEngine(new Player(0.0, 3));
        Enemy enemy = new Enemy(50.0, 0.0, 10.0);
        Projectile projectile = new Projectile(60.0, 0.0, 15.0); // distance = 10 <= 15.0
        engine.addEntity(enemy);
        engine.addEntity(projectile);

        engine.checkCollisions();

        assertFalse(enemy.isAlive(), "L'ennemi doit être marqué mort après collision");
        assertFalse(projectile.isAlive(), "Le projectile doit être désactivé après collision");
        assertEquals(1, engine.getScore(), "Le score doit augmenter de 1");
    }

    @Test
    void checkCollisionsDoesNotHitWhenTooFar() {
        GameEngine engine = new GameEngine(new Player(0.0, 3));
        Enemy enemy = new Enemy(20.0, 0.0, 10.0);
        Projectile projectile = new Projectile(80.0, 0.0, 15.0); // distance = 60 > 15.0
        engine.addEntity(enemy);
        engine.addEntity(projectile);

        engine.checkCollisions();

        assertTrue(enemy.isAlive());
        assertTrue(projectile.isAlive());
        assertEquals(0, engine.getScore());
    }

    @Test
    void collisionDetectedWhenEnemyAndProjectileCrossBetweenTwoTicks() {
        GameEngine engine = new GameEngine(new Player(0.0, 3));
        Enemy enemy = new Enemy(40.0, 0.0, 10.0);
        Projectile projectile = new Projectile(56.0, 0.0, 15.0);
        engine.addEntity(enemy);
        engine.addEntity(projectile);

        engine.tick(0.1);

        assertFalse(enemy.isAlive());
        assertFalse(projectile.isAlive());
        assertEquals(1, engine.getScore());
        assertEquals(1, engine.getEntities().size(), "Seul le joueur doit rester après nettoyage");
    }

    @Test
    void cleanupEntitiesRemovesDeadEnemyAndInactiveProjectileAtCenter() {
        GameEngine engine = new GameEngine(new Player(0.0, 3));
        Enemy deadEnemy = new Enemy(30.0, 1.0, 5.0);
        deadEnemy.die();
        Projectile projectileAtCenter = new Projectile(10.0, 2.0, 15.0);
        projectileAtCenter.update(); // rayon passe à 0 -> devient inactif

        engine.addEntity(deadEnemy);
        engine.addEntity(projectileAtCenter);

        engine.cleanupEntities();

        assertEquals(1, engine.getEntities().size());
        assertSame(engine.getPlayer(), engine.getEntities().getFirst());
    }

    @Test
    void playerUsesSharedOrbitRadiusConstant() {
        Player player = new Player(0.0, 3);
        assertEquals(Player.ORBIT_RADIUS, player.getPosition().rayon(), 1e-9);
        assertTrue(Player.ORBIT_RADIUS > 100.0, "L'orbite doit être agrandie par rapport aux 100px initiaux");
    }

    @Test
    void hudFormatsScoreHealthAndGameOverCorrectly() {
        assertEquals("Score : 4 | Vies : 2", GraphicalView.formatHudText(4, 2, false));
        assertEquals("Score : 10 | Vies : 0 [GAME OVER]", GraphicalView.formatHudText(10, 0, true));
    }
}