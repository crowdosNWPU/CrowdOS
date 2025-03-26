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
package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.InterruptManager;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.resource.ParticipantPool;
import cn.crowdos.kernel.resource.Task;

import java.util.LinkedList;
import java.util.List;

public class AlgoFactoryAdapter implements AlgoFactory{

    protected final SystemResourceCollection resourceCollection;

    //中断器管理实例
    private InterruptManager interruptManager;

    public AlgoFactoryAdapter(SystemResourceCollection resourceCollection){
        this.resourceCollection = resourceCollection;
    }
    /**
     * The getParticipantSelectionAlgo function returns a ParticipantSelectionAlgo object that is used to select
     * participants for tasks. The algorithm implemented here selects all available participants who can perform the task.
     *
     * @return A new instance of ParticipantSelectionAlgo
     *
     */
    @Override
    public ParticipantSelectionAlgo getParticipantSelectionAlgo() {

        return new ParticipantSelectionAlgo() {
            final ParticipantPool participantPool;
            {
                participantPool = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();
            }
            @Override
            public List<Participant> getCandidates(Task task) {
                List<Participant> candidate = new LinkedList<>();
                for (Participant participant : participantPool) {
                    if (participant.available() && task.canAssignTo(participant)){
                        candidate.add(participant);
                    }
                }
                return candidate;
            }
        };
    }

    @Override
    public TaskRecommendationAlgo getTaskRecommendationAlgo() {
        return new TaskRecommendationAlgo() {
            final ParticipantPool participantPool;
            {
                participantPool = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();
            }
            @Override
            public List<Participant> getRecommendationScheme(Task task) {
                List<Participant> candidate = new LinkedList<>();
                for (Participant participant : participantPool) {
                    if (participant.available() && task.canAssignTo(participant)){
                        candidate.add(participant);
                    }
                }
                return candidate;
            }
        };
    }

    @Override
    public TaskAssignmentAlgo getTaskAssignmentAlgo() {
        return new TaskAssignmentAlgo() {
            final ParticipantPool participantPool;
            {
                participantPool = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();
            }
            @Override
            public List<Participant> getAssignmentScheme(Task task) {
                List<Participant> candidate = new LinkedList<>();
                for (Participant participant : participantPool) {
                    if (participant.available() && task.canAssignTo(participant)){
                        candidate.add(participant);
                    }
                }
                return candidate;
            }
        };
    }
}
