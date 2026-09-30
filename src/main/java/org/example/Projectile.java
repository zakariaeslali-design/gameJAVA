package org.example;

public final class Projectile implements GameEntity {
    private PolarPosition position;
    private final double speed;
    private boolean active;

    public Projectile(double startRadius, double angle, double speed) {
        this.position = new PolarPosition(startRadius, angle);
        this.speed = speed;
        this.active = true;
    }

    @Override
    public PolarPosition getPosition() {
        return position;
    }

    /** Avance vers le centre. Devient inactif quand il atteint le centre. */
    @Override
    public void update() {
        if (!active) {
            return;
        }

        double newRadius = position.getRayon() - speed;
        if (newRadius <= 0) {
            newRadius = 0;
            active = false;
        }
        this.position = new PolarPosition(newRadius, position.getAngle());
    }

    @Override
    public boolean isAlive() {
        return active;
    }

    /** Utilisé plus tard (sprint 3) quand le projectile touche un ennemi. */
    public void deactivate() {
        active = false;
    }
}
