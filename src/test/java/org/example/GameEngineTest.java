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
}