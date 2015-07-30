package com.pnicorp.android.discoversentrode;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Node;

/**
 * Created by amiller on 7/23/2015.
 */
public class NodeGattCallbackCardManager extends BluetoothGattCallback{

    private final TextView nodeDetailsView;
    private final TextView nodeTitleView;
    public View parentView;
    private BluetoothDevice device;
    private BluetoothGatt mBlueGatt;

    public NodeGattCallbackCardManager(View v, BluetoothDevice d, Context t)
    {
        parentView = v;
        device = d;
        d.connectGatt(t,false,this);

        nodeDetailsView = (TextView) parentView.findViewById(R.id.node_details);
        nodeTitleView = (TextView) parentView.findViewById(R.id.node_title);

        String st = nodeDetailsView.getText().toString();
        nodeDetailsView.setText(st + "\r\nConnecting...");

    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        mBlueGatt = gatt;

        if(newState == BluetoothGatt.STATE_CONNECTED)
        {
            nodeDetailsView.setText(device.getAddress()+"\r\nConnected");
        }

    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
    }


    public void disconnect() {
        if(mBlueGatt == null)
        {
            return;
        }
        mBlueGatt.disconnect();
        mBlueGatt.close();
        mBlueGatt =null;

        nodeDetailsView.setText(device.getAddress());
    }
}
