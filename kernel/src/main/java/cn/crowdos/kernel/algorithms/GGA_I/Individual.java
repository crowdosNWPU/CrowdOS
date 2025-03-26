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
package cn.crowdos.kernel.algorithms.GGA_I;

import cn.crowdos.kernel.system.resource.ParticipantPool;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;

/**
 * individual
 * Implement Serializable interface in order to "serialize deep copy"
 *
 * @author wushengjie
 */

public class Individual implements Serializable {


    //Chromosome sequence, which is the task allocation result
    private Map<Integer, List<Integer>> assignMap = new HashMap<>();

    //Number of workers
    private int workerNum;

    //Number of tasks
    private int taskNum;

    //How many workers are required to perform each task
    private int[] p;

    //How many tasks can each worker perform at most
    private int q;

    //Distance matrix
    double[][] distanceMatrix;

    //Task distance matrix
    double[][] taskDistanceMatrix;

    //Distance
    private double distance;

    //fitness (i.e., the shorter the total distance, the higher the fitness, fitness=1.0/distance)
    private double fitness;

    //Survival rate (higher fitness means higher survival rate)
    private double survivalRate;


    /**
     *
     * @param workerNum workerNum
     * @param taskNum taskNum
     * @param distanceMatrix distanceMatrix
     * @param taskDistanceMatrix taskDistanceMatrix
     * @param p p
     * @param q q
     */
    public Individual(int workerNum,int taskNum,double[][] distanceMatrix,double[][] taskDistanceMatrix,int[] p,int q) {

        this.workerNum = workerNum;
        this.taskNum = taskNum;
        this.distanceMatrix = distanceMatrix;
        this.taskDistanceMatrix = taskDistanceMatrix;
        this.p = p;
        this.q = q;


        //Initializing the Map
        for (int i = 0; i < workerNum; i++) {
            this.assignMap.put(i, new ArrayList<>());

        }

        initGenesByRandom();

    }

    /**
     *
     * @param workerNum workerNum
     * @param taskNum taskNum
     * @param distanceMatrix distanceMatrix
     * @param taskDistanceMatrix taskDistanceMatrix
     * @param p p
     * @param q q
     * @param greedyType greedyType
     */
    public Individual(int workerNum,int taskNum,double[][] distanceMatrix,double[][] taskDistanceMatrix,
                      int[] p,int q,int greedyType) {
        this.workerNum = workerNum;
        this.taskNum = taskNum;
        this.distanceMatrix = distanceMatrix;
        this.taskDistanceMatrix = taskDistanceMatrix;
        this.p = p;
        this.q = q;


        //Initializing the Map
        for (int i = 0; i < workerNum; i++) {
            this.assignMap.put(i, new ArrayList<>());

        }

        initGenesByNearestFirst();

    }

    /**
     * Initialize the genes randomly
     */
    public void initGenesByRandom() {

        Random random = new SecureRandom();
        int taskIndex;
        int workerIndex;

        //Assign tasks to workers
        for (int i = 0; i < taskNum; i++) {

            for (int j = 0; j < p[i]; j++) {
                //Select a worker at random
                workerIndex = random.nextInt(workerNum);


                if(assignMap.get(workerIndex).size() >= q || assignMap.get(workerIndex).contains(i)){
                    j--;

                }
                else {
                    assignMap.get(workerIndex).add(i);

                }

            }
        }


        //Iterate over the assignMap and shuffle the list
        for (int i = 0; i < workerNum; i++) {
            Collections.shuffle(assignMap.get(i));
        }


    }


