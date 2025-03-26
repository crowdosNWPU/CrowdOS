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
package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.resource.Task;

import java.util.Collections;
import java.util.List;

/**
 * Dependence Constraint
 *
 * @author loyx
 * @since 1.0.1
 */
public class DependenceConstraint implements Constraint {
    List<Task> previousTasks;

    /**
     * The DependenceConstraint function is used to determine if a task can be scheduled.
     * It checks the previous tasks in the list and determines if they have been completed or not.
     * If all of them are complete, then it returns true, otherwise it returns false.

     *
     * @param previousTasks Store the list of tasks that must be completed before the current task can begin
     */
    public DependenceConstraint(List<Task> previousTasks) {
        this.previousTasks = previousTasks;
    }

    /**
     * The decomposer function for the DependenceConstraint class.
     *
     * @return A decomposer that creates a new
     */
    @Override
    public Decomposer<Constraint> decomposer() {
        List<Class<?>> argsClass = Collections.singletonList(List.class);
        List<Object> args = Collections.singletonList(previousTasks);
        return new IndecomposableDecomposerGenerator(argsClass, args, DependenceConstraint.class);
    }

    /**
     * The satisfy function checks if all the previous tasks have been completed.
     *
     * @return True if all previous tasks are finished
     */
    @Override
    public boolean satisfy() {
        return previousTasks.stream().allMatch(Task::finished);
    }

    /**
     * The satisfy function checks if the condition is satisfied.
     *
     * @param  condition Check if the condition is satisfied
     *
     * @return True if the condition is satisfied, and false otherwise
     */
    @Override
    public boolean satisfy(Condition condition) {
        return satisfy();
    }

    /**
     * get Condition Class Object
     *
     * @return The condition class that the effect will be applied to
     */
    @Override
    public Class<? extends Condition> getConditionClass() {
        return NoneCondition.class;
    }

    /**
     * The description function returns a string that describes the object.
     *
     * @return A string that is the name of the class and
     */
    @Override
    public String description() {
        return toString();
    }

    /**
     * The toString function is used to print out the contents of a class.
     * In this case, it prints out the previousTasks arraylist.
     *
     * @return A string representation of the object
     */
    @Override
    public String toString() {
        return "DependenceConstraint{" +
                "previousTasks=" + previousTasks +
                '}';
    }
}
