package org.example;

public non-sealed class Enemy implements GameEntity{



    @Override
    public PolarPosition getPosition() {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isAlive() {
        return false;
    }
}