    /**
     * Genes are initialized according to NearestFirst
     */
    public void initGenesByNearestFirst(){

        //Make a copy of the distance matrix
        double[][] distanceMatrixCopy = new double[workerNum][taskNum];
        for (int i = 0; i < workerNum; i++) {
            for (int j = 0; j < taskNum; j++) {
                distanceMatrixCopy[i][j] = distanceMatrix[i][j];
            }
        }
        NearestFirst nearestFirst = new NearestFirst(workerNum, taskNum, distanceMatrixCopy, p, q);
        nearestFirst.taskAssign();
        Map<Integer,List<Integer>> assignMapTemp = nearestFirst.getAssignMap();
        //Copy assignMapTemp to assignMap
        for (int i = 0; i < workerNum; i++) {
            for (int j = 0; j < assignMapTemp.get(i).size(); j++) {
                assignMap.get(i).add(assignMapTemp.get(i).get(j));
            }
        }

    }



    /**
     * The distance traveled and fitness of individuals are calculated
     */
    public void calculateDistanceAndFitness() {

        distance = 0;
        //Iterate over the assignMap and calculate the distance
        for (int i = 0; i < workerNum; i++) {
            List<Integer> taskList = assignMap.get(i);
            if(taskList.size() == 0){
                continue;
            }
            //Calculate the distance of worker i to the first task
            distance += distanceMatrix[i][taskList.get(0)];
            //Calculate the distance of worker i from the first task to the last task
            for (int j = 0; j < taskList.size() - 1; j++) {
                distance += taskDistanceMatrix[taskList.get(j)][taskList.get(j+1)];
            }
            //Calculate the distance of worker i from the last task to worker i
            distance += distanceMatrix[i][taskList.get(taskList.size()-1)];
        }

        //Calculating fitness
        fitness = 1.0 / distance;
    }

    /**
     * Mutation operation 1: Randomly swap a task of one worker with a task of another
     * worker currently buggy, trapped in an endless loop
     */
    public void mutation() {

        Random random = new SecureRandom();
        int workerIndex1;
        int workerIndex2;

        //A worker 1 is randomly selected and the task list is nonempty
        while(true){
            workerIndex1 = random.nextInt(workerNum);
            if (assignMap == null){
                    throw new NullPointerException("workerindex is null");
            }
            if (assignMap.get(workerIndex1).size() > 0){
                break;
            }
        }

        while(true) {
            //Choose a worker 2 at random
            workerIndex2 = random.nextInt(workerNum);

            //Avoid repeated selection
            while (workerIndex1 == workerIndex2) {
                workerIndex2 = random.nextInt(workerNum);
            }

            //Get the first task of worker 1
            int taskIndex = assignMap.get(workerIndex1).get(0);

            //If the task list of worker 2 does not contain the first task of worker 1,
            // then break out of the loop, otherwise continue selecting worker 2
            if (assignMap.get(workerIndex2).contains(taskIndex)) {
                continue;
            }
            assignMap.get(workerIndex2).add(0, taskIndex);
            break;
        }


    }


    /**
     *Mutation Operation 2: Randomly select a worker and shuffle its task list
     */
    public void mutation2() {
        Random random = new SecureRandom();
        int workerIndex;
        while(true){
            workerIndex = random.nextInt(workerNum);
            if (assignMap.get(workerIndex).size() > 0){
                break;
            }
        }
        Collections.shuffle(assignMap.get(workerIndex));
    }

    public Map<Integer, List<Integer>> getAssignMap() {
        return assignMap;
    }

    public void setAssignMap(Map<Integer, List<Integer>> assignMap) {
        this.assignMap = assignMap;
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

    public int[] getP() {
        return p;
    }

    public void setP(int[] p) {
        this.p = p;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getSurvivalRate() {
        return survivalRate;
    }

    public void setSurvivalRate(double survivalRate) {
        this.survivalRate = survivalRate;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "assignMap=" + assignMap +
                ", workerNum=" + workerNum +
                ", taskNum=" + taskNum +
                ", p=" + Arrays.toString(p) +
                ", q=" + q +
                ", distanceMatrix=" + Arrays.toString(distanceMatrix) +
                ", taskDistanceMatrix=" + Arrays.toString(taskDistanceMatrix) +
                ", distance=" + distance +
                ", fitness=" + fitness +
                ", survivalRate=" + survivalRate +
                '}';
    }
}
