package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.system.SystemResourceHandler;

public interface Resource<T> {

    SystemResourceHandler<T> getHandler();
}
