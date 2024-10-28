package cn.crowdos.kernel.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class SensorController {

    @Autowired
    private SensorManager sensorManager;

    // 设备上传传感器数据的接口
    @PostMapping("/upload")
    public ResponseEntity<String> uploadSensorData(@RequestBody SensorData sensorData) {
        sensorManager.handleSensorData(sensorData);
        return ResponseEntity.ok("Data received");
    }
}
