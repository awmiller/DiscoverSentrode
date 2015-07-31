package com.pnicorp.android.discoversentrode;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NodeProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NodeProfileFragment extends Fragment implements DeviceAdapter.OnCardClickedListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mrLayoutManger;
    private DeviceAdapter mrAdapter;
    private String[] mDeviceNames;
    private BluetoothDevice[] mBlueDevices;
    private ArrayList<BluetoothDevice> mBlueDeviceList;

    public static final Integer[] BLUENRG_DEFAULT_DEVADDR = {0x02, 0x80, 0xE1, 0x00, 0x34, 0x12};
    public static final Integer[] BLUENRG_RIGHT_NODE_DEVADDR = {0x02, 0x80, 0xE1, 0x00, 0x34, 0x13};
    public static final Integer[] BLUENRG_LEFT_NODE_DEVADDR = {0x02, 0x80, 0xE1, 0x00, 0x34, 0x14};
    private NodeController mNeutralNode;
    private NodeController mRightNode;
    private NodeController mLeftNode;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static NodeProfileFragment newInstance(String[] devicenames) {
        NodeProfileFragment fragment = new NodeProfileFragment();
        fragment.setDeviceNames(devicenames);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static NodeProfileFragment newInstance(BluetoothDevice[] devices) {
        NodeProfileFragment fragment = new NodeProfileFragment();
        fragment.setDevices(devices);
        return fragment;
    }

    public NodeProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlueDeviceList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.discover_devices_view, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecycleViewMain);
        mrLayoutManger = new LinearLayoutManager(getActivity());

        if (mrAdapter == null)
            mrAdapter = new DeviceAdapter(mBlueDevices, this,getActivity());

        if ((mRecyclerView != null) && (mrLayoutManger != null)) {
            mRecyclerView.setHasFixedSize(true);


            mRecyclerView.setLayoutManager(mrLayoutManger);


            mRecyclerView.setAdapter(mrAdapter);
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setDeviceNames(String[] deviceNames) {
        this.mDeviceNames = deviceNames;
    }


    public void setDevices(BluetoothDevice[] devices) {
        this.mBlueDevices = devices;
    }

    public static boolean checkDeviceAddressAgainst(Integer[] check, Integer[] against) {
        if (check.length == against.length) {
            for (Integer i = 0; i < check.length; i++) {
                if (!(check[i].intValue() == against[i].intValue())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isValidNode(ArrayList<Integer> address) {
        Integer[] bytes = address.toArray(new Integer[address.size()]);
        return checkDeviceAddressAgainst(bytes, BLUENRG_RIGHT_NODE_DEVADDR)
                || checkDeviceAddressAgainst(bytes, BLUENRG_DEFAULT_DEVADDR)
                || checkDeviceAddressAgainst(bytes, BLUENRG_LEFT_NODE_DEVADDR);
    }

    private String getAddressString(ArrayList<Integer> bytes) {
        StringBuilder s = new StringBuilder();
        for (Integer I : bytes) {
            s.append(String.format("%X:", I));
        }
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }

    @Override
    public boolean onRecyclerCardClicked(View view, BluetoothDevice bdv) {

        //mBlueDeviceList.add(bdv);

        return false;
    }

    public void addDeviceToAdapter(BluetoothDevice BlueD)
    {
        mrAdapter.addDevice(BlueD);
    }


}
