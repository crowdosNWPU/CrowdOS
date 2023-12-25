package cn.crowdos.kernel.algorithms;

public interface AlgoFactory {
    /**
     * This function returns the participant selection algorithm that the user has chosen
     *
     * @return The participant selection algorithm.
     */
    ParticipantSelectionAlgo getParticipantSelectionAlgo();
    /**
     * It returns a TaskRecommendationAlgo object.
     *
     * @return The TaskRecommendationAlgo object.
     */
    TaskRecommendationAlgo getTaskRecommendationAlgo();
    /**
     * This function returns the task assignment algorithm that is used by the scheduler
     *
     * @return The TaskAssignmentAlgo object.
     */
    TaskAssignmentAlgo getTaskAssignmentAlgo();
}
