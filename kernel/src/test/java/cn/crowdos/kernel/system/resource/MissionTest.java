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
package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.SimpleParticipant;
import cn.crowdos.kernel.resource.SimpleTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

class MissionTest {

    Mission mission;
    private List<Participant> participants;
    private SimpleTask simpleTask;

    public void setUp() {
        simpleTask = new SimpleTask(null, null);
        participants = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            participants.add(new SimpleParticipant());
        }
        mission = new Mission(simpleTask, participants);
    }

    public boolean testGetFirstSubmitParticipant() {
        try {
            setUp();
            if (mission.getFirstSubmitParticipant() != null) {
                System.out.println("getFirstSubmitParticipant 初始状态不为 null，测试失败");
                return false;
            }
            mission.updateSubmit(participants.get(1));
            if (mission.getFirstSubmitParticipant() == null) {
                System.out.println("getFirstSubmitParticipant 更新提交后仍为 null，测试失败");
                return false;
            }
            if (!mission.getFirstSubmitParticipant().equals(participants.get(1))) {
                System.out.println("getFirstSubmitParticipant 获取的参与者不正确，测试失败");
                return false;
            }
            System.out.println("getFirstSubmitParticipant 测试通过");
            return true;
        } catch (MissionUpdateException e) {
            System.out.println("getFirstSubmitParticipant 测试中出现异常：" + e.getMessage());
            return false;
        }
    }

    public void testGetParticipants() {
        setUp();
        System.out.println(mission.getParticipants());
        System.out.println("getParticipants 测试完成");
    }

    public boolean testBelongTo() {
        setUp();
        SimpleTask task2 = new SimpleTask(null, null);
        if (!mission.belongTo(simpleTask)) {
            System.out.println("belongTo 对自身任务判断错误，测试失败");
            return false;
        }
        if (mission.belongTo(task2)) {
            System.out.println("belongTo 对其他任务判断错误，测试失败");
            return false;
        }
        System.out.println("belongTo 测试通过");
        return true;
    }

    public boolean testUpdateSubmitError() {
        try {
            setUp();
            SimpleParticipant p = new SimpleParticipant();
            mission.updateSubmit(p);
            System.out.println("updateSubmitError 未抛出异常，测试失败");
            return false;
        } catch (MissionUpdateException e) {
            System.out.println("updateSubmitError 测试通过");
            return true;
        }
    }

    public boolean testUpdateSubmit() {
        try {
            setUp();
            mission.updateSubmit(participants.get(0));
            if (!mission.getFirstSubmitParticipant().equals(participants.get(0))) {
                System.out.println("updateSubmit 第一次更新提交后获取的参与者不正确，测试失败");
                return false;
            }
            SimpleDateFormat sf = new SimpleDateFormat("YY");
            mission.updateSubmit(participants.get(1), sf.parse("1999"));
            if (!mission.getFirstSubmitParticipant().equals(participants.get(1))) {
                System.out.println("updateSubmit 第二次更新提交后获取的参与者不正确，测试失败");
                return false;
            }
            System.out.println("updateSubmit 测试通过");
            return true;
        } catch (MissionUpdateException | ParseException e) {
            System.out.println("updateSubmit 测试中出现异常：" + e.getMessage());
            return false;
        }
    }

    public boolean testInvolved() {
        setUp();
        for (Participant participant : participants) {
            if (!mission.involved(participant)) {
                System.out.println("involved 对参与的参与者判断错误，测试失败");
                return false;
            }
        }
        SimpleParticipant p2 = new SimpleParticipant();
        if (mission.involved(p2)) {
            System.out.println("involved 对未参与的参与者判断错误，测试失败");
            return false;
        }
        System.out.println("involved 测试通过");
        return true;
    }

    public static void main(String[] args) {
        MissionTest test = new MissionTest();
        test.testGetFirstSubmitParticipant();
        test.testGetParticipants();
        test.testBelongTo();
        test.testUpdateSubmitError();
        test.testUpdateSubmit();
        test.testInvolved();
    }
}