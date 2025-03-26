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
package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.Decomposable;
import cn.crowdos.kernel.constraint.Constraint;

import java.util.List;

public interface Task extends Decomposable<Task> {



    // Defining a new type called `TaskDistributionType` with two possible values: `ASSIGNMENT` and `RECOMMENDATION`.
    enum TaskDistributionType{
        ASSIGNMENT,
        RECOMMENDATION,
    }

    // Defining a new type called `TaskStatus` with three possible values: `READY`, `IN_PROGRESS`, and `FINISHED`.
    enum TaskStatus {
        READY,
        IN_PROGRESS,
        FINISHED,
    }

    /**
     * The logic to perform the task
     */
    void execute();

    /**
     * Returns the type of task distribution used in the simulation
     *
     * @return The task distribution type.
     */
    TaskDistributionType getTaskDistributionType();

    /**
     * Returns the status of the task.
     *
     * @return The task status is being returned.
     */
    TaskStatus getTaskStatus();
    /**
     * Sets the status of the task to the given status.
     *
     * @param status The status of the task.
     */
    void setTaskStatus(TaskStatus status);

    /**
     * Returns a list of constraints that are applied to the field.
     *
     * @return A list of constraints.
     */
    List<Constraint> constraints();

    /**
     * Returns true if the given participant can be assigned to this role.
     *
     * @param participant The participant to assign the task to.
     * @return A boolean value.
     */
    boolean canAssignTo(Participant participant);

    /**
     * Returns true if the type is assignable to the type of the given object.
     *
     * @return A boolean value.
     */
    boolean assignable();

    /**
     * Returns true if the game is over, false otherwise.
     *
     * @return A boolean value.
     */
    boolean finished();

    /**
     *Interrupt handling logic
     *
     */
    void handleInterrupt();

    /**
     * Check if the task is complete
     *
     * @return A boolean value.
     */
    boolean isCompleted();
}
