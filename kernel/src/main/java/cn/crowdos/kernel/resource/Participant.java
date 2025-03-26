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

import cn.crowdos.kernel.constraint.Condition;

public interface Participant {

    // Defining a new type called `ParticipantStatus` with three possible values: `AVAILABLE`, `BUSY`, and `DISABLED`.
    enum ParticipantStatus {
        AVAILABLE,
        BUSY,
        DISABLED,
    }

    /**
     * Returns the status of the participant
     *
     * @return The status of the participant.
     */
    ParticipantStatus getStatus();

    /**
     * Sets the status of the participant
     *
     * @param status The status of the participant.
     */
    void setStatus(ParticipantStatus status);

    /**
     * Returns true if the given condition is present in the list of conditions.
     *
     * @param conditionClass The class of the condition to check for.
     * @return A boolean value.
     */
    boolean hasAbility(Class<? extends Condition> conditionClass);

    /**
     * Get the ability of the given class.
     *
     * @param conditionClass The class of the condition you want to get.
     * @return A condition object.
     */
    Condition getAbility(Class<? extends Condition> conditionClass);

    /**
     * Returns true if the next token is a number.
     *
     * @return A boolean value.
     */
    boolean available();

}
