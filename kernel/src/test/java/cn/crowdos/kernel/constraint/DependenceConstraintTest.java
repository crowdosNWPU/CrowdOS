package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.resource.SimpleTask;
import cn.crowdos.kernel.resource.Task;

import java.util.ArrayList;
import java.util.List;

class DependenceConstraintTest {

    private static DependenceConstraint finishedDependence;
    private static DependenceConstraint unfinishedDependence;
    private static DependenceConstraint mixDependence;

    // 初始化方法，原本的 @BeforeAll 方法
    static void beforeAll() {
        List<Task> finishedTask = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SimpleTask task = new SimpleTask(null, Task.TaskDistributionType.ASSIGNMENT);
            task.setTaskStatus(Task.TaskStatus.FINISHED);
            finishedTask.add(task);
        }
        List<Task> unfinishedTask = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SimpleTask task = new SimpleTask(null, Task.TaskDistributionType.ASSIGNMENT);
            task.setTaskStatus(Task.TaskStatus.READY);
            unfinishedTask.add(task);
        }
        finishedDependence = new DependenceConstraint(finishedTask);
        unfinishedDependence = new DependenceConstraint(unfinishedTask);
        List<Task> mixTasks = new ArrayList<>(finishedTask.subList(0, finishedTask.size() / 2));
        mixTasks.addAll(unfinishedTask.subList(unfinishedTask.size() / 2, unfinishedTask.size()));
        mixDependence = new DependenceConstraint(mixTasks);
    }

    // 测试 decomposer 方法
    static void testDecomposer() {
        try {
            Decomposer<Constraint> decomposer = finishedDependence.decomposer();
            List<Constraint> constraints = decomposer.trivialDecompose();
            System.out.println(constraints);
            List<Constraint> constraints1 = decomposer.scaleDecompose(10);
            System.out.println(constraints1);
            System.out.println("testDecomposer passed");
        } catch (DecomposeException e) {
            System.out.println("testDecomposer failed: " + e.getMessage());
        }
    }

    // 测试 satisfy 方法
    static void testSatisfy() {
        boolean result1 = finishedDependence.satisfy();
        boolean result2 = !unfinishedDependence.satisfy();
        boolean result3 = !mixDependence.satisfy();
        if (result1 && result2 && result3) {
            System.out.println("testSatisfy passed");
        } else {
            System.out.println("testSatisfy failed");
        }
    }

    // 测试 testSatisfy 方法（带条件）
    static void testTestSatisfy() {
        NoneCondition noneCondition = new NoneCondition();
        boolean result1 = finishedDependence.satisfy(noneCondition);
        boolean result2 = !unfinishedDependence.satisfy(noneCondition);
        boolean result3 = !mixDependence.satisfy(noneCondition);
        if (result1 && result2 && result3) {
            System.out.println("testTestSatisfy passed");
        } else {
            System.out.println("testTestSatisfy failed");
        }
    }

    // 测试 getConditionClass 方法
    static void testGetConditionClass() {
        boolean result1 = finishedDependence.getConditionClass() == NoneCondition.class;
        boolean result2 = unfinishedDependence.getConditionClass() == NoneCondition.class;
        boolean result3 = mixDependence.getConditionClass() == NoneCondition.class;
        if (result1 && result2 && result3) {
            System.out.println("testGetConditionClass passed");
        } else {
            System.out.println("testGetConditionClass failed");
        }
    }

    public static void main(String[] args) {
        beforeAll();
        testDecomposer();
        testSatisfy();
        testTestSatisfy();
        testGetConditionClass();
    }
}