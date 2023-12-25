package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class TemporalPointTest {

    TemporalPoint temporalPoint;

    @BeforeEach
    void setUp() {
        temporalPoint = new TemporalPoint("10:00:00", 10);
    }

    @Test
    void decomposer() throws DecomposeException {
        Decomposer<Constraint> decomposer = temporalPoint.decomposer();
        System.out.println(decomposer.trivialDecompose());
        System.out.println(decomposer.scaleDecompose(10));
    }

    @Test
    void satisfy() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("hh:mm");
        DateCondition time1 = new DateCondition(sf.parse("10:30").getTime());
        DateCondition time2 = new DateCondition(sf.parse("10:05").getTime());
        assertFalse(temporalPoint.satisfy(time1));
        assertTrue(temporalPoint.satisfy(time2));
    }

    @Test
    void getConditionClass() {
        assertSame(temporalPoint.getConditionClass(), DateCondition.class);
    }
}