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
package cn.crowdos.kernel.common;

import cn.crowdos.kernel.constraint.Condition;
import cn.crowdos.kernel.wrapper.DateCondition;
import cn.crowdos.kernel.resource.AbstractParticipant;
import cn.crowdos.kernel.resource.ability;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeParticipant extends AbstractParticipant {
    @ability
    final DateCondition activeTime;
    private final SimpleDateFormat format;

    public TimeParticipant(String time) {
        format = new SimpleDateFormat("yyyy.MM.dd");
        try {
            this.activeTime = new DateCondition(format.parse(time).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        status = ParticipantStatus.AVAILABLE;
    }

    @Override
    public boolean hasAbility(Class<? extends Condition> conditionClass) {
        return conditionClass == DateCondition.class;
    }

    @Override
    public Condition getAbility(Class<? extends Condition> conditionClass) {
        if (!hasAbility(conditionClass))
            return null;
        else return activeTime;
    }

    @Override
    public String toString() {
        return "P(" + format.format(activeTime) + ")";
    }
}
