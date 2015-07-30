package com.pnicorp.android.logger.writer;

import android.hardware.SensorEvent;
import android.hardware.TriggerEvent;
import android.location.Location;

import java.io.File;
import java.io.IOException;

public class SensorEventLogger extends DataWriter {


    /**
     * Custom sensor type for Location object data
     */
    public static final int SENSOR_TYPE_LOCATION = 101;

    /**
     * Custom sensor type for elevation data
     */
    public static final int SENSOR_TYPE_ELEVATION = 102;


    public static final int SENSOR_TYPE_ALGORITHM = 20;
    public static final int SENSOR_TYPE_GPS_PLUS_PATH = 103;

    private static StringBuilder sStringBuilder = new StringBuilder();

    private String mDelimiter;

    public SensorEventLogger(File dir, String delimiter) {
        super(dir);
        mDelimiter = delimiter;
    }

    public void setHeader(String header) throws IOException{
        write(header);
    }

    public void log(SensorEvent event) throws IOException {
        log(event.timestamp, event.sensor.getType(), event.accuracy, event.values);
    }

    public void log(TriggerEvent event) throws IOException {
        log(event.timestamp, event.sensor.getType(), 0, event.values);
    }
    
    public void log(long ts, int sensorType, int accuracy, float[] values) throws IOException {
        sStringBuilder.setLength(0);

        // System timestamp
        sStringBuilder.append(System.currentTimeMillis());

        // timestamp
        sStringBuilder.append(mDelimiter);
        sStringBuilder.append(ts);

        // sensor type
        sStringBuilder.append(mDelimiter);
        sStringBuilder.append(sensorType);

        // accuracy
        sStringBuilder.append(mDelimiter);
        sStringBuilder.append(accuracy);

        // sensor values
        for (int i = 0; i < values.length; i++) {
            sStringBuilder.append(mDelimiter);
            sStringBuilder.append(values[i]);
        }
        writeln(sStringBuilder.toString());
    }
    public void log(Location location) throws IOException {
        sStringBuilder.setLength(0);

        // System timestamp
        sStringBuilder.append(System.currentTimeMillis());

        // timestamp
        sStringBuilder.append(mDelimiter);
        sStringBuilder.append(location.getTime());

        // sensor type
        sStringBuilder.append(mDelimiter);
        sStringBuilder.append(10);

        // accuracy
        sStringBuilder.append(mDelimiter);
        sStringBuilder.append(location.getAccuracy());

        // sensor values
        sStringBuilder.append(mDelimiter);
        sStringBuilder.append(location.getLatitude());
        sStringBuilder.append(mDelimiter);
        sStringBuilder.append(location.getLongitude());


        writeln(sStringBuilder.toString());
    }

    
    public File getFile() {
        return mFile;
    }
}
