package cn.crowdos.kernel.algorithms;

import java.util.*;

/**
 * @author wushengjie
 *
 */
public class PT_Most {

    private int workerNum;

    private int taskNum;

    private double[][] distanceMatrix;

    private double[][] taskDistanceMatrix;

    private double[][] distanceMatrixTemp;

    private int q;

    private int[] p;

    private Map<Integer, List<Integer>> assignMap = new HashMap<>();

    private Map<Integer, List<Integer>> assignMapTemp = new HashMap<>();

    private final double INF = Double.MAX_VALUE;



    private List<Integer> workerList = new ArrayList<>();

    private double distance;

    private double[][] distanceMatrixTempCal;


    /**
     * @param workerNum          Number of workers
     * @param taskNum            Number of tasks
     * @param distanceMatrix     Worker task distance matrix, which stores the distance
     *                           between a worker and a task (or the cost of a task for a worker)
     * @param taskDistanceMatrix Task distance matrix, which holds the distance between tasks and tasks
     * @param p                  Constraints, how many workers are needed for each task
     * @param q                  Constraints, how many tasks are assigned to each worker at most
     */
    public PT_Most(int workerNum, int taskNum, double[][] distanceMatrix, double[][] taskDistanceMatrix, int[] p, int q) {
        this.workerNum = workerNum;
        this.taskNum = taskNum;
        this.distanceMatrix = distanceMatrix;
        this.taskDistanceMatrix = taskDistanceMatrix;
        /*The diagonal elements of the task distance matrix
         * are set to INF to facilitate the calculation
         */
        for (int i = 0; i < taskDistanceMatrix.length; i++) {
            taskDistanceMatrix[i][i] = INF;
        }
        this.q = q;
        this.p = p;

        //Initializing the Map
        for (int i = 0; i < workerNum; i++) {
            this.assignMap.put(i, new ArrayList<>());
        }

        //Replication distance matrix
        distanceMatrixTempCal = new double[workerNum][taskNum];
        for (int i = 0; i < workerNum; i++) {
            for (int j = 0; j < taskNum; j++) {
                distanceMatrixTempCal[i][j] = distanceMatrix[i][j];
            }
        }

    }

    /**
     * Assign tasks to workers using a greedy algorithm.
     * This method assigns tasks to workers based on the closest worker to each task,
     * starting with the task that needs the largest number of workers. The algorithm
     * continues until all tasks have been assigned.
     *
     * @throws IllegalArgumentException if the number of tasks is less than or equal to zero,
     * or the number of workers is less than or equal to zero.
     */
    public void taskAssign() {


        int taskIndex;
        int workerIndex;


        //Temporarily save the worker task distance matrix

        distanceMatrixTemp = new double[workerNum][taskNum];
        for (int i = 0; i < workerNum; i++) {
            for (int j = 0; j < taskNum; j++) {
                distanceMatrixTemp[i][j] = distanceMatrix[i][j];
            }
        }

        //Check that all tasks have been assigned

        while (!isTaskAssignFinish()) {


            while (!isTaskAssignFinish()) {


                //A worker who has not yet been assigned a task is randomly selected,
                // and the task closest to the worker is selected

                List<Integer> randomWorkerList = new ArrayList<>();

                //Iterate over assignMapTemp to find workers whose tasks have not yet been assigned

                for (int i = 0; i < workerNum; i++) {
                    if (assignMap.get(i).size() < q) {
                        randomWorkerList.add(i);
                    }
                }


                //All workers have been assigned tasks and break out of the loop

                if (randomWorkerList.size() == 0) {
                    break;
                }


                Collections.shuffle(randomWorkerList);
                workerIndex = randomWorkerList.get(0);

                //taskIndex may be -1

                taskIndex = findMinTaskToWorker(workerIndex);

                //This worker has no more tasks to assign, so break out of the loop

                if (taskIndex == -1) {

                    continue;
                }

                if (isAssignTask(taskIndex)) {
                    continue;
                }

                //Join the allocation result Map
                assignMap.get(workerIndex).add(taskIndex);
                //Reset matrix
                distanceMatrix[workerIndex][taskIndex] = INF;


                //A counter
                int count = 0;

                while (!isAssignWorker(workerIndex)) {

                    if (isTaskAssignFinish()) {
                        break;
                    }

                    //If there are five tasks, select four tasks in turn
                    if (count == taskNum - 1) {
                        break;
                    }


                    int[] taskIndexArray = findMinTaskToTask(taskIndex);


                    int taskIndex_ = taskIndexArray[count];
                    count++;


                    //Check if the task still needs workers
                    if (isAssignTask(taskIndex_)) {

                        continue;
                    } else {
                        //Join the allocation result Map
                        assignMap.get(workerIndex).add(taskIndex_);
                        //Reset matrix
                        distanceMatrix[workerIndex][taskIndex_] = INF;

                        isAssignTask(taskIndex_);

                    }
                }



            }
            //A BUG means there's something wrong with the function
            SelectMaxTU();


        }

        countDistance();


    }


