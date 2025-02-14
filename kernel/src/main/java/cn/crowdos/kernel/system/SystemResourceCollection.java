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
package cn.crowdos.kernel.system;

import cn.crowdos.kernel.system.resource.Resource;

import java.util.HashMap;
import java.util.Map;

public class SystemResourceCollection {
    Map<Class<?>, Map<String, Resource<?>>> resourceMap;

    public SystemResourceCollection(){
        resourceMap = new HashMap<>();
    }
    public void register(Resource<?> resource) throws DuplicateResourceNameException {
        register(resource, "default");
    }
    public void register(Resource<?> resource, String resourceName) throws DuplicateResourceNameException {
        if (!resourceMap.containsKey(resource.getClass())) {
            resourceMap.put(resource.getClass(), new HashMap<>());
        }
        Map<String, Resource<?>> resList = resourceMap.get(resource.getClass());
        if (resList.size() != 0 && resList.containsKey(resourceName)) {
            throw new DuplicateResourceNameException();
        }
        resList.put(resourceName, resource);
    }

    public <T> SystemResourceHandler<T> getResourceHandler(Class<? extends Resource<T>> resourceClass, String resourceName){
        Map<String, Resource<?>> stringResourceMap = resourceMap.get(resourceClass);
        if (stringResourceMap != null) {
            Resource<?> resource = stringResourceMap.get(resourceName);
            if (resource != null){
                return resourceClass.cast(resource).getHandler();
            }
        }
        return null;
    }


    public <T> SystemResourceHandler<T> getResourceHandler(Class<? extends Resource<T>> resourceClass){
        return getResourceHandler(resourceClass, "default");
    }
}
