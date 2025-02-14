package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SimpleTimeConstraintTest {

    SimpleTimeConstraint constraint;

    public SimpleTimeConstraintTest() {
        try {
            constraint = new SimpleTimeConstraint("2022.06.01", "2022.06.05");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    public void testException() {
        try {
            new SimpleTimeConstraint("2021", "2022");
            System.out.println("testException failed: Expected InvalidConstraintException for '2021', '2022'");
        } catch (InvalidConstraintException e) {
            // Expected exception, test passed
        }

        try {
            new SimpleTimeConstraint("2021.12.01", "2020.01.01");
            System.out.println("testException failed: Expected InvalidConstraintException for '2021.12.01', '2020.01.01'");
        } catch (InvalidConstraintException e) {
            // Expected exception, test passed
        }
    }

    public void satisfy() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        DateCondition c1 = null;
        DateCondition c2 = null;
        try {
            c1 = new DateCondition(df.parse("2021.05.30").getTime());
            c2 = new DateCondition(df.parse("2022.06.04").getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (constraint.satisfy(c1)) {
            System.out.println("satisfy failed: Expected false for '2021.05.30'");
        }

        if (!constraint.satisfy(c2)) {
            System.out.println("satisfy failed: Expected true for '2022.06.04'");
        }
    }

    public void getConditionClass() {
        if (constraint.getConditionClass() != DateCondition.class) {
            System.out.println("getConditionClass failed: Expected DateCondition.class");
        }
    }

    public void decomposer() {
        Decomposer<Constraint> decomposer = constraint.decomposer();
        try {
            for (Constraint sub : decomposer.decompose(10)) {
                System.out.println(sub);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SimpleTimeConstraintTest test = new SimpleTimeConstraintTest();
        test.testException();
        test.satisfy();
        test.getConditionClass();
        test.decomposer();
    }
}