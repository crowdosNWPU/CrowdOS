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
package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.constraint.Condition;
import cn.crowdos.kernel.constraint.Coordinate;

public class SimpleParticipant extends AbstractParticipant{

    @ability
    Coordinate location;

    @ability
    int something;

    @Override
    public boolean hasAbility(Class<? extends Condition> conditionClass) {
        return false;
    }

    @Override
    public Condition getAbility(Class<? extends Condition> conditionClass) {
        return null;
    }
}
