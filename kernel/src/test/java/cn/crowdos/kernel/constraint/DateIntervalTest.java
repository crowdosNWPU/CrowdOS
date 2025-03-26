/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University. <https://github.com/crowdosNWPU/CrowdOS>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 */
package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class DateIntervalTest {

    DateInterval dateInterval;

    public DateIntervalTest() {
        try {
            dateInterval = new DateInterval("2023-04-01", "2023-05-06");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    public void testException() {
        try {
            new DateInterval("2023-04-08", "2023-04-01");
            System.out.println("testException failed: Expected InvalidConstraintException was not thrown.");
        } catch (InvalidConstraintException e) {
            // Expected exception, test passes
        }

        try {
            new DateInterval(LocalDate.of(2023, 4, 18), LocalDate.of(2023, 4, 8));
            System.out.println("testException failed: Expected InvalidConstraintException was not thrown.");
        } catch (InvalidConstraintException e) {
            // Expected exception, test passes
        }
    }

    public void decomposer() {
        Decomposer<Constraint> decomposer = dateInterval.decomposer();
        try {
            for (Constraint subconstraint : decomposer.scaleDecompose(10)) {
                System.out.println(subconstraint);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }

    public void satisfy() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        DateCondition condition1 = null;
        DateCondition condition2 = null;
        try {
            condition1 = new DateCondition(df.parse("2023.04.03").getTime());
            condition2 = new DateCondition(df.parse("2023.06.04").getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (dateInterval.satisfy(condition1)) {
            System.out.println("satisfy test passed for condition1.");
        } else {
            System.out.println("satisfy test failed for condition1.");
        }

        if (!dateInterval.satisfy(condition2)) {
            System.out.println("satisfy test passed for condition2.");
        } else {
            System.out.println("satisfy test failed for condition2.");
        }
    }

    public void getConditionClass() {
        if (dateInterval.getConditionClass() == DateCondition.class) {
            System.out.println("getConditionClass test passed.");
        } else {
            System.out.println("getConditionClass test failed.");
        }
    }

    public static void main(String[] args) {
        DateIntervalTest test = new DateIntervalTest();
        test.testException();
        test.decomposer();
        test.satisfy();
        test.getConditionClass();
    }
}
