package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.resource.ParticipantPool;
import cn.crowdos.kernel.resource.Task;

import java.util.LinkedList;
import java.util.List;

public class AlgoFactoryAdapter implements AlgoFactory{

    protected final SystemResourceCollection resourceCollection;

    public AlgoFactoryAdapter(SystemResourceCollection resourceCollection){
        this.resourceCollection = resourceCollection;
    }
    /**
     * The getParticipantSelectionAlgo function returns a ParticipantSelectionAlgo object that is used to select
     * participants for tasks. The algorithm implemented here selects all available participants who can perform the task.
     *
     * @return A new instance of ParticipantSelectionAlgo
     *
     */
    @Override
    public ParticipantSelectionAlgo getParticipantSelectionAlgo() {

        return new ParticipantSelectionAlgo() {
            final ParticipantPool participantPool;
            {
                participantPool = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();
            }
            @Override
            public List<Participant> getCandidates(Task task) {
                List<Participant> candidate = new LinkedList<>();
                for (Participant participant : participantPool) {
                    if (participant.available() && task.canAssignTo(participant)){
                        candidate.add(participant);
                    }
                }
                return candidate;
            }
        };
    }

    @Override
    public TaskRecommendationAlgo getTaskRecommendationAlgo() {
        return new TaskRecommendationAlgo() {
            final ParticipantPool participantPool;
            {
                participantPool = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();
            }
            @Override
            public List<Participant> getRecommendationScheme(Task task) {
                List<Participant> candidate = new LinkedList<>();
                for (Participant participant : participantPool) {
                    if (participant.available() && task.canAssignTo(participant)){
                        candidate.add(participant);
                    }
                }
                return candidate;
            }
        };
    }

    @Override
    public TaskAssignmentAlgo getTaskAssignmentAlgo() {
        return new TaskAssignmentAlgo() {
            final ParticipantPool participantPool;
            {
                participantPool = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();
            }
            @Override
            public List<Participant> getAssignmentScheme(Task task) {
                List<Participant> candidate = new LinkedList<>();
                for (Participant participant : participantPool) {
                    if (participant.available() && task.canAssignTo(participant)){
                        candidate.add(participant);
                    }
                }
                return candidate;
            }
        };
    }
}