    //to do checks whether the passed arguments are valid,
    //such as the number of workers p[i] required for a task
    //does not match the maximum number q of tasks each worker can accept

    /**
     * The worker with the most assigned tasks
     * is selected and only he and his assigned tasks are kept
     */
    public void SelectMaxTU() {

        //Select the longest Value in the assignMap
        int max = 0;
        int maxKey = 0;
        for (Map.Entry<Integer, List<Integer>> entry : assignMap.entrySet()) {
            if (entry.getValue().size() > max) {

                if (workerList.contains(entry.getKey())) {
                    continue;
                }

                max = entry.getValue().size();
                maxKey = entry.getKey();
            }
        }

        workerList.add(maxKey);


        //Deep copy list
        List<Integer> list = new ArrayList<>(assignMap.get(maxKey));

        //Clear the other values in assignMap
        //Iterate over the workerList
        for (Map.Entry<Integer, List<Integer>> entry : assignMap.entrySet()) {
            if (!workerList.contains(entry.getKey())) {
                entry.getValue().clear();
            }
        }


        //Iterate over the list and set the corresponding element
        // in distanceMatrixTemp to INF
        for (Integer integer : list) {
            distanceMatrixTemp[maxKey][integer] = INF;
            isAssignTask(integer);
        }

        isAssignWorker(maxKey);

        //Reset matrix
        for (int i = 0; i < workerNum; i++) {
            for (int j = 0; j < taskNum; j++) {
                distanceMatrix[i][j] = distanceMatrixTemp[i][j];
            }
        }

    }


