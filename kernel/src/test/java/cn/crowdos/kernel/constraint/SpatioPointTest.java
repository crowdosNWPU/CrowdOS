package cn.crowdos.kernel.constraint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpatioPointTest {
    SpatioPoint spatioPoint;
    {
        try{
            spatioPoint = new SpatioPoint(new Coordinate(55.764,59.253),10);
        }catch (InvalidConstraintException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testException(){
        assertThrows(
                InvalidConstraintException.class,
                ()->new SpatioPoint(new Coordinate(55.764,69.253),-10)
        );
    }

    @Test
    void satisfy(){
        Coordinate coordinate1 = new Coordinate(57.563,60.125);
        assertTrue(spatioPoint.satisfy(coordinate1));
        Coordinate coordinate2 = new Coordinate(12.589,0.265);
        assertFalse(spatioPoint.satisfy(coordinate2));
    }

    @Test
    void getConditionClass(){
        String name = spatioPoint.getConditionClass().getName();
        assertEquals(name, Coordinate.class.getName());
    }
}
