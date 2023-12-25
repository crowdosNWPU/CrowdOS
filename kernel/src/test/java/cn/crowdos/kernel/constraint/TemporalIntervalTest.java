package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class TemporalIntervalTest {

    TemporalInterval temporalInterval;

    @BeforeEach
    void setUp() throws InvalidConstraintException {
        temporalInterval = new TemporalInterval("10:00", "11:00");
    }

    @Test
    void testCreate() {
        assertThrows(InvalidConstraintException.class, () -> {new TemporalInterval("11:00:00", "09:00:00");});
    }

    @Test
    void decomposer() throws DecomposeException {
        Decomposer<Constraint> decomposer = temporalInterval.decomposer();
        System.out.println(decomposer.trivialDecompose());
        System.out.println(decomposer.scaleDecompose(4));
    }

    @Test
    void satisfy() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("hh:mm");
        DateCondition time1;
        DateCondition time2;
        time1 = new DateCondition(sf.parse("10:30").getTime());
        time2 = new DateCondition(sf.parse("11:30").getTime());
        assertTrue(temporalInterval.satisfy(time1));
        assertFalse(temporalInterval.satisfy(time2));
    }

    @Test
    void getConditionClass() {
        assertSame(temporalInterval.getConditionClass(), DateCondition.class);
    }
}