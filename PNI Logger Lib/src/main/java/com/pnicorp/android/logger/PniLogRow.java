package com.pnicorp.android.logger;

import android.renderscript.Sampler;

import java.util.Arrays;

/**
 * Created by amiller on 6/30/2015.
 *
 * A Simple Structure for ordering and passing Log Row Data in the PNI format
 */
public class PniLogRow {
    public final boolean isHeader;

    public PniLogRow()
    {

        TS_Milliseconds=0L;
        SensorType=0;
        Accuracy=0.0f;
        Values=new Float[]{Float.NaN};
        isHeader = false;
    }
    public PniLogRow(PniLogRow p)
    {

        TS_Milliseconds=    p.TS_Milliseconds;
        SensorType=         p.SensorType;
        Accuracy=           p.Accuracy;
        Values=             p.Values.clone();
        isHeader=           p.isHeader;
    }
    public PniLogRow(Long ts,Integer st,Float ac,Float[] vl)
    {
        isHeader = false;
        TS_Milliseconds=ts;
        SensorType=st;
        Accuracy=ac;
        Values= vl.clone();
    }

    public Long TS_Milliseconds;
    public Integer SensorType;
    public Float Accuracy;
    public Float[] Values;

    public PniLogRow(boolean b) {
        isHeader = b;
        TS_Milliseconds=0L;
        SensorType=0;
        Accuracy=0.0f;
        Values=new Float[]{Float.NaN};
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(TS_Milliseconds);
        s.append(",");
        s.append(SensorType);
        s.append(",");
        s.append(Accuracy);
        s.append(",");

        for(Float v: Values)
        {
            s.append(v);
            s.append(",");
        }

        return s.toString();
    }

}
