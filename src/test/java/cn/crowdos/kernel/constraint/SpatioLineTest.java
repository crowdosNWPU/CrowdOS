package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpatioLineTest {

    SpatioLine spatioLine;
    {
        Coordinate startPoint = new Coordinate(2,3);
//        Coordinate endPoint = new Coordinate(15,3);
//        Coordinate endPoint = new Coordinate(2,15);
        Coordinate endPoint = new Coordinate(15,15);
        double width = 8;
        try{
            spatioLine = new SpatioLine(startPoint,endPoint,width);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testException(){
        assertThrows(
                InvalidConstraintException.class,
                () -> new SpatioLine(new Coordinate(0,0),new Coordinate(10,10),-12)
        );
    }

    @Test
    void decomposer(){
        Decomposer<Constraint> decomposer = spatioLine.decomposer();
        try{
            for (Constraint subconstraint : decomposer.scaleDecompose(5)){
                System.out.println(subconstraint);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void satisfy(){
        Coordinate coordinate1 = new Coordinate(13,17);
        Coordinate coordinate2 = new Coordinate(10,1);
        assertTrue(spatioLine.satisfy(coordinate1));
        assertFalse(spatioLine.satisfy(coordinate2));
    }

    @Test
    void getConditionClass(){
        String name = spatioLine.getConditionClass().getName();
        assertEquals(name, Coordinate.class.getName());
    }
}
