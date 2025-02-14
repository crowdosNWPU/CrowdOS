package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import java.text.ParseException;
import java.text.SimpleDateFormat;

class TemporalPointTest {

    TemporalPoint temporalPoint;

    public TemporalPointTest() {
        temporalPoint = new TemporalPoint("10:00:00", 10);
    }

    public void decomposer() throws DecomposeException {
        Decomposer<Constraint> decomposer = temporalPoint.decomposer();
        System.out.println(decomposer.trivialDecompose());
        System.out.println(decomposer.scaleDecompose(10));
    }

    public void satisfy() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("hh:mm");
        DateCondition time1 = new DateCondition(sf.parse("10:30").getTime());
        DateCondition time2 = new DateCondition(sf.parse("10:05").getTime());
        if (temporalPoint.satisfy(time1)) {
            System.out.println("satisfy(time1) test failed");
        } else {
            System.out.println("satisfy(time1) test passed");
        }
        if (temporalPoint.satisfy(time2)) {
            System.out.println("satisfy(time2) test passed");
        } else {
            System.out.println("satisfy(time2) test failed");
        }
    }

    public void getConditionClass() {
        if (temporalPoint.getConditionClass() == DateCondition.class) {
            System.out.println("getConditionClass() test passed");
        } else {
            System.out.println("getConditionClass() test failed");
        }
    }

    public static void main(String[] args) {
        TemporalPointTest test = new TemporalPointTest();
        try {
            test.decomposer();
        } catch (DecomposeException e) {
            System.out.println("decomposer() test failed: " + e.getMessage());
        }
        try {
            test.satisfy();
        } catch (ParseException e) {
            System.out.println("satisfy() test failed: " + e.getMessage());
        }
        test.getConditionClass();
    }
}