/*
 * Copyright 2019-2025 CrowdOS_Group, Inc. <https://github.com/crowdosNWPU/CrowdOS>
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

import java.io.*;
import java.security.SecureRandom;
import java.util.*;



/**
 * GGA-IAlgorithm body
 *
 * @author wushengjie
 */
public class GGA_I_Main {

    private int workerNum;

    private int taskNum;

    private double[][] distanceMatrix;

    private double[][] taskDistanceMatrix;

    private int q;

    private int[] p;

    private double threshold = 10000;
    private int populationNum = 100;

    private int maxGen = 300;
    private double crossRate = 0.98;
    private double mutateRate = 0.4;

    private final double INF = Double.MAX_VALUE;

    private Map<Integer, List<Integer>> assignMap = new HashMap<>();


    /**
     * @param workerNum          Number of workers
     * @param taskNum            Number of tasks
     * @param distanceMatrix     Worker task distance matrix, which stores the distance
     *                           between a worker and a task (or the cost of a task for a worker)
     * @param taskDistanceMatrix Task distance matrix, which holds the distance between tasks and tasks
     * @param p                  Constraints, how many workers are needed for each task
     * @param q                  Constraints, how many tasks are assigned to each worker at most
     */
    public GGA_I_Main(int workerNum, int taskNum, double[][] distanceMatrix, double[][] taskDistanceMatrix, int[] p, int q) {
        this.workerNum = workerNum;
        this.taskNum = taskNum;
        this.distanceMatrix = distanceMatrix;
        this.taskDistanceMatrix = taskDistanceMatrix;
        //Set the diagonal elements of the task matrix to INF
        for (int i = 0; i < taskNum; i++) {
            this.taskDistanceMatrix[i][i] = INF;
        }
        this.q = q;
        this.p = p;
    }

    /**
     * Main function of the algorithm
     */
    public void taskAssign() {

        //Variable declaration
        //population
        List<Individual> population = new ArrayList<>();
        //Random number tool
        Random random = new SecureRandom();
        //Start computation time
        long startTime = System.currentTimeMillis();
        //Store the best individual
        Individual bestIndividual = null;

        //solve
        //Initializing the population
        initPopulation(population);

        //Iterative optimization
        for (int t = 0; t < maxGen; t++) {


            //Selection
            Individual localBestIndividual = select(population, random);
            if (bestIndividual == null || localBestIndividual.getDistance() < bestIndividual.getDistance()) {
                bestIndividual = localBestIndividual;

            }
            //Cross over
            crossover(population, random);

            //mutation
            mutate(population, random);



        }



        //Reproduction bestIndividual. GetAssignMap () to assignMap
        assignMap = new HashMap<>(bestIndividual.getAssignMap());
    }

    /**
     * Initializing the population
     *
     * @param population The initial population list
     */
    public void initPopulation(List<Individual> population) {
        for (int i = 0; i < populationNum; i++) {
            if (i == 0) {
                population.add(new Individual(workerNum, taskNum, distanceMatrix, taskDistanceMatrix, p, q, 0));
            } else {
                population.add(new Individual(workerNum, taskNum, distanceMatrix, taskDistanceMatrix, p, q));
            }

        }
    }

    /**
     * The fitness and survival rate of each individual in the population are calculated
     * Select the best individual (highest fitness)
     *
     * @param population Changing population
     * @return {@code Individual}
     */
    public Individual calculateFitnessAndSurvivalRateOfIndividualAndChooseBestOne(List<Individual> population) {
        //variable declaration
        //The total fitness of all individuals in the population is stored
        double totalFitness = 0;
        //Store the optimal fitness value
        double bestFitness = -Double.MAX_VALUE;
        //Store the best individual
        Individual bestIndividual = null;

        //Calculate the fitness of the individual and select the best individual
        for (Individual individual : population) {
            individual.calculateDistanceAndFitness();
            totalFitness += individual.getFitness();
            if (individual.getFitness() > bestFitness) {
                bestFitness = individual.getFitness();
                bestIndividual = individual;
            }
        }
        //The best individual is removed from the population
        population.remove(bestIndividual);
        //The fitness value corresponding to the best individual is deleted
        totalFitness -= bestIndividual.getFitness();

        //Calculate the survival rate of the remaining individuals in the population
        for (Individual individual : population) {
            //Individual survival rate = individual fitness/total population fitness
            individual.setSurvivalRate(individual.getFitness() / totalFitness);
        }

        return bestIndividual;
    }

