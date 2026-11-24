package org.example;

public class AcceleratedMovement implements MovementStrategy {
    private double vitesse;
    private final double acceleration;

    public AcceleratedMovement(double vitesseInitiale, double acceleration) {
        this.vitesse = vitesseInitiale;
        this.acceleration = acceleration;
    }

    @Override
    public PolarPosition calculerDeplacement(double delta) {
        double deltaRayon = vitesse * delta;
        vitesse += acceleration * delta;
        return new PolarPosition(deltaRayon, 0.0);
    }
}