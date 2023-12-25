package cn.crowdos.kernel.algorithms.GGA_I;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * Greedy based task allocation algorithm NearestFirst
 * GGA-I is used to initialize the population
 * In the distance matrix, row represents workers and col represents tasks
 *
 *  @author wushengjie
 */
public class NearestFirst {

    private final int workerNum;

    private final int taskNum;
    private final double[][] distanceMatrix;

    private int q = 3;
    private final int[] p;

    //Task allocation results
    private final Map<Integer, List<Integer>> assignMap = new HashMap<>();


    public NearestFirst(int workerNum,int taskNum, double[][] distanceMatrix,int[] p , int q) {
        this.workerNum = workerNum;
        this.taskNum = taskNum;
        this.distanceMatrix = distanceMatrix;
        this.q = q;
        this.p = p;

        //Initializing the Map
        for (int i = 0; i < workerNum; i++) {
            this.assignMap.put(i, new ArrayList<>());
        }
    }


    /**
     * Algorithm body function
     */
    public void taskAssign() {

        int[] index;

        //Worker serial number
        int workerIndex;

        //Task sequence number
        int taskIndex;

        while (true) {

            //Check that all tasks have been assigned
            if (allTaskFinish()) {
                break;
            }

            index = findMinIndex();
            workerIndex = index[0];
            taskIndex = index[1];

            //Check whether the worker is still able to perform the task and whether the task still needs workers
            double INF = Double.MAX_VALUE;
            if(assignMap.get(workerIndex).size() >= q || countTaskIndex(taskIndex) == p[taskIndex]){

                //The worker cannot accept any more tasks, so set the elements of this row to the maximum value
                if (assignMap.get(workerIndex).size() >= q){

                    for (int i = 0; i < taskNum; i++) {
                        distanceMatrix[workerIndex][i] = INF;
                    }
                }
                //The task is no longer a worker, and the column element is set to maximum
                if (countTaskIndex(taskIndex) == p[taskIndex]){

                    for (int i = 0; i < workerNum; i++) {
                        distanceMatrix[i][taskIndex] = INF;
                    }
                }
                //Skip the current loop without assigning tasks
                continue;
            }

            //Join the allocation result Map
            assignMap.get(workerIndex).add(taskIndex);
            //Reset matrix
            distanceMatrix[workerIndex][taskIndex] = INF;

        }

    }

    /**
     * Query the number of times the task appears in all lists of the Map,
     * that is, how many workers the task was assigned to
     *
     * @param taskIndex  taskIndex
     * @return int how many workers the task was assigned to
     */
    public int countTaskIndex(int taskIndex) {
        int count = 0;
        for (Map.Entry<Integer, List<Integer>> entry : assignMap.entrySet()) {
            if (entry.getValue().contains(taskIndex)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Check that all tasks have been assigned
     *
     * @return boolean
     */
    public boolean allTaskFinish(){
        for (int i = 0; i < taskNum; i++) {
            if (countTaskIndex(i) < p[i]){
                return false;
            }
        }
        return true;
    }


    /**
     * Returns the index of the smallest value in the distance matrix
     *
     * @return {@code int[]}
     */
    public int[] findMinIndex() {
        int[] index = new int[2];
        double min = distanceMatrix[0][0];
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                if (distanceMatrix[i][j] < min) {
                    min = distanceMatrix[i][j];
                    index[0] = i;
                    index[1] = j;
                }
            }
        }
        return index;
    }


    /**
     *Print the assignment result
     */
    public void printAssignMap() {
        for (Map.Entry<Integer, List<Integer>> entry : assignMap.entrySet()) {
            System.out.println("worker" + entry.getKey() + "The assigned task is：" + entry.getValue());
        }
    }

    /**
     *Print distance matrix
     */
    public void printDistanceMatrix(){
        System.out.println("The current distance matrix is------------------------------------");
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                System.out.print(distanceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Map<Integer, List<Integer>> getAssignMap() {
        return assignMap;
    }
}








