package org.example;

public class SinuousMovement implements MovementStrategy {
    private final double vitesse;
    private final double amplitude;
    private final double frequence;
    private double temps;

    public SinuousMovement(double vitesse, double amplitude, double frequence) {
        this.vitesse = vitesse;
        this.amplitude = amplitude;
        this.frequence = frequence;
    }

    @Override
    public PolarPosition calculerDeplacement(double delta) {
        double deltaAngle = amplitude * Math.cos(frequence * temps) * delta;
        temps += delta;
        return new PolarPosition(vitesse * delta, deltaAngle);
    }
}