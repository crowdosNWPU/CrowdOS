package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.resource.SimpleTask;
import cn.crowdos.kernel.resource.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DependenceConstraintTest {

    private static DependenceConstraint finishedDependence;
    private static DependenceConstraint unfinishedDependence;
    private static DependenceConstraint mixDependence;

    @BeforeAll
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
        mixTasks.addAll(unfinishedTask.subList(unfinishedTask.size()/2, unfinishedTask.size()));
        mixDependence = new DependenceConstraint(mixTasks);
    }

    @Test
    void decomposer() throws DecomposeException {
        Decomposer<Constraint> decomposer = finishedDependence.decomposer();
        List<Constraint> constraints = decomposer.trivialDecompose();
        System.out.println(constraints);
        List<Constraint> constraints1 = decomposer.scaleDecompose(10);
        System.out.println(constraints1);
    }

    @Test
    void satisfy() {
        assertTrue(finishedDependence.satisfy());
        assertFalse(unfinishedDependence.satisfy());
        assertFalse(mixDependence.satisfy());
    }

    @Test
    void testSatisfy() {
        NoneCondition noneCondition = new NoneCondition();
        assertTrue(finishedDependence.satisfy(noneCondition));
        assertFalse(unfinishedDependence.satisfy(noneCondition));
        assertFalse(mixDependence.satisfy(noneCondition));
    }

    @Test
    void getConditionClass() {
        assertSame(finishedDependence.getConditionClass(), NoneCondition.class);
        assertSame(unfinishedDependence.getConditionClass(), NoneCondition.class);
        assertSame(mixDependence.getConditionClass(), NoneCondition.class);
    }
}