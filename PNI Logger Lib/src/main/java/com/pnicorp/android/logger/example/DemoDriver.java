package com.pnicorp.android.logger.example;

import android.content.Intent;
import android.hardware.SensorEvent;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pnicorp.android.logger.R;
import com.pnicorp.android.logger.Utils;
import com.pnicorp.android.logger.writer.SensorEventLogger;
import com.pnicorp.android.users.UserInfo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DemoDriver extends AppCompatActivity {

    //<editor-fold desc="Logging Functions">
    // data logger
    private boolean mLoggingEnabled =false;
    private boolean mAlgoithmLastPointLogged =false;

    private String latestFilename;
    /**
     * Prefix for data log files
     */
    protected static final String DATA_LOG_PREFIX = "tt_";

    /**
     * Extension for data log files
     */
    protected static final String DATA_LOG_EXT = ".csv";

    /**
     * Delimiter used to separate values within the data log
     */
    protected static final String DATA_LOG_DELIMITER = ",";
    /**
     * Data logger instance
     */
    protected final SensorEventLogger mDataLogger = new SensorEventLogger(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            DATA_LOG_DELIMITER);

    protected UserInfo mUserProfile;


    /**
     * Enables the data logger, opens a log file
     */
    protected void enableLogging() {

        if (mDataLogger != null) {
            if(mLoggingEnabled) {
                disableLogging();
            }
            try {
                SimpleDateFormat dateF = new SimpleDateFormat("MMdd-HHmm-ss");
                Date date = new Date();
                final String filename = DATA_LOG_PREFIX + String.valueOf(dateF.format(date)) + DATA_LOG_EXT;
                latestFilename = filename;
                mDataLogger.open(filename, false);
                String activityString;

                activityString = "Pedometer Calorie and Distance Counter";
                String header = "Header1234";

                String userGender = null;
                String userHeight = null;
                String userWeight = null;
                String userAge = null;
                String userActivity = null;
                String userPosition = null;
                String userTruthStepCount = null;

                if(mUserProfile != null)
                {

                    try {
                        int height =
                                Integer.parseInt(mUserProfile.getHeightFt())*12
                                        + Integer.parseInt(mUserProfile.getHeightIn());

                        userGender = mUserProfile.getGender();
                        userHeight = String.format("%d",height);
                        userWeight = mUserProfile.getWeight();
                        userAge = mUserProfile.getAge();
                        userActivity = mUserProfile.getActivity();
                        userPosition = mUserProfile.getOrientation();
                        userTruthStepCount = "Check Last Entry";
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                header += "; Gender = "+ userGender +"\n";
                header += "; Height = "+ userHeight +"\n";
                header += "; Weight = "+ userWeight +"\n";
                header += "; Age = "+ userAge +"\n";
                header += "; Activity = "+userActivity+"\n";
                header += "; Position = "+userPosition+"\n";
                header += "; TruthStepCount = "+ userTruthStepCount +"\n";
                header += "; currentActivity = " + activityString + "\n";
                header += "; System.currentTimeMillis, event.timestamp, type, accuracy, values \n";

                mDataLogger.setHeader(header);

                // set enabled flag
                mLoggingEnabled = true;

                //  Toast.makeText(this,"Logging Enabled",Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toast.makeText(this, "Error enabling logging " + e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    /**
     * Disables the data logger, closes the file and notifies the system that a
     * new file is available
     */
    protected void disableLogging() {
        File f1 =null;

        if (mLoggingEnabled && (mDataLogger != null)) {
            try {
                f1 = mDataLogger.getFile();
                // notify media scanner
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(mDataLogger.getFile())));

                // close logger
                mDataLogger.close();

                Toast.makeText(this,"Logging Disabled",Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toast.makeText(this, "Error disabling logging "+ e.getMessage(),Toast.LENGTH_LONG)
                        .show();
            }
        }

        final File f2 = f1;

        String dir =
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                ).getAbsolutePath();
        MediaScannerConnection.scanFile(this, new String[]{dir + "/" + latestFilename}, null, null);

        // set disabled flag
        mLoggingEnabled = false;

        Utils.sendAsEmail(DemoDriver.this, f2, mUserProfile.getEmail());
//
//                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
//                adb.setTitle("Send as email?");
//
//                final EditText address = new EditText(adb.getContext());
//                address.setInputType(InputType.TYPE_CLASS_TEXT|
//                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//                address.setHint("someone@somewhere.somehow");
//
//                adb.setView(address);
//
//                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                adb.setPositiveButton("Send", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if((!address.getText().toString().isEmpty()) && (f2 != null))
//                        {
//                            final Intent ei = new Intent(Intent.ACTION_SEND);
//                            ei.setType("vnd.android.cursor.dir/email");
//                            ei.setType("plain/text");
//                            ei.putExtra(Intent.EXTRA_EMAIL,new String[]{address.getText().toString()});
//                            ei.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+f2.getAbsolutePath()));
//                            startActivity(Intent.createChooser(ei,"Send Email"));
//                        }
//
//                    }
//                });
//                adb.show();

    }

    public void tryLogging(SensorEvent event) {
        if (mLoggingEnabled) {
            try {
                mDataLogger.log(event);
            } catch (IOException e) {
                Toast.makeText(
                        this, "Error logging "+ e.getMessage(),Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }
    public void tryLogging(Location event) {
        if (mLoggingEnabled) {
            try {
                mDataLogger.log(event);
            } catch (IOException e) {
                Toast.makeText(
                        this, "Error logging "+ e.getMessage(),Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private void tryLogging(long timestamp, float stepCounts, float calories, float distance) {
        if(mLoggingEnabled)         //log if available
        {
            float[] vals = {
                    stepCounts,
                    calories,
                    distance
            };

            try {
                mDataLogger.log(timestamp,SensorEventLogger.SENSOR_TYPE_ALGORITHM,0,vals);
            } catch (IOException e) {
                Toast.makeText(this,
                        "Error Logging: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void tryLogging(String s, int truth) {
        if(mLoggingEnabled)         //log if available
        {
            try {
                mDataLogger.log(0L,-1,truth,new float[0]);
            } catch (IOException e) {
                Toast.makeText(this,
                        "Error Logging: " + s +" \r\n"+ e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_driver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demo_driver, menu);
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
}
