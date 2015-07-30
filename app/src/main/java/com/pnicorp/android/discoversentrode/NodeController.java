package com.pnicorp.android.discoversentrode;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amiller on 7/24/2015.
 */
public class NodeController {

    private final Context mAppContext;
    private BluetoothGatt mBlueGatt;
    private BluetoothDevice mBlueDevice;
    private TextView mReportView;

    NodeController(Context ctx, BluetoothDevice device)
    {
        mBlueDevice = device;
        mAppContext = ctx;
        //startBlueConnection(ctx);
    }


    public void setmReportView(TextView mReportView) {
        this.mReportView = mReportView;
    }

    void setBluetoothDevice(BluetoothDevice device)
    {
        mBlueDevice = device;
    }

    public void startBlueConnection()
    {
        if(mBlueGatt != null)
        {
            mBlueGatt.disconnect();
            mBlueGatt.close();
            mBlueGatt=null;
        }
        mBlueGatt = mBlueDevice.connectGatt(mAppContext,false,mBleCallbacks);
    }

    public void disconnectGatt()
    {
        if(mBlueGatt != null)
        {
            mBlueGatt.disconnect();
        }
    }

    public void closeGatt()
    {
        if(mBlueGatt!=null)
        {
            mBlueGatt.disconnect();
            mBlueGatt.close();
            mBlueGatt = null;
        }

        BluetoothConnected = false;
        BluetoothStreaming = false;
    }

    public boolean isBluetoothConnected() {
        return BluetoothConnected;
    }

    public boolean isBluetoothStreaming() {
        return BluetoothStreaming;
    }

    private boolean BluetoothConnected;
    private boolean BluetoothStreaming;
    private boolean DisconnectOnServiceDiscovery = false;

    private BluetoothGattCallback mBleCallbacks = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if(newState == BluetoothGatt.STATE_CONNECTED)
            {
                BluetoothConnected = true;
                gatt.discoverServices();

            }
            else if(newState == BluetoothGatt.STATE_DISCONNECTED)
            {
                closeGatt();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            if(mReportView!=null)
            {
                ArrayList<BluetoothGattService> ListServs
                        = (ArrayList<BluetoothGattService>) gatt.getServices();
                StringBuilder sb = new StringBuilder(mBlueDevice.getAddress()+"\r\n");
                if(ListServs!=null)
                {
                    for(BluetoothGattService svs : ListServs)
                    {
                        sb.append(svs.getUuid().toString()).append("\r\n");
                    }
                }
                mReportView.setText(sb.toString());
            }

            if(DisconnectOnServiceDiscovery)
            {
                DisconnectOnServiceDiscovery = false;
                closeGatt();
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }
    };

    public boolean DiscoveryUpdating()
    {
        return DisconnectOnServiceDiscovery;
    }

    public void startDiscoveryUpdate() {
        DisconnectOnServiceDiscovery = true;
        startBlueConnection();
    }
}
