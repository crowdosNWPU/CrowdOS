package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DateIntervalTest {

    DateInterval dateInterval;
    {
        try{
            dateInterval = new DateInterval("2023-04-01","2023-05-06");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testException(){
        assertThrows(
                InvalidConstraintException.class,
                () -> new DateInterval("2023-04-08","2023-04-01")
        );
        assertThrows(
                InvalidConstraintException.class,
                () -> new DateInterval(LocalDate.of(2023,4,18),LocalDate.of(2023,4,8))
        );
    }

    @Test
    void decomposer(){
        Decomposer<Constraint> decomposer = dateInterval.decomposer();
        try{
            for (Constraint subconstraint : decomposer.scaleDecompose(10)){
                System.out.println(subconstraint);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void satisfy() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        DateCondition condition1, condition2;
        try {
            condition1 = new DateCondition(df.parse("2023.04.03").getTime());
            condition2 = new DateCondition(df.parse("2023.06.04").getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(dateInterval.satisfy(condition1));
        assertFalse(dateInterval.satisfy(condition2));
    }

    @Test
    void getConditionClass(){
        assertSame(dateInterval.getConditionClass(), DateCondition.class);
    }
}
