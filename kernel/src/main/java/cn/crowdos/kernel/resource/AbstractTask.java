package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.InterruptManager;
import cn.crowdos.kernel.constraint.Condition;
import cn.crowdos.kernel.constraint.Constraint;

import java.util.List;

public abstract class AbstractTask implements Task{

    protected final long taskId;
    private static long taskCounter = 1;
    protected final List<Constraint> constraints;
    protected final TaskDistributionType taskDistributionType;
    protected TaskStatus status;
    protected InterruptManager interruptManager;

    protected AbstractTask(List<Constraint> constraints, TaskDistributionType taskDistributionType) {
        this.taskId = taskCounter++;
        this.constraints = constraints;
        this.taskDistributionType = taskDistributionType;
    }


    abstract public Decomposer<Task> decomposer();

    public long getTaskId() {
        return this.taskId;
    }


    @Override
    public TaskDistributionType getTaskDistributionType() {
        return taskDistributionType;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return status;
    }

    @Override
    public void setTaskStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public List<Constraint> constraints() {
        return constraints;
    }

    @Override
    public boolean canAssignTo(Participant participant) {
        for (Constraint constraint : constraints) {
            Class<? extends Condition> conditionClass = constraint.getConditionClass();
            if(participant.hasAbility(conditionClass)){
                if (!constraint.satisfy(participant.getAbility(conditionClass)))
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean assignable() {
        return status == TaskStatus.READY;
    }

    @Override
    public boolean finished() {
        return status == TaskStatus.FINISHED;
    }

    @Override
    public void execute() {
        while (!isCompleted()){
            // Check if the task was interrupted
            if (interruptManager.isInterrupted(String.valueOf(taskId))) {
                System.out.println("Task " + taskId + " is interrupted.");
                // Handling interrupt logic
                handleInterrupt();
                return;
            }
            // The specific logic to perform the task
            performTask();
        }
    }

    public void handleInterrupt(){
        //Interrupt handling logic
    }
    public void performTask(){
        //The actual task executes the logic
    }
    public boolean isCompleted(){
        //Task execution process
        return true;
    }
}
