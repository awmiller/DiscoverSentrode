package com.pnicorp.android.discoversentrode;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

/**
 * Created by amiller on 7/24/2015.
 */
public class DeviceViewHolder extends ViewHolder implements View.OnClickListener {

    public TextView mNodeDetailsText;
    public TextView mNodeTitleText;
    public CardView clickField;
    private BluetoothDevice mBlueDevice;
    private DeviceAdapter.OnCardClickedListener mReporter;
    private NodeController mNodeC;

    @Override
    public void onClick(View v) {

        if (!mNodeC.isBluetoothConnected()) {
            mReporter.onRecyclerCardClicked(clickField, mBlueDevice);
            mNodeC.startBlueConnection();
        }
        else
        {
            mNodeC.closeGatt();
        }
    }

    public DeviceViewHolder(View itemView, DeviceAdapter.OnCardClickedListener repo) {
        super(itemView);

        clickField = (CardView) itemView.findViewById(R.id.card_view);
        mNodeDetailsText = (TextView) itemView.findViewById(R.id.node_details);
        mNodeTitleText = (TextView) itemView.findViewById(R.id.node_title);

        clickField.setOnClickListener(this);

        mReporter = repo;
    }

    public void setBluetoothDevice(BluetoothDevice BDV) {

        this.mBlueDevice = BDV;

    }

    public void setNodeController( NodeController nc)
    {
        mNodeC  = nc;
        mNodeC.setmReportView(mNodeDetailsText);
        mNodeC.startDiscoveryUpdate();
    }

    public boolean settingUpUUIDs()
    {
        return mNodeC.DiscoveryUpdating();
    }


}
