/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University. <https://github.com/crowdosNWPU/CrowdOS>
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

public class SensorData {
    private String deviceId;
    private String sensorType;
    private float value;
    private long timestamp;

    // 构造函数
    public SensorData(String deviceId, String sensorType, float value, long timestamp) {
        this.deviceId = deviceId;
        this.sensorType = sensorType;
        this.value = value;
        this.timestamp = timestamp;
    }

    // Getters 和 Setters
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getSensorType() { return sensorType; }
    public void setSensorType(String sensorType) { this.sensorType = sensorType; }

    public float getValue() { return value; }
    public void setValue(float value) { this.value = value; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "SensorData{" +
                "deviceId='" + deviceId + '\'' +
                ", sensorType='" + sensorType + '\'' +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }
}
