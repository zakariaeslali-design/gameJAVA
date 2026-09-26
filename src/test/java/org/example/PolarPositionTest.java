package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PolarPositionTest {

    @Test
    void testToCartesianXAtAngleZero() {
        PolarPosition p = new PolarPosition(10, 0);
        assertEquals(10.0, p.toCartesianX(), 0.001);
        assertEquals(0.0, p.toCartesianY(), 0.001);
    }

    @Test
    void testToCartesianXAt90Degrees() {
        PolarPosition p = new PolarPosition(10, Math.PI / 2);
        assertEquals(0.0, p.toCartesianX(), 0.001);
        assertEquals(10.0, p.toCartesianY(), 0.001);
    }

    @Test
    void testDistanceTo() {
        PolarPosition p1 = new PolarPosition(10, 0);
        PolarPosition p2 = new PolarPosition(10, Math.PI / 2);
        double expected = 10 * Math.sqrt(2);
        assertEquals(expected, p1.distanceTo(p2), 0.001);
    }
}