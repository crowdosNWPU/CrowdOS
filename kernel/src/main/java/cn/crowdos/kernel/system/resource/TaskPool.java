package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.InterruptManager;
import cn.crowdos.kernel.system.SystemResourceHandler;
import cn.crowdos.kernel.resource.Task;

import java.util.LinkedList;

public class TaskPool extends LinkedList<Task> implements Resource<TaskPool> {
    @Override
    public SystemResourceHandler<TaskPool> getHandler() {
        TaskPool tasks = this;
        //中断管理器实例
        InterruptManager interruptManager;

        return new SystemResourceHandler<TaskPool>() {

            @Override
            public TaskPool getResourceView() {
                return tasks;
            }

            @Override
            public TaskPool getResource() {
                return tasks;
            }
        };
    }
}
