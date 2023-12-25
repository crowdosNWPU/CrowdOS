package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.algorithms.AlgoFactory;
import cn.crowdos.kernel.system.SystemResourceHandler;

public class AlgoContainer extends ResourceContainer<AlgoFactory> {

    public AlgoContainer(AlgoFactory resource) {
        super(resource);
    }


    @Override
    public SystemResourceHandler<AlgoFactory> getHandler() {
        return new SystemResourceHandler<AlgoFactory>() {
            @Override
            public AlgoFactory getResourceView() {
                return resource;
            }

            @Override
            public AlgoFactory getResource() {
                return resource;
            }
        };
    }
}
