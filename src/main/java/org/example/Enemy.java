package org.example;

public final class Enemy implements GameEntity{
    private PolarPosition position;
    private final double speed;
    private boolean alive;

    public Enemy(double spawnRadius, double initialAngle, double speed){
        this.position = new PolarPosition(spawnRadius, initialAngle);
        this.speed = speed;
        this.alive = true;
    }


    @Override
    public PolarPosition getPosition() {
        return position;
    }

    @Override
    public void update() {
        if (!alive){
            return;
        }

        double newRadius = position.getRayon() + speed;
        this.position = new PolarPosition(newRadius, position.getAngle());
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public void die(){
        alive = false;
    }
}
