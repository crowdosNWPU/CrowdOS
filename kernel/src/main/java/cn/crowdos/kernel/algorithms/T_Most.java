/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University, Inc. <https://github.com/crowdosNWPU/CrowdOS>
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
 * @author wushengjie
 *
 * T_Most is a class that implements a greedy algorithm for task assignment.
 * It assigns tasks to workers based on the closest worker to each task, starting with the task that needs the largest number of workers.
 * The algorithm continues until all tasks have been assigned.
 *
 */

public class T_Most {

    private int workerNum;

    private int taskNum;

    private double[][] distanceMatrix;

    private double[][] taskDistanceMatrix;

    private int q;

    private int[] p;

    private Map<Integer, List<Integer>> assignMap = new HashMap<>();

    private final Double INF = Double.MAX_VALUE;

    private double distance;

    private final double[][] distanceMatrixTemp;

    /**
     * @param workerNum          Number of workers
     * @param taskNum            Number of tasks
     * @param distanceMatrix     Worker task distance matrix, which stores the distance between
     *                           a worker and a task (or the cost of a task for a worker)
     * @param taskDistanceMatrix Task distance matrix, which holds the distance between tasks and tasks
     * @param p                  Constraints, how many workers are needed for each task
     * @param q                  Constraints, how many tasks are assigned to each worker at most
     */
    public T_Most(int workerNum, int taskNum, double[][] distanceMatrix, double[][] taskDistanceMatrix, int[] p, int q) {
        this.workerNum = workerNum;
        this.taskNum = taskNum;
        this.distanceMatrix = distanceMatrix;
        this.taskDistanceMatrix = taskDistanceMatrix;
        //The diagonal elements of the task distance matrix are set to INF to facilitate the calculation
        for (int i = 0; i < this.taskDistanceMatrix.length; i++) {
            this.taskDistanceMatrix[i][i] = INF;
        }
        this.q = q;
        this.p = p;

        //Initializing the Map
        for (int i = 0; i < workerNum; i++) {
            this.assignMap.put(i, new ArrayList<>());
        }

        //Replication distance matrix
        distanceMatrixTemp = new double[workerNum][taskNum];
        for (int i = 0; i < workerNum; i++) {
            for (int j = 0; j < taskNum; j++) {
                distanceMatrixTemp[i][j] = distanceMatrix[i][j];
            }
        }

    }

    /**

     *Assign tasks to workers using a greedy algorithm.
     *This method assigns tasks to workers based on the closest worker to each task,
     *starting with the task that needs the largest number of workers. The algorithm
     *continues until all tasks have been assigned.
     *@throws IllegalArgumentException if the number of tasks is less than or equal to zero,
     *or the number of workers is less than or equal to zero.
     */
    public void taskAssign() {


        // Initialize variables
        int taskIndex;
        int workerIndex;


        //Check that all tasks have been assigned
        while (!isTaskAssignFinish()) {

            //The task that needs the largest number of workers is selected as the initial task
            int maxNum = -1;
            int maxNumIndex = -1;
            for (int i = 0; i < taskNum; i++) {
                if ((p[i] - countTaskIndex(i)) > maxNum) {
                    maxNum = p[i] - countTaskIndex(i);
                    maxNumIndex = i;
                }
            }
            taskIndex = maxNumIndex;


            //The worker closest to the initial task is selected
            workerIndex = findMinWorkerToTask(taskIndex);


            if (workerIndex == -1) {
                continue;
            }

            if (isAssignWorker(workerIndex) || isAssignTask(taskIndex)) {
                continue;
            }

            //Add assignment result to the map
            assignMap.get(workerIndex).add(taskIndex);
            //Reset the matrix
            distanceMatrix[workerIndex][taskIndex] = INF;

            //Counter
            int count = 0;

            while (!isTaskAssignFinish() || isAssignWorker(workerIndex)) {

                //If there are 5 tasks, select 4 tasks one by one
                if (count == taskNum - 1) {
                    break;
                }

                int[] taskIndexArray = findMinTaskToTask(taskIndex);

                int taskIndex_ = taskIndexArray[count];
                count++;

                //Check if the worker can still accept the task and if the task still needs workers
                if (isAssignTask(taskIndex_) || isAssignWorker(workerIndex)) {
                    continue;
                }

                //Add assignment result to the map
                assignMap.get(workerIndex).add(taskIndex_);
                //Reset the matrix
                distanceMatrix[workerIndex][taskIndex_] = INF;
                isAssignTask(taskIndex_);
            }

        }

        countDistance();
    }

    /**
     * The value -1 is returned if it was not found
     * Finds the index of the worker who is closest to the given task.
     * @param taskIndex the index of the task for which to find the closest worker
     * @return the index of the closest worker
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
     * Find the index of tasks sorted in ascending order based on their distance to the given task.
     *
     * @param taskIndex the index of the task to which the distances are calculated.
     *
     * @return an array containing the task indices sorted in ascending order
     * based on their distance to the given task.
     */
    private int[] findMinTaskToTask(int taskIndex) {

        //Array to store the task indices
        int[] taskIndexArray = new int[taskNum];

        Map<Integer, Double> taskDistanceMap = new HashMap<>();
        // Store the distances of all tasks to the given task in the map
        for (int i = 0; i < taskNum; i++) {
            taskDistanceMap.put(i, taskDistanceMatrix[taskIndex][i]);
        }

        //Sort the map based on the values (i.e., distances)
        Map<Integer, Double> sortMap = sortMap(taskDistanceMap);


        //Traverse the sorted map and store the corresponding task indices in the taskIndexArray
        int count = 0;
        for (Map.Entry<Integer, Double> entry : sortMap.entrySet()) {
            taskIndexArray[count] = entry.getKey();
            count++;
        }

        return taskIndexArray;
    }


    /**
     * 1、Converts the entrySet of the Map to a List
     * 2、sort with the sort method of the Collections utility class
     * 3、Iterate over the sorted list and put each set of keys and values into
     * a LinkedHashMap(the only Map implementation class that stores the values
     * in order of insertion is LinkedHashMap).
     *
     * @param map the map to be sorted
     *
     * @return a LinkedHashMap containing the sorted entries
     */
    public Map<Integer, Double> sortMap(Map<Integer, Double> map) {

        // Convert the map to a list of entries and sort it by value
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
     * Print distance matrix
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
        return "T_Most{" +
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
