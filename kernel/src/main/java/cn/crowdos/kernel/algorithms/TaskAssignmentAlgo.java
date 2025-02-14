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
package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface TaskAssignmentAlgo {
    /**
     * Given a task, return a list of workers that are assigned to the task.
     *
     * @param task The task for which you want to get the assignment scheme.
     * @return A list of participants .
     */
    List<Participant> getAssignmentScheme(Task task);
    default List<List<Participant>> getAssignmentScheme(ArrayList<Task> tasks){
        List<List<Participant>> candidate = new ArrayList<>();
        for(Task task : tasks){
            candidate.add(getAssignmentScheme(task));
        }
        return candidate;
    }
}
