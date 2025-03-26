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

import cn.crowdos.kernel.algorithms.*;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.SystemResourceHandler;
import cn.crowdos.kernel.system.resource.AlgoContainer;
import cn.crowdos.kernel.system.resource.Resource;
import cn.crowdos.kernel.system.resource.TaskPool;

import java.util.ArrayList;
import java.util.List;

public class Scheduler implements Resource<Scheduler> {
    // A private variable that is used to store the participant selection algorithm.
    private ParticipantSelectionAlgo participantSelectionAlgo;
    // A private variable that is used to store the task recommendation algorithm.
    private TaskRecommendationAlgo taskRecommendationAlgo;
    // A private variable that is used to store the task assignment algorithm.
    private TaskAssignmentAlgo taskAssignmentAlgo;
    // A private variable that is used to store the algorithm factory.
    private AlgoFactory algoFactory;
    // Used to store the resource collection.
    private final SystemResourceCollection resourceCollection;

    // The constructor of the Scheduler class. It is used to initialize the scheduler.
    public Scheduler(SystemResourceCollection collection){
        this.resourceCollection = collection;
        SystemResourceHandler<AlgoFactory> resourceHandler = collection.getResourceHandler(AlgoContainer.class,"DefaultAlgo");
        this.algoFactory = resourceHandler.getResource();

        participantSelectionAlgo = algoFactory.getParticipantSelectionAlgo();
        taskRecommendationAlgo = algoFactory.getTaskRecommendationAlgo();
        taskAssignmentAlgo = algoFactory.getTaskAssignmentAlgo();
    }

    /**
     * The setAlgoFactory function is used to set the AlgoFactory that will be used by the
     * TaskAssignmentManager. The name of the AlgoFactory must be passed in as a parameter, and
     * this function will then use that name to retrieve an instance of an AlgoFactory from
     * ResourceCollection. This retrieved instance is then stored in algoFactory, which can later
     * be accessed by other functions within TaskAssignmentManager. In addition, this function also
     * retrieves instances of ParticipantSelectionAlgorithm and TaskRecommendationAlgorithm from
     * algoFactory (which are both interfaces),
     *
     * @param name Get the needed algorithms from the resourceCollection.
     *
     */
    public void setAlgoFactory(String name){
        SystemResourceHandler<AlgoFactory> resourceHandler = resourceCollection.getResourceHandler(AlgoContainer.class,name);
        algoFactory = resourceHandler.getResource();
        participantSelectionAlgo = algoFactory.getParticipantSelectionAlgo();
        taskRecommendationAlgo = algoFactory.getTaskRecommendationAlgo();
        taskAssignmentAlgo = algoFactory.getTaskAssignmentAlgo();
    }


    /**
     *  For each task in the task pool, recommend a list of participants to complete the task
     *
     * @return A list of lists of participants.
     */
    public List<List<Participant>> recommendTasks(){
        TaskPool resourceView = resourceCollection.getResourceHandler(TaskPool.class).getResourceView();
        List<List<Participant>> recommendScheme = new ArrayList<>(resourceView.size());
        for (Task task : resourceView) {
            recommendScheme.add(taskRecommendation(task));
        }
        return recommendScheme;
    }

    /**
     *  For each task in the task pool, assign a list of participants to it
     *
     * @return A list of lists of participants.
     */
    public List<List<Participant>> assignTasks(){
        TaskPool resourceView = resourceCollection.getResourceHandler(TaskPool.class).getResourceView();
        List<List<Participant>> assignmentScheme = new ArrayList<>(resourceView.size());
        for (Task task : resourceView) {
            assignmentScheme.add(taskAssignment(task));
        }
        return assignmentScheme;
    }

    /**
     * The participant selection algorithm is used to get the candidates for a task.
     *
     * @param task The task for which the participants are to be selected.
     * @return A list of participants.
     */
    public List<Participant> participantSelection(Task task){
        return participantSelectionAlgo.getCandidates(task);
    }
    public List<List<Participant>> participantSelection(ArrayList<Task> tasks){
        return participantSelectionAlgo.getCandidates(tasks);
    }
    /**
     * This function returns a list of participants that are recommended for a given task.
     *
     * @param task The task for which you want to get the recommendations.
     * @return A list of participants.
     */
    public List<Participant> taskRecommendation(Task task){
        return taskRecommendationAlgo.getRecommendationScheme(task);
    }
    public List<List<Participant>> taskRecommendation(ArrayList<Task> tasks){
        return taskRecommendationAlgo.getRecommendationScheme(tasks);
    }
    /**
     * This function returns a list of participants that are assigned to a task.
     *
     * @param task The task object that needs to be assigned to participants.
     * @return A list of participants.
     */
    public List<Participant> taskAssignment(Task task){
        return taskAssignmentAlgo.getAssignmentScheme(task);
    }
    public List<List<Participant>> taskAssignment(ArrayList<Task> tasks){
        return taskAssignmentAlgo.getAssignmentScheme(tasks);
    }
    /**
     * This function returns the AlgoFactory object that was created in the constructor.
     *
     * @return The algoFactory object.
     */

    @Override
    // A method that returns a SystemResourceHandler object.
    public SystemResourceHandler<Scheduler> getHandler() {
        Scheduler scheduler = this;
        return new SystemResourceHandler<Scheduler>() {
            @Override
            public Scheduler getResourceView() {
                return scheduler;
            }

            @Override
            public Scheduler getResource() {
                return scheduler;
            }
        };
    }
}
