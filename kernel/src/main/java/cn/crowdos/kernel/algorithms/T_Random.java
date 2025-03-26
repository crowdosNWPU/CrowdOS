/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University. <https://github.com/crowdosNWPU/CrowdOS>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 */
package cn.crowdos.kernel.algorithms;

import java.util.*;

/**
 * This class represents a random task assignment algorithm for the multi-worker multi-task problem.
 * It randomly assigns tasks to workers based on a given distance matrix between workers and tasks, and a given
 * distance matrix between tasks.
 *
 * @author wushengjie
 */
public class T_Random {

    private int workerNum;

    private int taskNum;

    private double[][] distanceMatrix;

    private double[][] taskDistanceMatrix;

    private int q;

    private int[] p;

    private Map<Integer, List<Integer>> assignMap = new HashMap<>();

    private final Double INF = Double.MAX_VALUE;

    private double distance;

    private double[][] distanceMatrixTemp;

    /**
     * Constructs a new T_Random object with the given parameters.
     *
     * @param workerNum the number of workers
     * @param taskNum the number of tasks
     * @param distanceMatrix the distance matrix between workers and tasks
     * @param taskDistanceMatrix the distance matrix between tasks
     * @param p the number of tasks assigned to each worker
     * @param q the number of workers assigned to each task
     */
    public T_Random(int workerNum, int taskNum, double[][] distanceMatrix, double[][] taskDistanceMatrix, int[] p, int q) {
        //initialize instance variables
        this.workerNum = workerNum;
        this.taskNum = taskNum;
        this.distanceMatrix = distanceMatrix;
        this.taskDistanceMatrix = taskDistanceMatrix;
        ////set diagonal elements of taskDistanceMatrix to INF for convenience
        for (int i = 0; i < this.taskDistanceMatrix.length; i++) {
            this.taskDistanceMatrix[i][i] = INF;
        }
        this.q = q;
        this.p = p;

        //initialize assignMap
        for (int i = 0; i < workerNum; i++) {
            this.assignMap.put(i, new ArrayList<>());
        }

        //copy distanceMatrix to distanceMatrixTemp
        distanceMatrixTemp = new double[workerNum][taskNum];
        for (int i = 0; i < workerNum; i++) {
            for (int j = 0; j < taskNum; j++) {
                distanceMatrixTemp[i][j] = distanceMatrix[i][j];
            }
        }
    }

    /**
     * Assign tasks to workers randomly and greedily based on the distance between tasks and workers.
     * This method uses a random starting task and assigns the closest worker to that task.
     * Then, it iteratively assigns the closest task to the current worker until the worker is assigned the maximum
     * number of tasks or no more available tasks can be assigned.
     * The method also updates the distance matrix and assigns the results to the {@code assignMap} map.
     * Finally, it calculates the total distance of the assignments and updates the {@code totalDistance} field.
     */
    public void taskAssign() {


        int taskIndex;
        int workerIndex;


        //Check that all tasks have been assigned
        while (!isTaskAssignFinish()) {


            //Randomly select the initial task among the tasks that need workers
            List<Integer> randomTaskList = new ArrayList<>();
            for (int i = 0; i < taskNum; i++) {
                if (countTaskIndex(i) < p[i]) {
                    randomTaskList.add(i);
                }
            }
            Collections.shuffle(randomTaskList);
            taskIndex = randomTaskList.get(0);


            //The worker closest to the initial task is selected
            workerIndex = findMinWorkerToTask(taskIndex);


            if (workerIndex == -1) {
                continue;
            }


            if (isAssignWorker(workerIndex) || isAssignTask(taskIndex)) {
                continue;
            }

            //Join the allocation result Map
            assignMap.get(workerIndex).add(taskIndex);
            //Reset matrix
            distanceMatrix[workerIndex][taskIndex] = INF;

            //A counter
            int count = 0;

            while (!isTaskAssignFinish() || isAssignWorker(workerIndex)) {

                //If there are five tasks, select four tasks in turn
                if (count == taskNum - 1) {
                    break;
                }

                int[] taskIndexArray = findMinTaskToTask(taskIndex);


                int taskIndex_ = taskIndexArray[count];
                count++;


                //Check whether the worker can still accept the task and whether the task still needs workers
                if (isAssignTask(taskIndex_) || isAssignWorker(workerIndex)) {
                    continue;
                }

                //Join the allocation result Map
                assignMap.get(workerIndex).add(taskIndex_);

                //Reset matrix
                distanceMatrix[workerIndex][taskIndex_] = INF;

                isAssignTask(taskIndex_);
            }

        }

        countDistance();
    }

