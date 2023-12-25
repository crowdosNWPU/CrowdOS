package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface TaskRecommendationAlgo {
    /**
     * Given a task, return a list of workers that are recommended to work on the task.
     *
     * @param task The task for which you want to get the recommendation scheme.
     * @return A list of participants .
     */
    List<Participant> getRecommendationScheme(Task task);
    default List<List<Participant>> getRecommendationScheme(ArrayList<Task> tasks){
        List<List<Participant>> candidate = new ArrayList<List<Participant>>();
        for(Task task : tasks){
            candidate.add(getRecommendationScheme(task));
        }
        return candidate;
    };
}
