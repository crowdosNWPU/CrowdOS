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
