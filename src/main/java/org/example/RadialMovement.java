package org.example;

public class RadialMovement implements MovementStrategy {
    private final double vitesse;

    public RadialMovement(double vitesse) {
        this.vitesse = vitesse;
    }

    @Override
    public PolarPosition calculerDeplacement(double delta) {
        return new PolarPosition(vitesse * delta, 0.0);
    }
}