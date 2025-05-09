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

public class SpatioPointTest {
    SpatioPoint spatioPoint;

    public SpatioPointTest() {
        try {
            spatioPoint = new SpatioPoint(new Coordinate(55.764, 59.253), 10);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    public void testException() {
        boolean exceptionThrown = false;
        try {
            new SpatioPoint(new Coordinate(55.764, 69.253), -10);
        } catch (InvalidConstraintException e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown) {
            System.err.println("testException failed: Expected InvalidConstraintException was not thrown.");
        } else {
            System.out.println("testException passed.");
        }
    }

    public void satisfy() {
        Coordinate coordinate1 = new Coordinate(57.563, 60.125);
        Coordinate coordinate2 = new Coordinate(12.589, 0.265);
        boolean result1 = spatioPoint.satisfy(coordinate1);
        boolean result2 = spatioPoint.satisfy(coordinate2);
        if (!result1) {
            System.err.println("satisfy failed: Expected true for coordinate1 but got false.");
        } else if (result2) {
            System.err.println("satisfy failed: Expected false for coordinate2 but got true.");
        } else {
            System.out.println("satisfy passed.");
        }
    }

    public void getConditionClass() {
        String name = spatioPoint.getConditionClass().getName();
        if (!name.equals(Coordinate.class.getName())) {
            System.err.println("getConditionClass failed: Expected " + Coordinate.class.getName() + " but got " + name);
        } else {
            System.out.println("getConditionClass passed.");
        }
    }

    public static void main(String[] args) {
        SpatioPointTest test = new SpatioPointTest();
        test.testException();
        test.satisfy();
        test.getConditionClass();
    }
}