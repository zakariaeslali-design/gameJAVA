package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private static final double DEUX_PI = 2 * Math.PI;

    @Test
    void deplacementPositifAugmenteLAngle() {
        Player p = new Player(0.0, 3);
        p.move(0.5);
        assertEquals(0.5, p.getPosition().getAngle(), 1e-9);
    }

    @Test
    void deplacementNegatifResteDansLIntervalle() {
        Player p = new Player(0.0, 3);
        p.move(-0.5);
        double a = p.getPosition().getAngle();
        assertTrue(a >= 0 && a < DEUX_PI);
        assertEquals(DEUX_PI - 0.5, a, 1e-9);
    }

    @Test
    void depassementDeDeuxPiRepartDeZero() {
        Player p = new Player(0.0, 3);
        p.move(DEUX_PI + 0.3);
        assertEquals(0.3, p.getPosition().getAngle(), 1e-9);
    }

    @Test
    void beaucoupDeDeplacementsGardentLAngleValide() {
        Player p = new Player(0.0, 3);
        for (int i = 0; i < 1000; i++) {
            p.move(-0.37);
            double a = p.getPosition().getAngle();
            assertTrue(a >= 0 && a < DEUX_PI, "angle hors intervalle : " + a);
        }
    }
}