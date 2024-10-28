package cn.crowdos.kernel;

import cn.crowdos.kernel.Incentive.CredibilityBasedIncentive;
import cn.crowdos.kernel.Incentive.CredibilityBasedIncentiveImpl;
import cn.crowdos.kernel.algorithms.*;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.sensor.SensorData;
import cn.crowdos.kernel.sensor.SensorManager;
import cn.crowdos.kernel.system.DuplicateResourceNameException;
import cn.crowdos.kernel.system.SystemResourceCollection;
import cn.crowdos.kernel.system.resource.*;
import cn.crowdos.kernel.resource.Task;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class Kernel implements CrowdKernel {

    private boolean initialed = false;
    private static CrowdKernel kernel;
    private InterruptManager interruptManager = new InterruptManager();
    private SensorManager sensorManager;


    private SystemResourceCollection systemResourceCollection;

    private Kernel(){}

    /**
     *  If the kernel is not initialized, throw an exception when calling any method except `initial` and `isInitialed`
     *
     * @return A proxy object that wraps the kernel object.
     */
    public static CrowdKernel getKernel(){
        if (kernel != null) return kernel;
        Kernel crowdKernel = new Kernel();
        kernel = (CrowdKernel) Proxy.newProxyInstance(
                crowdKernel.getClass().getClassLoader(),
                crowdKernel.getClass().getInterfaces(),
                new InvocationHandler() {
                    final Set<String> checkedMethod;
                    {
                        checkedMethod = new HashSet<>();
                        Class<CrowdKernel> crowdKernelClass = CrowdKernel.class;
                        for (Method method : crowdKernelClass.getMethods()) {
                            String name = method.getName();
                            if (!name.equals("initial") && !name.equals("isInitialed")) {
                                checkedMethod.add(name);
                            }
                        }
                    }
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (checkedMethod.contains(method.getName()) && !crowdKernel.isInitialed())
                            throw new UninitializedKernelException();
                        return method.invoke(crowdKernel, args);
                    }
                }
        );
        return kernel;
    }
    /**
     * This function returns the version of the CrowdOS CrowdKernel.
     *
     * @return The version of the CrowdOS kernel.
     */
    public static String version(){
        return "CrowdOS CrowdKernel v1.0";
    }

    /**
     *  The shutdown function sets the kernel to null
     */
    public static void shutdown() {
        kernel = null;
    }


    // CrowdKernel APIs

    @Override
    public boolean isInitialed(){
        return initialed;
    }
    @Override
    public void initial(Object...args){
        systemResourceCollection = new SystemResourceCollection();
        try {
            systemResourceCollection.register(new TaskPool());
            systemResourceCollection.register(new ParticipantPool());
            systemResourceCollection.register(new AlgoContainer(new AlgoFactoryAdapter(systemResourceCollection)),"DefaultAlgo");
            systemResourceCollection.register(new Scheduler(systemResourceCollection));
            systemResourceCollection.register(new MissionHistory());
            systemResourceCollection.register(new CredibilityBasedIncentiveImpl());
            systemResourceCollection.register(new AlgoContainer(new PTMostFactory(systemResourceCollection)),"PTMost");
            systemResourceCollection.register(new AlgoContainer(new T_MostFactory(systemResourceCollection)),"T_Most");
            systemResourceCollection.register(new AlgoContainer(new T_RandomFactory(systemResourceCollection)),"T_Random");
            systemResourceCollection.register(new AlgoContainer(new GGA_IFactory(systemResourceCollection)),"GGA_I");
        } catch (DuplicateResourceNameException e) {
            throw new RuntimeException(e);
        }
        initialed = true;
    }


    //The system provides PTMost, T_Most, T_Random and GGA_I algorithms by default. If necessary, you can choose them by yourself

    /**
     * The algoSelect function is used to select the algorithm that will be used by the scheduler.
     * The system provides PTMost, T_Most, T_Random and GGA_I algorithms by default.If you need, you can choose them by yourself.
     * If you don't need to select algorithm, the system will provide default algorithm "DefaultAlgo".
     * If you provide new algorithm, please add the algorithm selection to this function.
     *
     * @param name Select the needed algorithm and change algorithms in resource's AlgoFactory.
     *
     */
    @Override
    public void algoSelect(String name){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        switch (name) {
            case "PTMost":
                resource.setAlgoFactory("PTMost");
                break;
            case "T_Most":
                resource.setAlgoFactory("T_Most");
                break;
            case "T_Random":
                resource.setAlgoFactory("T_Random");
                break;
            case "GGA_I":
                resource.setAlgoFactory("GGA_I");
                break;
            default:
                throw new IllegalArgumentException("The system does not have the method.Please choose method from 'PTMost','T_Most','T_Random','GGA_I'");
        }
    }

    @Override
    public void initial(){
        initial((Object) null);
    }

    @Override
    public SystemResourceCollection getSystemResourceCollection() {
        return systemResourceCollection;
    }
    @Override
    public boolean submitTask(Task task){
        TaskPool resource = systemResourceCollection.getResourceHandler(TaskPool.class).getResource();
        resource.add(task);
        return true;
    }
    @Override
    public boolean registerParticipant(Participant participant) {
        ParticipantPool resource = systemResourceCollection.getResourceHandler(ParticipantPool.class).getResource();
        resource.add(participant);
        return true;
    }

    @Override
    public List<Task> getTasks(){
        TaskPool resource = systemResourceCollection.getResourceHandler(TaskPool.class).getResource();
        return new ArrayList<>(resource);
    }
    @Override
    public List<Participant> getTaskAssignmentScheme(Task task){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.taskAssignment(task);
    }
    @Override
    public Map<Participant, Double> getTaskIncentiveAssignmentScheme(Task task , Double rewards){

        MissionHistory resource = systemResourceCollection.getResourceHandler(MissionHistory.class).getResource();
        CredibilityBasedIncentive credibility =  systemResourceCollection.getResourceHandler(CredibilityBasedIncentive.class).getResource();

        List<Mission> missions = resource.getMissionsByTask(task);
        Mission firstMission = missions.get(0);
        Participant firstSubmitParticipant = firstMission.getFirstSubmitParticipant();
        List<Participant> participants = firstMission.getParticipants();

        credibility.allocateRewards(rewards,task,firstSubmitParticipant,participants);
        return credibility.IncentiveAssignment();
    }

    /**
     * The getTaskAssignmentScheme function is used to assign tasks to participants.
     * The function takes in a list of tasks and returns a list of participant lists, where each sublist
     * contains the participants assigned to that task.
     * @param tasks Pass the tasks to be assigned to the scheduler
     * @return A list of participant lists.
     */
    @Override
    public List<List<Participant>> getTaskAssignmentScheme(ArrayList<Task> tasks){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.taskAssignment(tasks);
    }

    @Override
    public List<Participant> getTaskRecommendationScheme(Task task){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.taskRecommendation(task);
    }
    /**
     * The etTaskRecommendationScheme function is used to assign tasks to participants.
     * The function takes in a list of tasks and returns a list of participant lists,where each sublist
     * contains the participants recommended to that task.
     *
     * @param tasks Pass the tasks to be recommended to the scheduler
     *
     * @return A list of participant lists.
     */
    @Override
    public List<List<Participant>> getTaskRecommendationScheme(ArrayList<Task> tasks){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.taskRecommendation(tasks);
    }
    @Override
    public List<Participant> getTaskParticipantSelectionResult(Task task){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.participantSelection(task);
    }
    /**
     * The getTaskParticipantSelectionResult function is used to assign tasks to participants.
     * The function takes in a list of tasks and returns a list of participant lists,where each sublist
     * contains the participants selected for that task.
     *
     * @param tasks Pass the tasks to be selected for to the scheduler
     *
     * @return A list of participant lists.
     */
    @Override
    public List<List<Participant>> getTaskParticipantSelectionResult(ArrayList<Task> tasks){
        Scheduler resource = systemResourceCollection.getResourceHandler(Scheduler.class).getResource();
        return resource.participantSelection(tasks);
    }
    @Override
    public List<Participant> getParticipants(){
        ParticipantPool resource = systemResourceCollection.getResourceHandler(ParticipantPool.class).getResource();
        return new ArrayList<>(resource);
    }

    @Override
    /**
     *Gets the interrupt manager instance
     *
     * @return Interrupt manager instance
     */
    public InterruptManager getInterruptManager(){
        return interruptManager;
    }

    /**
     * 启动指定参与者设备上的传感器
     * @param deviceId 参与者的设备ID
     * @param sensorType 需要启动的传感器类型
     */
    public void startSensor(String deviceId, String sensorType) {
        // 调用SensorManager中的方法，启动传感器
        sensorManager.startSensorTask(deviceId, sensorType);
    }

    /**
     * 停止指定参与者设备上的传感器
     * @param deviceId 参与者的设备ID
     * @param sensorType 需要停止的传感器类型
     */
    public void stopSensor(String deviceId, String sensorType) {
        // 调用SensorManager中的方法，停止传感器
        sensorManager.stopSensorTask(deviceId, sensorType);
    }

    /**
     * 获取传感器数据
     * @param deviceId 参与者的设备ID
     * @return 返回该设备上采集到的传感器数据
     */
    public List<SensorData> getSensorData(String deviceId) {
        // 通过SensorManager获取数据
        return sensorManager.getSensorData(deviceId);
    }



}
