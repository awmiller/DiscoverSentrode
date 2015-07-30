package com.pnicorp.android.logger.reader;

import com.pnicorp.android.logger.PniLogRow;

import java.io.File;
import java.io.IOException;

public class SensorEventReader extends DataReader {

    public long timestamp = 0L;

    public int sensorType = 0;

    public Float[] values = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };

    private String mDelimiter;

    private String mLine;

    private String[] mColumns;
    private float accuracy;

    public SensorEventReader(File dir, String delimiter) {
        super(dir);
        mDelimiter = delimiter;
    }

    public PniLogRow read() throws IOException {
        mLine = readln();
        
        if (mLine == null) {
            return null;
        }
        
        mColumns = mLine.split(mDelimiter);
        
        // timestamp
        this.timestamp = 0L;
        if (mColumns.length > 0) {
            try {
                this.timestamp = Long.parseLong(mColumns[0]);
            } catch (NumberFormatException e) {
                return new PniLogRow(true);
            }
        }
        
        // sensor type
        this.sensorType = 0;
        if (mColumns.length > 2) {
            try {
                this.sensorType = Integer.parseInt(mColumns[2]);
            } catch (NumberFormatException e) {
                return new PniLogRow(timestamp,0,0.0f,null);
            }
        }

        // accuracy
        this.accuracy = 0;
        if (mColumns.length > 3) {
            try {
                this.accuracy = Float.parseFloat(mColumns[3]);
            } catch (NumberFormatException e) {

                return new PniLogRow(timestamp,0,0.0f,null);
            }
        }

        // values
        if (mColumns.length > 4) {
            values = new Float[mColumns.length - 4];
            for (int i = 4; i < mColumns.length; i++) {

                try {
                    this.values[i - 4] = Float.parseFloat(mColumns[i]);
                } catch (NumberFormatException e) {
                    this.values[i-4] = Float.NaN;
                }
            }
        }
        return new PniLogRow(this.timestamp,this.sensorType,this.accuracy,this.values);
    }

}
