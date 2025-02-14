package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import java.text.ParseException;
import java.text.SimpleDateFormat;

class TemporalIntervalTest {

    TemporalInterval temporalInterval;

    public TemporalIntervalTest() throws InvalidConstraintException {
        // 初始化 TemporalInterval 对象
        temporalInterval = new TemporalInterval("10:00", "11:00");
    }

    public void testCreate() {
        try {
            new TemporalInterval("11:00:00", "09:00:00");
            System.out.println("testCreate failed: Expected InvalidConstraintException was not thrown.");
        } catch (InvalidConstraintException e) {
            System.out.println("testCreate passed.");
        }
    }

    public void decomposer() {
        try {
            Decomposer<Constraint> decomposer = temporalInterval.decomposer();
            System.out.println(decomposer.trivialDecompose());
            System.out.println(decomposer.scaleDecompose(4));
            System.out.println("decomposer test passed.");
        } catch (DecomposeException e) {
            System.out.println("decomposer test failed: " + e.getMessage());
        }
    }

    public void satisfy() {
        SimpleDateFormat sf = new SimpleDateFormat("hh:mm");
        try {
            DateCondition time1 = new DateCondition(sf.parse("10:30").getTime());
            DateCondition time2 = new DateCondition(sf.parse("11:30").getTime());
            if (temporalInterval.satisfy(time1) && !temporalInterval.satisfy(time2)) {
                System.out.println("satisfy test passed.");
            } else {
                System.out.println("satisfy test failed.");
            }
        } catch (ParseException e) {
            System.out.println("satisfy test failed: " + e.getMessage());
        }
    }

    public void getConditionClass() {
        if (temporalInterval.getConditionClass() == DateCondition.class) {
            System.out.println("getConditionClass test passed.");
        } else {
            System.out.println("getConditionClass test failed.");
        }
    }

    public static void main(String[] args) {
        try {
            TemporalIntervalTest test = new TemporalIntervalTest();
            test.testCreate();
            test.decomposer();
            test.satisfy();
            test.getConditionClass();
        } catch (InvalidConstraintException e) {
            System.out.println("Initialization failed: " + e.getMessage());
        }
    }
}