    /**
     * Select excellent individuals, select the individual with the highest fitness value,
     * copy several individuals, and use roulette to select the remaining places from the remaining
     * individuals of the population
     *
     * @param population Incoming population
     * @param random Probability of choice
     * @return {@code Individual}
     */
    public Individual select(List<Individual> population, Random random) {

        //New population
        List<Individual> newPopulation = new ArrayList<>();
        //The best individual is replicated several times
        int cloneNumOfBestIndividual = 3;
        //At least once, otherwise it is not added to the new population

        //Select the individual, the selection method:
        // 1, select the best individual of the population, and then replicate several times;
        // 2, roulette selects the remaining individuals
        //The fitness and survival rate of each individual in the population are calculated and the best individual is selected
        Individual bestIndividual = calculateFitnessAndSurvivalRateOfIndividualAndChooseBestOne(population);

        for (int i = 0; i < cloneNumOfBestIndividual; i++) {

            newPopulation.add((Individual) deepClone(bestIndividual));
        }


        for (int i = 0; i < (populationNum - cloneNumOfBestIndividual); i++) {
            double p = random.nextDouble();
            double sumP = 0;
            for (Individual individual : population) {
                sumP += individual.getSurvivalRate();
                if (sumP >= p) {
                    //Selecting individuals
                    newPopulation.add((Individual) deepClone(individual));
                    break;
                }
            }
        }

        population.clear();
        population.addAll(newPopulation);


        return bestIndividual;
    }

    /**
     * Cross over
     * When the random number When pcl is smaller than pch,
     * two individuals are randomly found out for gene crossover and exchange
     *
     * @param population population
     * @param random random
     */
    public void crossover(List<Individual> population, Random random) {

        double p = random.nextDouble();

        if (p < crossRate) {

            //Find two indexes randomly, and find two individuals randomly in the population
            int i = random.nextInt(population.size());
            int j = random.nextInt(population.size());
            while (i == j) {
                j = random.nextInt(population.size());
            }
            Individual individualI = population.get(i);
            Individual individualJ = population.get(j);


            //Find a random index, that is, find a random gene segment
            int task = random.nextInt(taskNum);

            //In individual 1, which workers are there for this task
            List<Integer> workerList1 = new ArrayList<>();
            //In individual 2, which workers are there for this task
            List<Integer> workerList2 = new ArrayList<>();
            for (int k = 0; k < workerNum; k++) {
                if (individualI.getAssignMap().get(k).contains(task)) {
                    workerList1.add(k);
                }
                if (individualJ.getAssignMap().get(k).contains(task)) {
                    workerList2.add(k);
                }
            }
            //Swap the elements of workerList1 and workerList2, that is, swap the workers of the two tasks
            for (int k = 0; k < workerList1.size(); k++) {
                int worker1 = workerList1.get(k);
                individualI.getAssignMap().get(worker1).remove((Integer) task);
            }
            for (int k = 0; k < workerList2.size(); k++) {
                int worker2 = workerList2.get(k);
                individualJ.getAssignMap().get(worker2).remove((Integer) task);
            }

            for (int k = 0; k < workerList1.size(); k++) {
                int worker1 = workerList1.get(k);
                individualJ.getAssignMap().get(worker1).add(task);
            }
            for (int k = 0; k < workerList2.size(); k++) {
                int worker2 = workerList2.get(k);
                individualI.getAssignMap().get(worker2).add(task);
            }

            //After crossover, it may happen that the number of tasks of workers exceeds the upper limit of 1,
            // so it needs to be repaired
            repairIndividual(individualI);
            repairIndividual(individualJ);


            population.set(i, individualI);
            population.set(j, individualJ);
        }
    }

