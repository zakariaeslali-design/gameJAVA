package org.example;

public final class Enemy implements GameEntity{
    private PolarPosition position;
    private final MovementStrategy strategy;
    private final double collisionRadius;
    private boolean alive;

    public Enemy(double spawnRadius, double initialAngle, MovementStrategy strategy, double collisionRadius) {
        this.position = new PolarPosition(spawnRadius, initialAngle);
        this.strategy = strategy;
        this.collisionRadius = collisionRadius;
        this.alive = true;
    }

    public Enemy(double spawnRadius, double initialAngle, MovementStrategy strategy) {
        this(spawnRadius, initialAngle, strategy, DEFAULT_COLLISION_RADIUS);
    }

    public Enemy(double spawnRadius, double initialAngle, double speed) {
        this(spawnRadius, initialAngle, new RadialMovement(speed));
    }

    public double getCollisionRadius() {
        return collisionRadius;

    @Override
    public PolarPosition getPosition() {
        return position;
    }

    @Override
    public void update() {
        if (!alive){
            return;
        }

        PolarPosition d = strategy.calculerDeplacement(1.0);
        position = new PolarPosition(position.getRayon() + d.getRayon(),
                position.getAngle() + d.getAngle());
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public void die(){
        alive = false;
    }
}
