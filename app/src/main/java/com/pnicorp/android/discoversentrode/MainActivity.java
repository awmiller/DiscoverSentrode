package com.pnicorp.android.discoversentrode;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity extends AppCompatActivity implements NodeControllerFragment.OnFragmentInteractionListener{

    protected static final String FRAGMENT_DISCOVERING_DEVICES = "wjbgf;awfj;alw.p3218fues90iqfm";
    protected static final String FRAGMENT_EXPLORING_DEVICE = "aiufqjbk.af *A Wfajnk;a  sjbvge98p sue";

    /**
     * Bluetooth LE Locals
     */
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 10;
    private BleReceiver mBleBcReceiver = new BleReceiver();
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private LinkedHashSet<BluetoothDevice> mBleDevices = new LinkedHashSet<>();
    final Timer BleTimeOut = new Timer();
    private NodeProfileFragment mNodeProfileFragment;
    NodeConfigureFragment mConfigFrag;
    /**
     *
     * Widget management features
     */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mrAdapter;
    private RecyclerView.LayoutManager mrLayoutManger;
    private boolean mDisplayRunning=false;
    private NodeControllerFragment mNodeContFrag;

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {

        //roll back to initial state.
        FragmentManager fm = getFragmentManager();
        if(fm.getBackStackEntryCount() < 1)
            super.onBackPressed();
        else
            fm.popBackStack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter itf = new IntentFilter();
        itf.addAction(BluetoothDevice.ACTION_FOUND);
        itf.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getApplication().registerReceiver(mBleBcReceiver, itf);

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else
        {
            scanForDevices();
        }
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        mBluetoothAdapter.startDiscovery();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        safeDiscoveryStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        //set null state as a flag to non UI threads
        mBluetoothAdapter = null;
        if(BleTimeOut!=null)
            BleTimeOut.cancel();

        //roll back to initial state.
        FragmentManager fm = getFragmentManager();
        if(fm.findFragmentByTag(FRAGMENT_EXPLORING_DEVICE)!=null)
            fm.popBackStackImmediate();
        if(fm.findFragmentByTag(FRAGMENT_DISCOVERING_DEVICES)!=null)
            fm.popBackStackImmediate();
        fm.popBackStackImmediate();

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if(requestCode == REQUEST_ENABLE_BT)
        {
            scanForDevices();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void scanForDevices()
    {
        if(mBluetoothAdapter!=null && mBluetoothAdapter.isEnabled())
        {
//            for(BluetoothDevice d : mBluetoothAdapter.getBondedDevices())
//            {
//                mBleDevices.add(d);
//            }
            BleTimeOut.schedule(new TimerTask() {
                @Override
                public void run() {
                    BleListDevices();
                }
            }, 5000);
        }
    }

    private void BleListDevices()
    {
        if(mBleDevices.size()>0)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //null state used as a flag that this activity is not active
                    if((mBluetoothAdapter!=null)&&(!mDisplayRunning)) {
                        //BleTimeOut.cancel();
                        startDeviceDisplay();
                        mDisplayRunning = true;
                    }

                }
            });
        }
    }


    private void startDeviceDisplay() {

//        mNodeProfileFragment = NodeProfileFragment.newInstance(devices);
//
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.RelLaymain, mNodeProfileFragment);
//        ft.addToBackStack("That");
//        ft.commit();
        mNodeContFrag = new NodeControllerFragment();
        mNodeContFrag = NodeControllerFragment.newInstance(new ArrayList<>(mBleDevices));

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.RelLaymain, mNodeContFrag);
        ft.addToBackStack(FRAGMENT_DISCOVERING_DEVICES);
        ft.commit();

        findViewById(R.id.text).setVisibility(View.GONE);
        findViewById(R.id.circle).setVisibility(View.GONE);

        if(BleTimeOut!=null)
            BleTimeOut.cancel();

    }

    @Override
    public void onItemClicked(NodeController clickedDevice) {
        //start a node action
        Toast.makeText(MainActivity.this, String.format("Device Clicked %s",clickedDevice.toString())
                , Toast.LENGTH_SHORT).show();

        startConfigFragment(clickedDevice);
    }

    @Override
    public void requestDiscoveryStop() {
        safeDiscoveryStop();
    }

    private void safeDiscoveryStop()
    {
        if(mBluetoothAdapter!=null)
        {
            if(mBluetoothAdapter.isDiscovering())
            {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
    }



    private class BleReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


                mBleDevices.add(mDevice);

                if(mNodeContFrag!=null)
                {
                    mNodeContFrag.addBluetoothDevice(mDevice);
                }

            }

            if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

                safeDiscoveryStop();
            }
        }
    }

    private void startConfigFragment(NodeController nodeC)
    {
        mConfigFrag = NodeConfigureFragment.newInstance(nodeC.getBluetoothDevice());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.RelLaymain, mConfigFrag);
        ft.addToBackStack(FRAGMENT_EXPLORING_DEVICE);
        ft.commit();

        mConfigFrag.setNodeController(nodeC);
    }


}
