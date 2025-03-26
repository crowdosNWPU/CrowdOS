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

public class SpatioLineTest {

    SpatioLine spatioLine;

    // 初始化 spatioLine 对象
    {
        Coordinate startPoint = new Coordinate(2, 3);
        // Coordinate endPoint = new Coordinate(15,3);
        // Coordinate endPoint = new Coordinate(2,15);
        Coordinate endPoint = new Coordinate(15, 15);
        double width = 8;
        try {
            spatioLine = new SpatioLine(startPoint, endPoint, width);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SpatioLineTest test = new SpatioLineTest();
        test.testException();
        test.decomposer();
        test.satisfy();
        test.getConditionClass();
    }

    // 测试异常抛出
    public void testException() {
        try {
            new SpatioLine(new Coordinate(0, 0), new Coordinate(10, 10), -12);
            System.out.println("testException failed: Expected InvalidConstraintException was not thrown.");
        } catch (InvalidConstraintException e) {
            System.out.println("testException passed: InvalidConstraintException was thrown as expected.");
        } catch (Exception e) {
            System.out.println("testException failed: Unexpected exception was thrown: " + e.getMessage());
        }
    }

    // 测试分解器
    public void decomposer() {
        Decomposer<Constraint> decomposer = spatioLine.decomposer();
        try {
            for (Constraint subconstraint : decomposer.scaleDecompose(5)) {
                System.out.println(subconstraint);
            }
            System.out.println("decomposer passed.");
        } catch (DecomposeException e) {
            System.out.println("decomposer failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("decomposer failed: Unexpected exception was thrown: " + e.getMessage());
        }
    }

    // 测试满足条件
    public void satisfy() {
        Coordinate coordinate1 = new Coordinate(13, 17);
        Coordinate coordinate2 = new Coordinate(10, 1);
        boolean result1 = spatioLine.satisfy(coordinate1);
        boolean result2 = spatioLine.satisfy(coordinate2);
        if (result1 && !result2) {
            System.out.println("satisfy passed.");
        } else {
            System.out.println("satisfy failed.");
        }
    }

    // 测试获取条件类
    public void getConditionClass() {
        String name = spatioLine.getConditionClass().getName();
        if (name.equals(Coordinate.class.getName())) {
            System.out.println("getConditionClass passed.");
        } else {
            System.out.println("getConditionClass failed.");
        }
    }
}