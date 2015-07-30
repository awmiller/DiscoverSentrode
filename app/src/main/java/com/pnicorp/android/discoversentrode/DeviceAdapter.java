package com.pnicorp.android.discoversentrode;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by amiller on 7/21/2015.
 */
public class DeviceAdapter extends android.support.v7.widget.RecyclerView.Adapter<DeviceViewHolder> {

    private BluetoothDevice[] mBlueDevices;
    private OnCardClickedListener mListener;
    private ArrayMap<RecyclerView.ViewHolder,BluetoothDevice> ViewDeviceMap;
    private ArrayMap<Integer,DeviceViewHolder> ViewPositionMap;

    public interface OnCardClickedListener{
        boolean onRecyclerCardClicked(View view, BluetoothDevice btd);
    }

    public DeviceAdapter(BluetoothDevice[] d,OnCardClickedListener listener)
    {
        mBlueDevices = d;
        mListener = listener;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_node_card,parent,false);

        DeviceViewHolder vh = new DeviceViewHolder(v,mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {

        holder.mNodeTitleText.setText(mBlueDevices[position].getName());
        holder.mNodeDetailsText.setText(mBlueDevices[position].getAddress());
        holder.setBluetoothDevice(mBlueDevices[position]);

    }

    @Override
    public int getItemCount() {

        return mBlueDevices.length;

    }
}
