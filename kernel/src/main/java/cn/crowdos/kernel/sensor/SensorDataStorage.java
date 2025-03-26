/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University, Inc. <https://github.com/crowdosNWPU/CrowdOS>
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
package cn.crowdos.kernel.sensor;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SensorDataStorage {
    private final Map<String, List<SensorData>> sensorDataMap = new HashMap<>();

    // 存储传感器数据
    public void storeSensorData(SensorData sensorData) {
        sensorDataMap.computeIfAbsent(sensorData.getDeviceId(), k -> new ArrayList<>()).add(sensorData);
    }

    // 获取指定设备的传感器数据
    public List<SensorData> getSensorData(String deviceId) {
        return sensorDataMap.getOrDefault(deviceId, Collections.emptyList());
    }
}
