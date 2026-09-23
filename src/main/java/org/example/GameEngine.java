package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** tick() sera ajouté par Wassim). */

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
}