    /**
     * Find the task closest to the worker
     *
     * @param workerIndex the number of worker
     *
     * @return int
     */
    private int findMinTaskToWorker(int workerIndex) {
        double min = INF;
        int minIndex = -1;
        for (int i = 0; i < taskNum; i++) {
            if (distanceMatrix[workerIndex][i] < min) {
                min = distanceMatrix[workerIndex][i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * The distance between all tasks and this task is sorted in
     * ascending order,and stored by task sequence number
     *
     * @param taskIndex the number of task
     *
     * @return {@code int[]}
     */
    private int[] findMinTaskToTask(int taskIndex) {

        //Save the task sequence number
        int[] taskIndexArray = new int[taskNum];

        Map<Integer, Double> taskDistanceMap = new HashMap<>();

        for (int i = 0; i < taskNum; i++) {
            taskDistanceMap.put(i, taskDistanceMatrix[taskIndex][i]);
        }

        //Sort the map by value
        Map<Integer, Double> sortMap = sortMap(taskDistanceMap);


        //Iterate over taskDistanceMap and
        // store the corresponding task sequence number in taskIndexArray
        int count = 0;
        for (Map.Entry<Integer, Double> entry : sortMap.entrySet()) {
            taskIndexArray[count] = entry.getKey();
            count++;
        }

        return taskIndexArray;
    }


    /**
     * 1、Converts the entrySet of the Map to a List
     * 2、Sort with the sort method of the Collections utility class
     * 3、Iterate over the sorted list and put each set of keys and
     * values into a LinkedHashMap(the only Map implementation class
     * that stores the values in order of insertion is LinkedHashMap).
     *
     * @param map {@code map<Integer, Double>}
     *
     * @return {@code Map<Integer, Double>}
     */
    public Map<Integer, Double> sortMap(Map<Integer, Double> map) {

        //Using the entrySet method of the Map, it is transformed into a list for sorting
        List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(map.entrySet());

        //Sort the list using the Collections sort method
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                //Positive order, reverse order
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        //As you iterate through the sorted list,
        // it's important to include the LinkedHashMap,
        // because only the LinkedHashMap is stored in the order in
        // which it was inserted
        LinkedHashMap<Integer, Double> linkedHashMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> e : entryList
        ) {
            linkedHashMap.put(e.getKey(), e.getValue());
        }

        return linkedHashMap;
    }

    /**
     * Determines if the worker is still acceptable,
     * and sets all elements of the row to maximum
     *
     * @param workerIndex workerIndex
     *
     * @return boolean
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
     * Determine if the task still needs workers,
     * and set the column to the maximum
     *
     * @param taskIndex taskIndex
     *
     * @return boolean
     */
    public boolean isAssignTask(int taskIndex) {

        if (countTaskIndex(taskIndex) >= p[taskIndex]) {
            for (int i = 0; i < workerNum; i++) {
                distanceMatrix[i][taskIndex] = INF;
            }
            return true;
        }
        return false;
    }

    /**
     * Calculate how many workers have been assigned to this task
     *
     * @param taskIndex taskIndex
     *
     * @return int
     */
    public int countTaskIndex(int taskIndex) {
        int count = 0;

        //Compute the task allocation map
        for (Map.Entry<Integer, List<Integer>> entry : assignMap.entrySet()) {
            if (entry.getValue().contains(taskIndex)) {
                count++;
            }
        }
        return count;
    }

    public int countTaskIndexInTemp(int taskIndex) {
        int count = 0;

        //The computation tasks are assigned map copies
        for (Map.Entry<Integer, List<Integer>> entry : assignMapTemp.entrySet()) {
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
    public boolean isTaskAssignFinish() {
        for (int i = 0; i < taskNum; i++) {
            if (countTaskIndex(i) < p[i]) {
                return false;
            }
        }
        //If all values in assignMap have length q, then all tasks have been assigned
        int countWorkerTask = 0;
        for (Map.Entry<Integer, List<Integer>> entry : assignMap.entrySet()) {
            if (entry.getValue().size() == q) {
                countWorkerTask++;
            }
        }
        //If all workers are assigned q tasks,
        // it means that all tasks are assigned and
        // workers cannot accept any more tasks
        if (countWorkerTask == workerNum) {
            return true;
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
     * Calculate the distance, the total distance that the worker needs to
     * move to complete the task, which is the total cost
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
            distance += distanceMatrixTempCal[i][taskList.get(0)];
            //Calculate the distance of worker i from the first task to the last task
            for (int j = 0; j < taskList.size() - 1; j++) {
                distance += taskDistanceMatrix[taskList.get(j)][taskList.get(j + 1)];
            }
            //Calculate the distance of worker i from the last task to worker i
            distance += distanceMatrixTempCal[i][taskList.get(taskList.size() - 1)];
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

    public double[][] getDistanceMatrixTemp() {
        return distanceMatrixTemp;
    }

    public void setDistanceMatrixTemp(double[][] distanceMatrixTemp) {
        this.distanceMatrixTemp = distanceMatrixTemp;
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

    public Map<Integer, List<Integer>> getAssignMapTemp() {
        return assignMapTemp;
    }

    public void setAssignMapTemp(Map<Integer, List<Integer>> assignMapTemp) {
        this.assignMapTemp = assignMapTemp;
    }

    public double getINF() {
        return INF;
    }

    public List<Integer> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(List<Integer> workerList) {
        this.workerList = workerList;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double[][] getDistanceMatrixTempCal() {
        return distanceMatrixTempCal;
    }

    public void setDistanceMatrixTempCal(double[][] distanceMatrixTempCal) {
        this.distanceMatrixTempCal = distanceMatrixTempCal;
    }
}


