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

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.constraint.Constraint;

import java.util.*;

public class SimpleTask extends AbstractTask{

    public SimpleTask(List<Constraint> constraints, TaskDistributionType taskDistributionType) {
        super(constraints, taskDistributionType);
        status = TaskStatus.READY;
    }

    @Override
    public Decomposer<Task> decomposer() {
        SimpleTask simpleTask = this;
        return new Decomposer<Task>() {
            @Override
            public List<Task> trivialDecompose() {
                return Collections.singletonList(simpleTask);
            }


            @Override
            public List<Task> scaleDecompose(int scale) throws DecomposeException {
                List<Task> subTasks = new LinkedList<>();
                List<Decomposer<Constraint>> decomposers = new ArrayList<Decomposer<Constraint>>(constraints.size()){{
                    for (Constraint constraint : constraints) {
                        add(constraint.decomposer());
                    }
                }};
                subTaskHelper(subTasks, decomposers, null, 0, scale);
                return subTasks;
            }

            private void subTaskHelper(
                    List<Task> subTasks,
                    List<Decomposer<Constraint>> decomposers,
                    List<Constraint> newConstraints,
                    int pos,
                    int scale
            ) throws DecomposeException {
                if (pos == 0){
                    newConstraints = new ArrayList<>(decomposers.size());
                }
                if (pos == decomposers.size()){
                    subTasks.add(new SimpleTask(newConstraints, taskDistributionType));
                    return;
                }
                Decomposer<Constraint> current = decomposers.get(pos);
                for (Constraint constraint : current.decompose(scale)) {
                    newConstraints.add(constraint);
                    subTaskHelper(subTasks, decomposers, newConstraints, pos+1, scale);
                }
            }

        };
    }
}
