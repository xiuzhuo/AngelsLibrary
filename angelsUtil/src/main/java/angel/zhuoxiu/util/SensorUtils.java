package angel.zhuoxiu.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

/**
 * Created by zxui on 18/05/15.
 */
public class SensorUtils {

    public enum AndroidSensor {
        LIGHT(Sensor.TYPE_LIGHT, SensorManager.SENSOR_DELAY_UI), ACCELEROMETER(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);

        int type;
        int delay;

        AndroidSensor(int type, int delay) {
            this.type = type;
            this.delay = delay;
        }
    }

    public static SensorManager getSensorManager(Context context) {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public static java.util.List<Sensor> getAllSensors(Context context) {
        return getSensorManager(context).getSensorList(Sensor.TYPE_ALL);
    }

    public static Sensor getSensor(Context context, AndroidSensor androidSensor) {
        List<Sensor> list = getSensorManager(context).getSensorList(androidSensor.type);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static void registerSensorListener(Context context, AndroidSensor androidSensor, SensorEventListener sensorEventListener) {
        Sensor sensor = getSensor(context, androidSensor);
        if (sensor != null) {
            getSensorManager(context).registerListener(sensorEventListener, sensor, androidSensor.delay);
        }
    }

    public static void unRegisterSensorListener(Context context, AndroidSensor androidSensor, SensorEventListener sensorEventListener) {
        Sensor sensor = getSensor(context, androidSensor);
        if (sensor != null) {
            getSensorManager(context).unregisterListener(sensorEventListener, sensor);
        }
    }

}
