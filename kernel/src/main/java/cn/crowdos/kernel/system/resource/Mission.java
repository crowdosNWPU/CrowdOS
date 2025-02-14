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
package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;

import java.util.*;

/**
 * Mission maintains the execution of tasks in the system, reporting over task assignment
 * results, task submission order, etc. Whenever a task is assigned, the system changes to
 * generate a Mission object. All the Missions are recorded in the {@link  MissionHistory}.
 *
 * @author loyx
 * @since 1.0.1
 * @see MissionHistory
 */
public class Mission {
    private final Task task;
    private final List<Participant> participants;
    private final List<Participant> submitParticipants;
    private final PriorityQueue<Object[]> priorityQueue;

    enum MissionStatus {
        UNFINISHED,
        FINISHED
    }
    MissionStatus missionStatus;

    /**
     * The Mission function is a constructor for the Mission class.
     * It takes in two parameters: task and participants, which are both objects of other classes.
     * The function initializes the fields of this class with these parameters, as well as creating
     * new instances of PriorityQueue and ArrayList to be used by this object.
     *
     * @param task Set the task of this mission
     * @param participants Create a new list&lt;participant&gt; submitparticipants
     */
    public Mission(Task task, List<Participant> participants) {
        this.task = task;
        this.participants = participants;
        this.priorityQueue = new PriorityQueue<>(Comparator.comparing(objects -> (Date)objects[1]));
        this.submitParticipants = new ArrayList<>();
        this.missionStatus = MissionStatus.UNFINISHED;
    }

    /**
     * The getFirstSubmitParticipant function z.
     *
     * @return The participant with the lowest time
     *
     */
    public Participant getFirstSubmitParticipant(){
        Object[] peek = priorityQueue.peek();
        if (peek == null) return null;
        return (Participant) peek[0];
    }

    /**
     * The getParticipants function returns a list of participants.
     *
     * @return A list of participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * The updateSubmit function updates the participant's submission time to the current time.
     *
     * @param participant Determine which participant is being updated
     * @throws MissionUpdateException MissionUpdateException
     */
    public void updateSubmit(Participant participant) throws MissionUpdateException {
        updateSubmit(participant, new Date());
    }

    /**
     * The updateSubmit function is used to update the submit time of a participant.
     *
     * @param participant Identify the participant that is updating their status
     * @param submitTime Determine the order in which participants submit their work
     * @throws MissionUpdateException MissionUpdateException
     */
    public void updateSubmit(Participant participant, Date submitTime) throws MissionUpdateException {
        if (!participants.contains(participant) || submitParticipants.contains(participant)){
            throw new MissionUpdateException();
        }
        submitParticipants.add(participant);
        priorityQueue.offer(new Object[]{participant, submitTime});
        if (submitParticipants.size() == participants.size()){
            task.setTaskStatus(Task.TaskStatus.FINISHED);
            missionStatus = MissionStatus.FINISHED;
        }
    }

    /**
     * The belongTo function checks if the task of a given TaskItem is equal to the
     * task of this TaskItem. If they are, it returns true. Otherwise, it returns false.
     *
     * @param task Compare the task object to the current task object
     *
     * @return A boolean value
     */
    public boolean belongTo(Task task){
        return this.task.equals(task);
    }

    /**
     * The involved function checks if a participant is involved in the conversation.
     *
     * @param participant Check if the participant is already in the participants list
     *
     * @return True if the participant is in the list of participants
     */
    public boolean involved(Participant participant){
        return this.participants.contains(participant);
    }
}
