package cn.crowdos.kernel.constraint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void inLine() {
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(0, 0);
        Coordinate c3 = new Coordinate(0, 1);
        Coordinate c4 = new Coordinate(1, 0);
        Coordinate c5 = new Coordinate(1, 1);
        assertTrue(c1.inLine(c2));
        assertTrue(c1.inLine(c3));
        assertTrue(c1.inLine(c4));
        assertFalse(c1.inLine(c5));
    }

    @Test
    void testToString() {
        Coordinate c1 = new Coordinate(0, 0);
        System.out.println(c1);
    }
}