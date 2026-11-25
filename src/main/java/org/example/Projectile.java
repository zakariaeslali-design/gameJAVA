package org.example;

public final class Projectile implements GameEntity {
    private PolarPosition position;
    private final double startRadius;
    private final double targetAngle;
    private final double angleOffset;
    private final double speed;
    private boolean active;

    public Projectile(double startRadius, double angle, double speed) {
        this(startRadius, angle, speed, 0.0);
    }

    /**
     * Projectile a trajectoire courbe : il part de la position du joueur
     * (startRadius, targetAngle), s'ecarte lateralement jusqu'a angleOffset a
     * mi-course, puis revient sur targetAngle au centre.
     */
    public Projectile(double startRadius, double targetAngle, double speed, double angleOffset) {
        this.startRadius = startRadius;
        this.targetAngle = targetAngle;
        this.angleOffset = angleOffset;
        this.position = new PolarPosition(startRadius, targetAngle);
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
        this.position = new PolarPosition(newRadius, angleAt(newRadius));
    }

    /**
     * L'ecart lateral vaut 0 a la sortie du joueur (r = startRadius) et au centre
     * (r = 0) : le projectile part donc du joueur, s'ecarte en courbe puis converge.
     */
    private double angleAt(double radius) {
        if (angleOffset == 0.0 || startRadius <= 0) {
            return targetAngle;
        }
        double ratio = radius / startRadius;
        return targetAngle + angleOffset * Math.sin(Math.PI * (1.0 - ratio));
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
