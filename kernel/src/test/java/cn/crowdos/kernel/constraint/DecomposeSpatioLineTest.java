package cn.crowdos.kernel.constraint;

import java.util.ArrayList;
import java.util.List;

public class DecomposeSpatioLineTest {
    private DecomposeSpatioLine decomposeSpatioLine;
    private double width = 8;

    public DecomposeSpatioLineTest() {
        List<Coordinate> pointList = new ArrayList<>();
        pointList.add(new Coordinate(0, 1));
        pointList.add(new Coordinate(1, 3));
        pointList.add(new Coordinate(2, 4));
        pointList.add(new Coordinate(4, 2));
        pointList.add(new Coordinate(2, 0));
        try {
            decomposeSpatioLine = new DecomposeSpatioLine(width, pointList);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    public void testException() {
        List<Coordinate> pointList = new ArrayList<>();
        pointList.add(new Coordinate(0, 1));
        pointList.add(new Coordinate(1, 3));
        pointList.add(new Coordinate(2, 4));
        try {
            new DecomposeSpatioLine(-12, pointList);
            System.out.println("testException failed: No InvalidConstraintException was thrown.");
        } catch (InvalidConstraintException e) {
            System.out.println("testException passed: InvalidConstraintException was thrown as expected.");
        }
    }

    public void decomposePointslistToLines() {
        try {
            List<SpatioLine> decomposeLines = decomposeSpatioLine.DecomposePointslistToLines();
            for (SpatioLine line : decomposeLines) {
                System.out.println(line);
            }
        } catch (InvalidConstraintException e) {
            System.out.println("decomposePointslistToLines failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DecomposeSpatioLineTest test = new DecomposeSpatioLineTest();
        test.testException();
        test.decomposePointslistToLines();
    }
}