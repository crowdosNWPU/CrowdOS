package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.system.SystemResourceHandler;

public abstract class ResourceContainer<T> implements Resource<T>{

    protected T resource;
    public ResourceContainer(T resource){
        this.resource = resource;
    }
    public abstract SystemResourceHandler<T> getHandler();
}
