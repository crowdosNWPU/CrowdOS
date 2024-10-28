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
