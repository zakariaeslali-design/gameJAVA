package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MovementStrategyTest {
    private static final double TOLERANCE = 0.000000001;

    @Test
    void radialAvanceDeVitesseFoisDelta() {
        MovementStrategy s = new RadialMovement(10.0);
        PolarPosition d = s.calculerDeplacement(0.5);
        assertEquals(5.0, d.getRayon(), TOLERANCE);
        assertEquals(0.0, d.getAngle(), TOLERANCE);
    }

    @Test
    void sinuousPremierAppelPuisSecondAppel() {
        MovementStrategy s = new SinuousMovement(10.0, 0.1, Math.PI / 3);
        PolarPosition d1 = s.calculerDeplacement(1.0);
        assertEquals(10.0, d1.getRayon(), TOLERANCE);
        assertEquals(0.1, d1.getAngle(), TOLERANCE);
        PolarPosition d2 = s.calculerDeplacement(1.0);
        assertEquals(0.05, d2.getAngle(), TOLERANCE);
    }

    @Test
    void acceleratedAugmenteSaVitesse() {
        MovementStrategy s = new AcceleratedMovement(2.0, 1.0);
        assertEquals(2.0, s.calculerDeplacement(1.0).getRayon(), TOLERANCE);
        assertEquals(3.0, s.calculerDeplacement(1.0).getRayon(), TOLERANCE);
        assertEquals(4.0, s.calculerDeplacement(1.0).getRayon(), TOLERANCE);
    }
}