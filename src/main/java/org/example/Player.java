package org.example;

public final class Player implements GameEntity {
    public static final double ORBIT_RADIUS = 300.0;

    private PolarPosition position;
    private int health;

    public Player(double initialAngle, int health) {
        this.position = new PolarPosition(ORBIT_RADIUS, initialAngle);
        this.health = health;
    }

    @Override
    public PolarPosition getPosition() {
        return position;
    }

    @Override
    public void update() {
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    public void move(double angleDelta) {
        double newAngle = (position.getAngle() + angleDelta) % (2 * Math.PI);
        if (newAngle < 0) {
            newAngle += 2 * Math.PI;
        }
        position = new PolarPosition(ORBIT_RADIUS, newAngle);
    }

    public void takeDamage() {
        this.health--;
    }

    public int getHealth() {
        return health;
    }
}