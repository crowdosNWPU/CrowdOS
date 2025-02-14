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
import cn.crowdos.kernel.system.SystemResourceHandler;

import java.util.LinkedList;

public class ParticipantPool extends LinkedList<Participant> implements Resource<ParticipantPool> {
    @Override
    public SystemResourceHandler<ParticipantPool> getHandler() {
        ParticipantPool participants = this;
        return new SystemResourceHandler<ParticipantPool>() {

            @Override
            public ParticipantPool getResourceView() {
                return participants;
            }

            @Override
            public ParticipantPool getResource() {
                return participants;
            }
        };
    }
}
