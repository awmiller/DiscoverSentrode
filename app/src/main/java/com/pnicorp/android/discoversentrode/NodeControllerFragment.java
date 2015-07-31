package com.pnicorp.android.discoversentrode;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class NodeControllerFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String BLUETOOTH_DEVICE_ARRAY_KEY = "BluetoothDeviceArray 123456";
    public ArrayMap<BluetoothDevice,NodeController> mNodeDevices = new ArrayMap<>();
    private ProgressBar mProgressBar;
    Timer TimeOut = new Timer();
    Timer progressAnimator = new Timer();
    private int BackgroundDefaultColor;

    private int BackgroundSelectedColor = Color.argb(200,100,100,255);

    private void setBlueDevices(BluetoothDevice[] devices)
    {
        for(BluetoothDevice b : devices)
        {
            if(!mNodeDevices.containsKey(b))
            {
                mNodeDevices.put(b,new NodeController(getActivity(),b));
            }
        }
    }

    public boolean addBluetoothDevice(BluetoothDevice device)
    {
        if(!mNodeDevices.containsKey(device))
        {
            mNodeDevices.put(device,new NodeController(getActivity(),device));

            mAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, getListItems());

            mListView.setAdapter(mAdapter);

            return true;
        }
        return false;
    }

    public List<NodeController> getListItems()
    {
        ArrayList<NodeController> ret = new ArrayList<>();
        for(NodeController nc : mNodeDevices.values())
        {
            ret.add(nc);
        }
        return ret;
    }

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    public static NodeControllerFragment newInstance(ArrayList<BluetoothDevice> BlueDevices) {
        NodeControllerFragment fragment = new NodeControllerFragment();
        Bundle state = new Bundle();
        BluetoothDevice[] array = BlueDevices.toArray(new BluetoothDevice[BlueDevices.size()]);
        state.putParcelableArray(BLUETOOTH_DEVICE_ARRAY_KEY,array);
        fragment.setArguments(state);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NodeControllerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            setBlueDevices((BluetoothDevice[]) getArguments().getParcelableArray(BLUETOOTH_DEVICE_ARRAY_KEY));
        }

        mAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, getListItems());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nodecontroller, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        if(mProgressBar==null)
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar2);

        mProgressBar.setVisibility(View.VISIBLE);

        progressAnimator.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mProgressBar.getProgress()==mProgressBar.getMax())
                            if(mListener!=null)
                                mListener.requestDiscoveryStop();
                        mProgressBar.incrementProgressBy(1);
                    }
                });
            }
        },100,500);

        BackgroundDefaultColor = view.getDrawingCacheBackgroundColor();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private ArrayDeque<NodeController> mNodes = new ArrayDeque<>(2);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {

            NodeController nc = (NodeController) mAdapter.getItem(position);
            nc.setmReportView((TextView) view.findViewById(android.R.id.text1));


            if(nc.isBluetoothConnected())
            {
                nc.disconnectGatt();
                mProgressBar.setVisibility(View.INVISIBLE);

            }
            else {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onItemClicked(nc);

                if(mNodes.contains(nc))
                {
                    NodeController popout = mNodes.pop();
                    popout.getReportView().setBackgroundColor(BackgroundDefaultColor);
                }
                else {
                    if (mNodes.size() > 1) {
                        NodeController popout = mNodes.pop();
                        popout.getReportView().setBackgroundColor(BackgroundDefaultColor);
                        mNodes.addLast(nc);

                    } else
                        mNodes.addLast(nc);

                    nc.getReportView().setBackgroundColor(BackgroundSelectedColor);
                }

                /**
                 * Start device connection
                 *//**
                nc.startDiscoveryUpdate();
                mProgressBar.setVisibility(View.VISIBLE);
                scheduleConnectionTimeout(nc);*/
            }
        }
    }

    private void scheduleConnectionTimeout(final NodeController nc) {
        TimeOut.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread( new Runnable() {
                    public void run() {
                        if(nc.isBluetoothConnected())
                        {
                            nc.disconnectGatt();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        },10000);
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onItemClicked(NodeController clickedDevice);
        void requestDiscoveryStop();
    }

}
