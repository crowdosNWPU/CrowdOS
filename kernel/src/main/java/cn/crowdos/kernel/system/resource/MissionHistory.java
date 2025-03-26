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
package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.SystemResourceHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * MissionHistory records all active and completed missions in the system.
 *
 * @author loyx
 * @since 1.0.1
 * @see Mission
 */
public class MissionHistory implements Resource<MissionHistory>{
    ArrayList<Mission> missions;

    public MissionHistory() {
        this.missions = new ArrayList<>();
    }

    public void newMission(Task task, List<Participant> participants){
        missions.add(new Mission(task, participants));
    }

    /**
     * The getUnfinishedMissions function returns a list of all the missions that are unfinished.
     *
     * @return A list of all the unfinished missions in the missions field
     */
    public List<Mission> getUnfinishedMissions(){
        return missions.stream()
                .filter(mission -> mission.missionStatus == Mission.MissionStatus.UNFINISHED)
                .collect(Collectors.toList());
    }

    /**
     * The getFinishedMissions function returns a list of all the finished missions.
     *
     * @return A list of finished missions
     */
    public List<Mission> getFinishedMissions(){
        return missions.stream()
                .filter(mission -> mission.missionStatus == Mission.MissionStatus.FINISHED)
                .collect(Collectors.toList());
    }

    /**
     * The getMissionsByTask function returns a list of missions that belong to the task passed in as an argument.
     *
     * @param task Filter the missions by task
     *
     * @return A list of missions that belong to a specific task
     */
    public List<Mission> getMissionsByTask(Task task){
        return missions.stream()
                .filter(mission -> mission.belongTo(task))
                .collect(Collectors.toList());
    }

    /**
     * The getMissionsByParticipant function returns a list of missions that the participant is involved in.
     *
     * @param participant Filter the missions
     *
     * @return A list of missions that a participant is involved in
     */
    public List<Mission> getMissionsByParticipant(Participant participant){
        return missions.stream()
                .filter(mission -> mission.involved(participant))
                .collect(Collectors.toList());
    }

    /**
     * The getHandler function is used to return a SystemResourceHandler object that can be used to
     * access the MissionHistory object. This function is required by the SystemResource interface, and
     * it allows for a MissionHistory object to be accessed as if it were an instance of its parent class,
     * which in this case is SystemResource. The getHandler function returns an anonymous inner class that implements
     * the abstract methods of the SystemResourceHandler interface. In this case, there are two such methods:
     * getResourceView() and getResource(). Both functions simply return &quot;this&quot;, or in other words they
     *
     * @return A {@code SystemResourceHandler} object
     */
    @Override
    public SystemResourceHandler<MissionHistory> getHandler() {
        MissionHistory history = this;
        return new SystemResourceHandler<MissionHistory>() {
            @Override
            public MissionHistory getResourceView() {
                return history;
            }

            @Override
            public MissionHistory getResource() {
                return history;
            }
        };
    }
}
