package org.example;


public sealed interface GameEntity permits Player, Enemy, Projectile {

    PolarPosition getPosition();

    void update();

    boolean isAlive();
}