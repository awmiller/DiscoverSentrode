package com.pnicorp.android.discoversentrode;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity extends AppCompatActivity{

    /**
     * Bluetooth LE Locals
     */
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 10;
    private BleReceiver mBleBcReceiver = new BleReceiver();
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private ArrayList<BluetoothDevice> mBleDevices = new ArrayList<>();
    final Timer BleTimeOut = new Timer();

    /**
     *
     * Widget management features
     */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mrAdapter;
    private RecyclerView.LayoutManager mrLayoutManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter itf = new IntentFilter();
        itf.addAction(BluetoothDevice.ACTION_FOUND);
        getApplication().registerReceiver(mBleBcReceiver, itf);

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else
        {
            BleTimeOut.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    mBluetoothAdapter.cancelDiscovery();
                    BleListDevices();
                }
            },10000, 5000);
            scanForDevices();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        BleTimeOut.cancel();
        mBluetoothAdapter.cancelDiscovery();
        //set null state as a flag to non UI threads
        mBluetoothAdapter = null;

        //roll back to initial state.
        FragmentManager fm = getFragmentManager();
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
            mBluetoothAdapter.startDiscovery();
        }
    }

    private void BleListDevices()
    {
        if(mBleDevices.size()>0)
        {
//            ArrayList<String> list = new ArrayList<>(mBleDevices.size());
//
//            for(BluetoothDevice d : mBleDevices)
//            {
//                list.add(d.getName() + "\r\n" + d.getAddress() + "\r\n\r\n");
//            }
//
//            final String[] s = list.toArray(new String[list.size()]);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //null state used as a flag that this activity is not active
                    if(mBluetoothAdapter!=null) {
                        BleTimeOut.cancel();
                        startDeviceDisplay(mBleDevices.toArray(new BluetoothDevice[mBleDevices.size()]));
                    }
                }
            });
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(MainActivity.this, "No Devices founds, restarting scan...", Toast.LENGTH_SHORT).show();

                    scanForDevices();
                }
            });
        }
    }

        private void startDeviceDisplay(String[] s) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.RelLaymain, NodeProfileFragment.newInstance(s));
        ft.addToBackStack("That");
        ft.commit();

        findViewById(R.id.text).setVisibility(View.GONE);
        findViewById(R.id.circle).setVisibility(View.GONE);
    }
    private void startDeviceDisplay(BluetoothDevice[] devices) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.RelLaymain, NodeProfileFragment.newInstance(devices));
        ft.addToBackStack("That");
        ft.commit();

        findViewById(R.id.text).setVisibility(View.GONE);
        findViewById(R.id.circle).setVisibility(View.GONE);
    }


    private class BleReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_FOUND))
            {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mBleDevices.add(mDevice);
            }
        }
    }


}
