package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DateDeadlineTest {

    DateDeadline dateDeadline;
    {
        try{
            dateDeadline = new DateDeadline("4023-04-18");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testException(){
        assertThrows(
                InvalidConstraintException.class,
                () -> new DateDeadline("2022-04-07")
        );
        assertThrows(
                InvalidConstraintException.class,
                () -> new DateDeadline(LocalDate.of(2022,4,7))
        );
    }

    @Test
    void decompser(){
        Decomposer<Constraint> decomposer = dateDeadline.decomposer();
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
            condition1 = new DateCondition(df.parse("3023.04.10").getTime());
            condition2 = new DateCondition(df.parse("7023.06.04").getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(dateDeadline.satisfy(condition1));
        assertFalse(dateDeadline.satisfy(condition2));
    }

    @Test
    void getConditionClass(){
        assertSame(dateDeadline.getConditionClass(), DateCondition.class);
    }
}
