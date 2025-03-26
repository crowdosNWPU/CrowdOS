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
package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.common.TimeParticipant;
import cn.crowdos.kernel.constraint.InvalidConstraintException;
import cn.crowdos.kernel.constraint.SimpleTimeConstraint;

import java.util.Collections;

class SimpleTaskTest {
    Task task;

    // 初始化任务的代码块
    {
        SimpleTimeConstraint timeConst;
        try {
            timeConst = new SimpleTimeConstraint("2022.6.1", "2022.6.5");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
        task = new SimpleTask(Collections.singletonList(timeConst), Task.TaskDistributionType.RECOMMENDATION);
    }

    // 测试分解器的方法
    void decomposer() {
        Decomposer<Task> decomposer = task.decomposer();
        try {
            for (Task sub : decomposer.decompose(5)) {
                System.out.println(sub);
            }
        } catch (DecomposeException e) {
            throw new RuntimeException(e);
        }
    }

    // 获取任务分配类型的方法
    void getTaskDistributionType() {
        System.out.println(task.getTaskDistributionType());
    }

    // 获取任务状态的方法
    void getTaskStatus() {
        System.out.println(task.getTaskStatus());
    }

    // 获取任务约束的方法
    void constraints() {
        System.out.println(task.constraints());
    }

    // 测试任务是否可分配给参与者的方法
    void canAssignTo() {
        TimeParticipant p1 = new TimeParticipant("2022.6.3");
        TimeParticipant p2 = new TimeParticipant("2022.6.10");
        boolean canAssignToP1 = task.canAssignTo(p1);
        boolean canAssignToP2 = task.canAssignTo(p2);
        System.out.println("Can assign to p1: " + canAssignToP1);
        System.out.println("Can assign to p2: " + canAssignToP2);
    }

    // 测试任务是否可分配的方法
    void assignable() {
        System.out.println(task.assignable());
    }

    // 测试任务是否已完成的方法
    void finished() {
        System.out.println(task.finished());
    }

    // 主方法，用于调用各个测试方法
    public static void main(String[] args) {
        SimpleTaskTest test = new SimpleTaskTest();
        test.decomposer();
        test.getTaskDistributionType();
        test.getTaskStatus();
        test.constraints();
        test.canAssignTo();
        test.assignable();
        test.finished();
    }
}