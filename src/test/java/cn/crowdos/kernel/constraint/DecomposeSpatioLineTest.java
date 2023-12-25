package cn.crowdos.kernel.constraint;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DecomposeSpatioLineTest {
    DecomposeSpatioLine decomposeSpatioLine;
    double width = 8;
    {
        List<Coordinate>pointList = new ArrayList<>();
        pointList.add(new Coordinate(0,1));
        pointList.add(new Coordinate(1,3));
        pointList.add(new Coordinate(2,4));
        pointList.add(new Coordinate(4,2));
        pointList.add(new Coordinate(2,0));
        try{
            decomposeSpatioLine = new DecomposeSpatioLine(width,pointList);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testException(){
        List<Coordinate>pointList = new ArrayList<>();
        pointList.add(new Coordinate(0,1));
        pointList.add(new Coordinate(1,3));
        pointList.add(new Coordinate(2,4));
        assertThrows(
                InvalidConstraintException.class,
                () -> new DecomposeSpatioLine(-12,pointList)
        );
    }

    @Test
    void DecomposePointslistToLines() throws InvalidConstraintException {
        List<SpatioLine> decomposeLines = decomposeSpatioLine.DecomposePointslistToLines();
        for(SpatioLine line:decomposeLines){
            System.out.println(line);
        }
    }
}
