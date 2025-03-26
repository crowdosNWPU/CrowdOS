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
package cn.crowdos.kernel;

import cn.crowdos.kernel.common.TimeParticipant;
import cn.crowdos.kernel.constraint.InvalidConstraintException;
import cn.crowdos.kernel.constraint.SimpleTimeConstraint;
import cn.crowdos.kernel.resource.SimpleTask;
import cn.crowdos.kernel.resource.Task;

import java.util.Collections;

public class KernelTest {

    private CrowdKernel kernel;

    // 初始化方法，原@BeforeEach注解的方法内容
    private void init() {
        kernel = Kernel.getKernel();
        kernel.initial();
        TimeParticipant p1 = new TimeParticipant("2022.6.1");
        TimeParticipant p2 = new TimeParticipant("2022.6.2");
        TimeParticipant p3 = new TimeParticipant("2022.6.3");
        TimeParticipant p4 = new TimeParticipant("2022.6.4");
        TimeParticipant p5 = new TimeParticipant("2022.6.5");
        TimeParticipant p6 = new TimeParticipant("2022.6.6");
        kernel.registerParticipant(p1);
        kernel.registerParticipant(p2);
        kernel.registerParticipant(p3);
        kernel.registerParticipant(p4);
        kernel.registerParticipant(p5);
        kernel.registerParticipant(p6);
        try {
            SimpleTimeConstraint timeConst = new SimpleTimeConstraint("2022.6.1", "2022.6.5");
            SimpleTask t1 = new SimpleTask(Collections.singletonList(timeConst), Task.TaskDistributionType.RECOMMENDATION);
            timeConst = new SimpleTimeConstraint("2022.6.2", "2022.6.4");
            SimpleTask t2 = new SimpleTask(Collections.singletonList(timeConst), Task.TaskDistributionType.ASSIGNMENT );
            kernel.submitTask(t1);
            kernel.submitTask(t2);
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    // 清理方法，原@AfterEach注解的方法内容
    private void cleanup() {
        Kernel.shutdown();
    }

    // 原getTasks测试方法内容
    private void testGetTasks() {
        System.out.println(kernel.getTasks());
    }

    // 原getParticipants测试方法内容
    private void testGetParticipants() {
        System.out.println(kernel.getParticipants());
    }

    // 原getTaskAssignmentScheme测试方法内容
    private void testGetTaskAssignmentScheme() {
        for (Task task : kernel.getTasks()) {
            System.out.println(kernel.getTaskAssignmentScheme(task));
        }
    }

    // 原getTaskRecommendationScheme测试方法内容
    private void testGetTaskRecommendationScheme() {
        for (Task task : kernel.getTasks()) {
            System.out.println(kernel.getTaskRecommendationScheme(task));
        }
    }

    // 原getTaskParticipantSelectionResult测试方法内容
    private void testGetTaskParticipantSelectionResult() {
        for (Task task : kernel.getTasks()) {
            System.out.println(kernel.getTaskParticipantSelectionResult(task));
        }
    }

    public static void main(String[] args) {
        KernelTest test = new KernelTest();
        test.init();

        test.testGetTasks();
        test.testGetParticipants();
        test.testGetTaskAssignmentScheme();
        test.testGetTaskRecommendationScheme();
        test.testGetTaskParticipantSelectionResult();

        test.cleanup();
    }
}