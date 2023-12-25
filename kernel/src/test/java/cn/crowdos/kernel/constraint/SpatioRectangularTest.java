package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpatioRectangularTest {

    SpatioRectangular constraint;
    {
        Coordinate leftTop = new Coordinate(0, 0);
        Coordinate rightBottom = new Coordinate(10, 10);
        try {
            constraint = new SpatioRectangular(leftTop, rightBottom);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test()
    void newException(){
        assertThrows(
                InvalidConstraintException.class,
                () -> new SpatioRectangular(new Coordinate(0, 0), new Coordinate(0, 100))
        );
    }

    @Test
    void satisfy() {
        Coordinate c1 = new Coordinate(0, 0);
        assertTrue(constraint.satisfy(c1));
        Coordinate c2 = new Coordinate(10, 10);
        assertFalse(constraint.satisfy(c2));
        Coordinate c3 = new Coordinate(5, 5);
        assertTrue(constraint.satisfy(c3));
        Coordinate c4 = new Coordinate(20, 20);
        assertFalse(constraint.satisfy(c4));
    }

    @Test
    void getConditionClass() {
        String name = constraint.getConditionClass().getName();
        assertEquals(name, Coordinate.class.getName());
    }

    @Test
    void decomposer() {
        Decomposer<Constraint> decomposer = constraint.decomposer();
        try {
            for (Constraint sub : decomposer.decompose(100)) {
                System.out.println(sub);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }
}