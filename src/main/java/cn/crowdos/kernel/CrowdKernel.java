package cn.crowdos.kernel;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.SystemResourceCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CrowdOS is a ubiquitous operating system for Crowdsoucring and Mobile Crowdsensing,
 * which can deal with multiple types of crowdsourcing problems simultaneously.
 * CrowdOS kernel provide three core frameworks including Task Resolution and
 * Assignment Framework (TRAF), Integrated Resource Management (IRM), and Task Result quality Optimization (TRO).
 */
public interface CrowdKernel {
    // CrowdKernel APIs

    /**
     * It checks if the object is initialized.
     *
     * @return A boolean value.
     */
    boolean isInitialed();

    /**
     * Initial takes any number of arguments and returns the first one.
     * @param args initial args
     */
    void initial(Object...args);


    //The system provides PTMost, T_Most, T_Random and GGA_I algorithms by default. If necessary, you can choose them by yourself
    void algoSelect(String name);

    /**
     * It initializes the program.
     */
    void initial();

    /**
     * Returns the collection of system resources.
     *
     * @return A collection of system resources.
     */
    SystemResourceCollection getSystemResourceCollection() ;

    boolean submitTask(Task task);

    /**
     * This function registers a participant in the system.
     *
     * @param participant The participant object to be registered.
     * @return A boolean value.
     */
    boolean registerParticipant(Participant participant);
    /**
     * This function returns a list of tasks.
     *
     * @return A list of tasks.
     */
    List<Task> getTasks();
    /**
     * Given a task, return a list of participants that are assigned to the task.
     *
     * @param task The task to be assigned.
     * @return A list of participants.
     */
    List<Participant> getTaskAssignmentScheme(Task task);

    Map<Participant, Double> getTaskIncentiveAssignmentScheme(Task task , Double rewards);

    List<List<Participant>> getTaskAssignmentScheme(ArrayList<Task> tasks);

    /**
     * Given a task, return a list of participants that are recommended to work on that task.
     *
     * @param task The task for which you want to get the recommendation scheme.
     * @return A list of participants.
     */
    List<Participant> getTaskRecommendationScheme(Task task);

    List<List<Participant>> getTaskRecommendationScheme(ArrayList<Task> tasks);

    /**
     * Get the participant selection result for the given task.
     *
     * @param task The task object that you want to get the participant selection result for.
     * @return A list of participants.
     */
    List<Participant> getTaskParticipantSelectionResult(Task task);

    List<List<Participant>> getTaskParticipantSelectionResult(ArrayList<Task> tasks);

    /**
     * Get a list of all the participants in the conversation.
     *
     * @return A list of participants.
     */
    List<Participant> getParticipants();

}
