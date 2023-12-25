package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTimeConstraintTest {

    SimpleTimeConstraint constraint;
    {
        try {
            constraint = new SimpleTimeConstraint("2022.06.01", "2022.06.05");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testException(){
        assertThrows(
                InvalidConstraintException.class,
                () -> new SimpleTimeConstraint("2021", "2022")
        );
        assertThrows(
                InvalidConstraintException.class,
                () -> new SimpleTimeConstraint("2021.12.01", "2020.01.01")
        );
    }

    @Test
    void satisfy() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        DateCondition c1, c2;
        try {
            c1 = new DateCondition(df.parse("2021.05.30").getTime());
            c2 = new DateCondition(df.parse("2022.06.04").getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertFalse(constraint.satisfy(c1));
        assertTrue(constraint.satisfy(c2));
    }

    @Test
    void getConditionClass() {
        assertSame(constraint.getConditionClass(), DateCondition.class);
    }

    @Test
    void decomposer() {
        Decomposer<Constraint> decomposer = constraint.decomposer();
        try {
            for (Constraint sub : decomposer.decompose(10)) {
                System.out.println(sub);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }
}