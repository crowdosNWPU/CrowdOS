package cn.crowdos.kernel;

import java.util.HashMap;
import java.util.Map;

/**
 * InterruptManager Interrupt requests for managing tasks
 */
public class InterruptManager {
    private Map<String, Boolean> interruptFlags;

    /**
     * Constructor to initialize the HashMap of the interrupt flag.
     */
    public InterruptManager() {
        interruptFlags = new HashMap<>();
    }

    /**
     * Request to interrupt a specific task, and set the interrupt flag corresponding to the task ID to true.
     *
     * @param taskId taskID
     */
    public void requestInterrupt(String taskId) {
        interruptFlags.put(taskId, true);
    }

    /**
     * Checks if a particular task was interrupted and returns false if the task ID does not exist.
     *
     * @param taskId taskID
     * @return If the task was interrupted, true is returned. Otherwise, false is returned.
     */
    public boolean isInterrupted(String taskId) {
        return interruptFlags.getOrDefault(taskId, false);
    }

    /**
     * Clears the interrupt flag for a specific task.
     *
     * @param taskId taskID
     */
    public void clearInterrupt(String taskId) {
        interruptFlags.remove(taskId);
    }
}
