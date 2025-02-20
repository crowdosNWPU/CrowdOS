/*
 * Copyright 2019-2025 CrowdOS_Group, Inc. <https://github.com/crowdosNWPU/CrowdOS>
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

public class DateDeadlineTest {

    DateDeadline dateDeadline;

    public DateDeadlineTest() {
        try {
            dateDeadline = new DateDeadline("4023-04-18");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    public void testException() {
        boolean exceptionThrown1 = false;
        boolean exceptionThrown2 = false;

        try {
            new DateDeadline("2022-04-07");
        } catch (InvalidConstraintException e) {
            exceptionThrown1 = true;
        }

        try {
            new DateDeadline(LocalDate.of(2022, 4, 7));
        } catch (InvalidConstraintException e) {
            exceptionThrown2 = true;
        }

        if (exceptionThrown1 && exceptionThrown2) {
            System.out.println("testException passed");
        } else {
            System.out.println("testException failed");
        }
    }

    public void decompser() {
        Decomposer<Constraint> decomposer = dateDeadline.decomposer();
        try {
            for (Constraint subconstraint : decomposer.scaleDecompose(10)) {
                System.out.println(subconstraint);
            }
            System.out.println("decompser passed");
        } catch (DecomposeException e) {
            System.out.println("decompser failed: " + e.getMessage());
        }
    }

    public void satisfy() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        DateCondition condition1 = null;
        DateCondition condition2 = null;
        try {
            condition1 = new DateCondition(df.parse("3023.04.10").getTime());
            condition2 = new DateCondition(df.parse("7023.06.04").getTime());
        } catch (ParseException e) {
            System.out.println("satisfy failed: " + e.getMessage());
            return;
        }

        boolean result1 = dateDeadline.satisfy(condition1);
        boolean result2 = dateDeadline.satisfy(condition2);

        if (result1 && !result2) {
            System.out.println("satisfy passed");
        } else {
            System.out.println("satisfy failed");
        }
    }

    public void getConditionClass() {
        if (dateDeadline.getConditionClass() == DateCondition.class) {
            System.out.println("getConditionClass passed");
        } else {
            System.out.println("getConditionClass failed");
        }
    }

    public static void main(String[] args) {
        DateDeadlineTest test = new DateDeadlineTest();
        test.testException();
        test.decompser();
        test.satisfy();
        test.getConditionClass();
    }
}
