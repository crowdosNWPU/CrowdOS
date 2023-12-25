package cn.crowdos.kernel.algorithms;

import cn.crowdos.kernel.constraint.Constraint;
import cn.crowdos.kernel.constraint.Coordinate;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.resource.ParticipantPool;
import cn.crowdos.kernel.constraint.POIConstraint;

import java.util.*;
import java.util.stream.Collectors;


public class T_MostFactory extends AlgoFactoryAdapter {

    public T_MostFactory(SystemResourceCollection resourceCollection) {
        super(resourceCollection);
    }

    /**
     * The getParticipantSelectionAlgo function returns a T_Most ParticipantSelectionAlgo object that is used to select
     * participants for tasks. The algorithm implemented here selects all available participants who can perform the task.
     *
     * @return A new instance of ParticipantSelectionAlgo
     *
     */
    @Override
    public TaskAssignmentAlgo getTaskAssignmentAlgo() {
        return new TaskAssignmentAlgo() {
            @Override
            public List<List<Participant>> getAssignmentScheme(ArrayList<Task> tasks) {
                ParticipantPool participants = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();


                //Location information for all tasks
                List<Coordinate> taskLocations = new ArrayList<>();
                //Location information of all candidates
                List<Coordinate> candidateLocations = new ArrayList<>();
                //reserve All candidates
                List<Participant> candidates = new ArrayList<>();

                //Iterate over all tasks
                //The task constraints required in the algorithm are obtained, where is the task location
                for (Task task : tasks){
                    List<Constraint> taskConstraint = task.constraints().stream()
                            .filter(constraint -> constraint instanceof POIConstraint)
                            .collect(Collectors.toList());
                    if(taskConstraint.size() != 1){
                        return null;
                    }
                    Coordinate taskLocation = (Coordinate) taskConstraint.get(0);
                    //add taskLocation to taskLocations
                    taskLocations.add(taskLocation);
                }


                for (Participant participant:participants){
                    for (Task task:tasks){
                        if(!task.canAssignTo(participant)){
                            continue;
                        }
                        candidates.add(participant);
                        break;
                    }
                }

                //Get the number of tasks and the number of workers.
                int taskNum = tasks.size();
                int workerNum = candidates.size();

                //Iterate through the candidates to add their location information to candidateLocations
                for (Participant candidate : candidates){
                    Coordinate candidateLocation = (Coordinate) candidate.getAbility(Coordinate.class);
                    candidateLocations.add(candidateLocation);
                }

                //Compute task-worker distance matrix
                double[][] distanceMatrix = new double[workerNum][taskNum];
                //Compute task-task distance matrix
                double[][] taskDistanceMatrix = new double[taskNum][taskNum];

                //Calculate the task-worker distance matrix based on candidateLocations and taskLocations
                for (int i = 0; i < workerNum; i++){
                    for (int j = 0; j < taskNum; j++){
                        distanceMatrix[i][j] = candidateLocations.get(i).euclideanDistance(taskLocations.get(j));
                    }
                }

                //Calculate the task distance matrix based on taskLocations
                for (int i = 0; i < taskNum; i++){
                    for (int j = 0; j < taskNum; j++){
                        taskDistanceMatrix[i][j] = taskLocations.get(i).euclideanDistance(taskLocations.get(j));
                    }
                }

                //By default, only one participant is required per task
                int[] p =new int[taskNum];
                for (int i =0; i < taskNum; i++){
                    p[i] = 1;
                }

                //Compute task-worker distance matrix
                T_Most t_most = new T_Most(workerNum, taskNum, distanceMatrix, taskDistanceMatrix, p, 1);
                t_most.taskAssign();

                //Save the task assignment results
                List<List<Participant>> assignmentScheme = new ArrayList<>(taskNum);
                Map<Integer, List<Participant>> taskToParticipants = new HashMap<>();


                /*
                 *Add the data from t_most.getAssignMap() to assignmentScheme,
                 * because the original algorithm result is map<worker, List<task>>,
                 * but our kernel result is the worker corresponding to the task,
                 * so it needs to be converted
                 **/
                for (Map.Entry<Integer, List<Integer>> entry : t_most.getAssignMap().entrySet()){
                    Participant participant = candidates.get(entry.getKey());
                    List<Integer> m_tasks = entry.getValue();//tasks list

                    for (Integer taskId : m_tasks){
                        List<Participant> m_participants = taskToParticipants.getOrDefault(taskId,new ArrayList<>());
                        m_participants.add(participant);
                        taskToParticipants.put(taskId,m_participants);
                    }

                }
                Set<Integer> set= taskToParticipants.keySet();
                Object[] arr = set.toArray();
                Arrays.sort(arr);
                for (Object taskID : arr){
                    assignmentScheme.add(taskToParticipants.get(Integer.parseInt(taskID.toString())));
                }
                return assignmentScheme;
            }

            @Override
            public List<Participant> getAssignmentScheme(Task task) {
                // prepare args
                ParticipantPool participants = resourceCollection.getResourceHandler(ParticipantPool.class).getResourceView();

                int taskNum = 1;

                List<Constraint> taskLocation = task.constraints().stream()
                        .filter(constraint -> constraint instanceof POIConstraint)
                        .collect(Collectors.toList());
                if (taskLocation.size() != 1) {
                    return null;
                }
                List<Participant> candidate = participants.stream()
                        .filter(task::canAssignTo)
                        .collect(Collectors.toList());
                int workerNum = candidate.size();


                Coordinate tLocation = (Coordinate) taskLocation.get(0);
                double[][] distanceMatrix = new double[workerNum][taskNum];
                for (int i = 0; i < candidate.size(); i++) {
                    Participant worker = candidate.get(i);
                    Coordinate wLocation = (Coordinate) worker.getAbility(Coordinate.class);
                    distanceMatrix[i][0] = wLocation.euclideanDistance(tLocation);
                }

                double[][] taskDistanceMatrix = new double[][]{{1}};


                // create algorithm instance
                T_Most t_most = new T_Most(workerNum, taskNum, distanceMatrix, taskDistanceMatrix, new int[]{1}, 1);
                t_most.taskAssign();

                // parser result
                List<Participant> assignmentScheme = new ArrayList<>();
                t_most.getAssignMap().keySet().forEach(participantId -> assignmentScheme.add(participants.get(participantId)));
                return assignmentScheme;
            }
        };

    }

}
