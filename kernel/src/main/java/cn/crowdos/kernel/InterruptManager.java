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
package cn.crowdos.kernel;

import java.util.HashMap;
import java.util.Map;

/**
 * InterruptManager Interrupt requests for managing tasks
 */
public class InterruptManager {
    private Map<String, Boolean> interruptFlags;

    /**
     * Constructor to initialize the HashMap of the interrupt flag.
     */
    public InterruptManager() {
        interruptFlags = new HashMap<>();
    }

    /**
     * Request to interrupt a specific task, and set the interrupt flag corresponding to the task ID to true.
     *
     * @param taskId taskID
     */
    public void requestInterrupt(String taskId) {
        interruptFlags.put(taskId, true);
    }

    /**
     * Checks if a particular task was interrupted and returns false if the task ID does not exist.
     *
     * @param taskId taskID
     * @return If the task was interrupted, true is returned. Otherwise, false is returned.
     */
    public boolean isInterrupted(String taskId) {
        return interruptFlags.getOrDefault(taskId, false);
    }

    /**
     * Clears the interrupt flag for a specific task.
     *
     * @param taskId taskID
     */
    public void clearInterrupt(String taskId) {
        interruptFlags.remove(taskId);
    }
}