    /**
     * The individuals are crossed and repaired to satisfy the constraints
     *
     * @param individual individual
     */
    public void repairIndividual(Individual individual) {

        Random random = new SecureRandom();

        for (int i = 0; i < workerNum; i++) {
            List<Integer> taskList = individual.getAssignMap().get(i);
            if (taskList.size() > q) {
                //If the worker has more than one task, which can be the maximum number,
                // then a random task is removed
                int index = random.nextInt(taskList.size());
                taskList.remove(index);
            }
        }

        //The unassigned tasks are assigned to the workers
        int count = 0;
        int workerIndex;
        for (int i = 0; i < taskNum; i++) {
            count = 0;
            for (Map.Entry<Integer, List<Integer>> entry : individual.getAssignMap().entrySet()) {
                if (entry.getValue().contains(i)) {
                    count++;
                }
            }
            //If the task is not fully assigned
            while (count < p[i]) {
                workerIndex = random.nextInt(workerNum);
                if (individual.getAssignMap().get(workerIndex).size() >= q ||
                        individual.getAssignMap().get(workerIndex).contains(i)) {
                    continue;
                }
                individual.getAssignMap().get(workerIndex).add(i);
                break;
            }
        }
    }


    /**
     * Mutation: Randomly swapping two elements of an individual gene
     *
     * @param population population
     * @param random random
     */
    public void mutate(List<Individual> population, Random random) {
        for (Individual individual : population) {
            //When the random number is less than the mutation probability,
            // the genes of the individual are mutated
            if (random.nextDouble() < mutateRate) {
                //Two elements in an individual gene are exchanged
                individual.mutation2();

            }
        }
    }

    /**
     * Deep copy object
     *
     * @param srcObject srcObject
     * @return Object
     */
    public Object deepClone(Object srcObject) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        Object result = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            //Write objects to streams
            oos.writeObject(srcObject);
            //Read back from the stream
            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);
            result = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert bos != null;
                bos.close();
                assert oos != null;
                oos.close();
                assert bis != null;
                bis.close();
                assert ois != null;
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
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

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public int getPopulationNum() {
        return populationNum;
    }

    public void setPopulationNum(int populationNum) {
        this.populationNum = populationNum;
    }

    public int getMaxGen() {
        return maxGen;
    }

    public void setMaxGen(int maxGen) {
        this.maxGen = maxGen;
    }

    public double getCrossRate() {
        return crossRate;
    }

    public void setCrossRate(double crossRate) {
        this.crossRate = crossRate;
    }

    public double getMutateRate() {
        return mutateRate;
    }

    public void setMutateRate(double mutateRate) {
        this.mutateRate = mutateRate;
    }

    public Map<Integer, List<Integer>> getAssignMap() {
        return assignMap;
    }

    public void setAssignMap(Map<Integer, List<Integer>> assignMap) {
        this.assignMap = assignMap;
    }

    @Override
    public String toString() {
        return "GaApi{" +
                "workerNum=" + workerNum +
                ", taskNum=" + taskNum +
                ", distanceMatrix=" + Arrays.toString(distanceMatrix) +
                ", taskDistanceMatrix=" + Arrays.toString(taskDistanceMatrix) +
                ", q=" + q +
                ", p=" + Arrays.toString(p) +
                ", threshold=" + threshold +
                ", populationNum=" + populationNum +
                ", maxGen=" + maxGen +
                ", crossRate=" + crossRate +
                ", mutateRate=" + mutateRate +
                ", INF=" + INF +
                '}';
    }
}