    /**
     * Finds the index of the worker who is closest to the given task.
     *
     * @param taskIndex the index of the task for which the closest worker is being searched
     *
     * @return the index of the closest worker, or -1 if no worker is available
     */
    private int findMinWorkerToTask(int taskIndex) {
        double min = INF;
        int minIndex = -1;
        for (int i = 0; i < workerNum; i++) {
            if (distanceMatrix[i][taskIndex] < min) {
                min = distanceMatrix[i][taskIndex];
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * Find the tasks that are closest to the given task.
     *
     * @param taskIndex the index of the given task
     *
     * @return an array of task indices, sorted in ascending order of their distances to the given task
     */
    private int[] findMinTaskToTask(int taskIndex) {

        // Create an array to store task indices
        int[] taskIndexArray = new int[taskNum];

        Map<Integer, Double> taskDistanceMap = new HashMap<>();

        // Populate the map with task indices and their corresponding distances to the given task
        for (int i = 0; i < taskNum; i++) {
            taskDistanceMap.put(i, taskDistanceMatrix[taskIndex][i]);
        }

        //Sort the map by value in ascending order
        Map<Integer, Double> sortMap = sortMap(taskDistanceMap);


        // Traverse the sorted map and store the corresponding task indices in the array
        int count = 0;
        for (Map.Entry<Integer, Double> entry : sortMap.entrySet()) {
            taskIndexArray[count] = entry.getKey();
            count++;
        }


        return taskIndexArray;
    }


    /**
     * Sorts the given map by value in ascending order and returns a LinkedHashMap with the sorted entries.
     *  1、Converts the entrySet of the Map to a List
     *  2、sort with the sort method of the Collections utility class
     *  3、Iterate over the sorted list and put each set of keys and values
     *  into a LinkedHashMap(the only Map implementation class that stores the values
     *  in order of insertion is LinkedHashMap).
     *
     * @param map the map to be sorted
     *
     * @return a LinkedHashMap with the sorted entries
     */

    public Map<Integer, Double> sortMap(Map<Integer, Double> map) {

        //Convert the map into a list of entries and sort it by value in ascending order
        List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(map.entrySet());

        //sort the list using the Collections sort method
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                //Positive order, reverse order
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        //As you iterate through the sorted list, it's important to include the LinkedHashMap,
        // because only the LinkedHashMap is stored in the order in which it was inserted
        LinkedHashMap<Integer, Double> linkedHashMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> e : entryList
        ) {
            linkedHashMap.put(e.getKey(), e.getValue());
        }

        return linkedHashMap;
    }

    /**
     * Check if the worker is already assigned with q number of tasks.
     * If the worker is assigned with q number of tasks, set the distance between this worker and all tasks to INF (infinite).
     *
     * @param workerIndex the index of the worker to be checked
     *
     * @return true if the worker is already assigned with q number of tasks, false otherwise
     */
    public boolean isAssignWorker(int workerIndex) {

        if (assignMap.get(workerIndex).size() >= q) {

            for (int i = 0; i < taskNum; i++) {
                distanceMatrix[workerIndex][i] = INF;
            }
            return true;
        }
        return false;
    }

    /**
     * Checks whether the given task has been assigned to enough workers based on its required number of workers.
     * If so, updates the distance matrix by setting the distance to INF for all workers to that task.
     *
     * @param taskIndex The index of the task to check.
     *
     * @return True if the task has been assigned to enough workers, false otherwise.
     */
    public boolean isAssignTask(int taskIndex) {
        if (countTaskIndex(taskIndex) == p[taskIndex]) {
            for (int i = 0; i < workerNum; i++) {
                distanceMatrix[i][taskIndex] = INF;
            }
            return true;
        }
        return false;
    }

    /**
     * Counts the number of workers that have been assigned a particular task.
     *
     * @param taskIndex The index of the task to count the number of workers for.
     *
     * @return The number of workers that have been assigned the specified task.
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
     * Checks whether all tasks have been assigned to the required number of workers or not.
     *
     * @return true if all tasks have been assigned to the required number of workers, false otherwise.
     */
    public boolean isTaskAssignFinish() {
        for (int i = 0; i < taskNum; i++) {
            if (countTaskIndex(i) < p[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Print the assignment result
     */
    public void printAssignMap() {
        for (Map.Entry<Integer, List<Integer>> entry : assignMap.entrySet()) {
            System.out.println("worker" + entry.getKey() + "The assigned task is：" + entry.getValue());
        }
    }

    /**
     * Print the assignment result
     */
    public void printDistanceMatrix() {
        System.out.println("The current distance matrix is------------------------------------");
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                System.out.print(distanceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Calculate the total distance the worker needs to travel to complete the task
     */
    public void countDistance() {
        distance = 0;
        //Iterate over the assignMap and calculate the distance
        for (int i = 0; i < workerNum; i++) {
            List<Integer> taskList = assignMap.get(i);
            if (taskList.size() == 0) {
                continue;
            }
            //Calculate the distance of worker i to the first task
            distance += distanceMatrixTemp[i][taskList.get(0)];
            //Calculate the distance of worker i from the first task to the last task
            for (int j = 0; j < taskList.size() - 1; j++) {
                distance += taskDistanceMatrix[taskList.get(j)][taskList.get(j + 1)];
            }
            //Calculate the distance of worker i from the last task to worker i
            distance += distanceMatrixTemp[i][taskList.get(taskList.size() - 1)];
        }
    }


    public int getWorkerNum() {
        return workerNum;
    }

    public void setWorkerNum(int workerNum) {
        this.workerNum = workerNum;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public double[][] getTaskDistanceMatrix() {
        return taskDistanceMatrix;
    }

    public void setTaskDistanceMatrix(double[][] taskDistanceMatrix) {
        this.taskDistanceMatrix = taskDistanceMatrix;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int[] getP() {
        return p;
    }

    public void setP(int[] p) {
        this.p = p;
    }

    public Map<Integer, List<Integer>> getAssignMap() {
        return assignMap;
    }

    public void setAssignMap(Map<Integer, List<Integer>> assignMap) {
        this.assignMap = assignMap;
    }

    public double getINF() {
        return INF;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "T_Random{" +
                "workerNum=" + workerNum +
                ", taskNum=" + taskNum +
                ", distanceMatrix=" + Arrays.toString(distanceMatrix) +
                ", taskDistanceMatrix=" + Arrays.toString(taskDistanceMatrix) +
                ", q=" + q +
                ", p=" + Arrays.toString(p) +
                ", assignMap=" + assignMap +
                ", INF=" + INF +
                '}';
    }
}


