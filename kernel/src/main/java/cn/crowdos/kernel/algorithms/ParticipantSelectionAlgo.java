package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;

import java.util.ArrayList;
import java.util.List;


@FunctionalInterface
public interface ParticipantSelectionAlgo {
    /**
     * Get all the workers that are eligible to be assigned to the given task.
     *
     * @param task The task for which you want to get the candidates.
     * @return A list of participants .
     */
    List<Participant> getCandidates(Task task);
    default List<List<Participant>> getCandidates(ArrayList<Task> tasks){
        List<List<Participant>> candidate = new ArrayList<List<Participant>>();
        for(Task task : tasks){
            candidate.add(getCandidates(task));
        }
        return candidate;
    };
}
