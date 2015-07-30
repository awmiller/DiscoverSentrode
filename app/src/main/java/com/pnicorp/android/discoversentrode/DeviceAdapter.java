package com.pnicorp.android.discoversentrode;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.BaseKeyListener;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by amiller on 7/21/2015.
 */
public class DeviceAdapter extends android.support.v7.widget.RecyclerView.Adapter<DeviceViewHolder> {

    private Set<BluetoothDevice> mBlueDeviceSet;
    private List<BluetoothDevice> mBlueDevices;
    private OnCardClickedListener mListener;
    private Context appContext;

    public interface OnCardClickedListener{
        boolean onRecyclerCardClicked(View view, BluetoothDevice btd);
    }

    public DeviceAdapter(BluetoothDevice[] d,OnCardClickedListener listener, Context ctx)
    {
        mBlueDeviceSet = new LinkedHashSet<>();
        for(BluetoothDevice bdv : d)
        {
            mBlueDeviceSet.add(bdv);
        }
        mBlueDevices = new ArrayList<>(mBlueDeviceSet);
        mListener = listener;
        appContext = ctx;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_node_card,parent,false);

        DeviceViewHolder vh = new DeviceViewHolder(v,mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {

        holder.mNodeTitleText.setText(mBlueDevices.get(position).getName());
        holder.mNodeDetailsText.setText(mBlueDevices.get(position).getAddress());
        holder.setBluetoothDevice(mBlueDevices.get(position));
        holder.setNodeController(new NodeController(appContext,mBlueDevices.get(position)));

    }

    @Override
    public int getItemCount() {

        return mBlueDevices.size();

    }

    public boolean addDevice(BluetoothDevice BlueD)
    {
        try {
            if(mBlueDeviceSet.add(BlueD)) {
                mBlueDevices = new ArrayList<>(mBlueDeviceSet);
                this.notifyDataSetChanged();
            }
        }
        catch(Exception ignored)
        {
            Log.d("DeviceAdapter","Exception at add: ",ignored);
            return false;
        }
        return true;
    }



}
