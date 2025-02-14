package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;

public class SpatioRectangularTest {

    SpatioRectangular constraint;

    public SpatioRectangularTest() {
        Coordinate leftTop = new Coordinate(0, 0);
        Coordinate rightBottom = new Coordinate(10, 10);
        try {
            constraint = new SpatioRectangular(leftTop, rightBottom);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    public void testNewException() {
        try {
            new SpatioRectangular(new Coordinate(0, 0), new Coordinate(0, 100));
            System.out.println("newException test failed");
        } catch (InvalidConstraintException e) {
            System.out.println("newException test passed");
        }
    }

    public void testSatisfy() {
        Coordinate c1 = new Coordinate(0, 0);
        if (constraint.satisfy(c1)) {
            System.out.println("satisfy test for c1 passed");
        } else {
            System.out.println("satisfy test for c1 failed");
        }

        Coordinate c2 = new Coordinate(10, 10);
        if (!constraint.satisfy(c2)) {
            System.out.println("satisfy test for c2 passed");
        } else {
            System.out.println("satisfy test for c2 failed");
        }

        Coordinate c3 = new Coordinate(5, 5);
        if (constraint.satisfy(c3)) {
            System.out.println("satisfy test for c3 passed");
        } else {
            System.out.println("satisfy test for c3 failed");
        }

        Coordinate c4 = new Coordinate(20, 20);
        if (!constraint.satisfy(c4)) {
            System.out.println("satisfy test for c4 passed");
        } else {
            System.out.println("satisfy test for c4 failed");
        }
    }

    public void testGetConditionClass() {
        String name = constraint.getConditionClass().getName();
        if (name.equals(Coordinate.class.getName())) {
            System.out.println("getConditionClass test passed");
        } else {
            System.out.println("getConditionClass test failed");
        }
    }

    public void testDecomposer() {
        Decomposer<Constraint> decomposer = constraint.decomposer();
        try {
            for (Constraint sub : decomposer.decompose(100)) {
                System.out.println(sub);
            }
            System.out.println("decomposer test passed");
        } catch (DecomposeException e) {
            System.out.println("decomposer test failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpatioRectangularTest test = new SpatioRectangularTest();
        test.testNewException();
        test.testSatisfy();
        test.testGetConditionClass();
        test.testDecomposer();
    }
}