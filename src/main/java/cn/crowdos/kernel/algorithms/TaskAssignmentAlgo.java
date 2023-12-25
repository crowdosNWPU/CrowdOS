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
