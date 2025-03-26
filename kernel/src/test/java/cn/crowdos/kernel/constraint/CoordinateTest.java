/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University, Inc. <https://github.com/crowdosNWPU/CrowdOS>
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

class CoordinateTest {

    public static void main(String[] args) {
        testInLine();
        testToString();
    }

    public static void testInLine() {
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(0, 0);
        Coordinate c3 = new Coordinate(0, 1);
        Coordinate c4 = new Coordinate(1, 0);
        Coordinate c5 = new Coordinate(1, 1);

        boolean result1 = c1.inLine(c2);
        boolean result2 = c1.inLine(c3);
        boolean result3 = c1.inLine(c4);
        boolean result4 = c1.inLine(c5);

        if (result1 && result2 && result3 && !result4) {
            System.out.println("testInLine passed");
        } else {
            System.out.println("testInLine failed");
        }
    }

    public static void testToString() {
        Coordinate c1 = new Coordinate(0, 0);
        System.out.println(c1);
    }
}
