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

import java.util.ArrayList;
import java.util.List;

public class SpatioPolygonTest {
    SpatioPolygon spatioPolygon;

    public SpatioPolygonTest() {
        List<Coordinate> polygon = new ArrayList<>();
        polygon.add(new Coordinate(0, 1));
        polygon.add(new Coordinate(1, 3));
        polygon.add(new Coordinate(2, 4));
        polygon.add(new Coordinate(4, 2));
        polygon.add(new Coordinate(2, 0));
        try {
            spatioPolygon = new SpatioPolygon(polygon);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    public void testException() {
        boolean exceptionThrown1 = false;
        boolean exceptionThrown2 = false;

        List<Coordinate> polygon1 = new ArrayList<>();
        polygon1.add(new Coordinate(0, 1));
        polygon1.add(new Coordinate(1, 3));
        try {
            new SpatioPolygon(polygon1);
        } catch (InvalidConstraintException e) {
            exceptionThrown1 = true;
        }

        List<Coordinate> polygon2 = new ArrayList<>();
        polygon2.add(new Coordinate(0, 1));
        polygon2.add(new Coordinate(1, 3));
        polygon2.add(new Coordinate(2, 4));
        polygon2.add(new Coordinate(2, 0));
        polygon2.add(new Coordinate(4, 2));
        try {
            new SpatioPolygon(polygon2);
        } catch (InvalidConstraintException e) {
            exceptionThrown2 = true;
        }

        if (!exceptionThrown1 || !exceptionThrown2) {
            System.out.println("testException failed.");
        } else {
            System.out.println("testException passed.");
        }
    }

    public void satisfy() {
        Coordinate coordinate1 = new Coordinate(2, 2);
        Coordinate coordinate2 = new Coordinate(4, 0);
        boolean result1 = spatioPolygon.satisfy(coordinate1);
        boolean result2 = spatioPolygon.satisfy(coordinate2);

        if (result1 && !result2) {
            System.out.println("satisfy passed.");
        } else {
            System.out.println("satisfy failed.");
        }
    }

    public void decomposer() {
        Decomposer<Constraint> decomposer = spatioPolygon.decomposer();
        try {
            for (Constraint subconstraint : decomposer.scaleDecompose(15)) {
                System.out.println(subconstraint);
            }
            System.out.println("decomposer passed.");
        } catch (DecomposeException e) {
            System.out.println("decomposer failed: " + e.getMessage());
        }
    }

    public void getConditionClass() {
        String name = spatioPolygon.getConditionClass().getName();
        if (name.equals(Coordinate.class.getName())) {
            System.out.println("getConditionClass passed.");
        } else {
            System.out.println("getConditionClass failed.");
        }
    }

    public static void main(String[] args) {
        SpatioPolygonTest test = new SpatioPolygonTest();
        test.testException();
        test.satisfy();
        test.decomposer();
        test.getConditionClass();
    }
}