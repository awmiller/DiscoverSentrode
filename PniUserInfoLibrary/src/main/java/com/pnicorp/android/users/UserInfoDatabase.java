package com.pnicorp.android.users;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by amiller on 6/17/2015.
 */
public class UserInfoDatabase implements UserInfoSQLHelper.DatabaseLifecycleListener {

    private final Context mAppContext;
    private UserInfoSQLHelper sqlHelper;
    private UserInfo[] mUIcontainer;

    public UserInfoDatabase(Context ctx) {

        mAppContext = ctx;
        sqlHelper = new UserInfoSQLHelper(ctx);
        sqlHelper.attachDatabaseLifecycleListener(this);
    }

    public void synchronize() {
        SQLiteDatabase db = sqlHelper.getReadableDatabase();

        Cursor c = db.query(
                UserInfoSQLHelper.Entries.TABLE_NAME,
                UserInfoSQLHelper.ALL_COLUMNS,
                null, //WHERE
                null, //WHERE values
                null, //dont group rows
                null, //dont filter rows
                UserInfoSQLHelper.Entries.COLUMN_NAME_NAME + " ASC"
        );

        mUIcontainer = new UserInfo[c.getCount()];

        UserInfo UI;

        if(!c.moveToFirst()) {return;}

        do {
            UI = new UserInfo();
            try {
                UI.setName(
                        c.getString(c.getColumnIndexOrThrow(UserInfoSQLHelper.Entries.COLUMN_NAME_NAME)));
            } catch (IllegalArgumentException e) {
                UI.setName("Column Error");
                Log.d("USERINFOR DB ERROR","Column",e);
            }
            try {
                UI.setAge(
                        c.getString(c.getColumnIndexOrThrow(UserInfoSQLHelper.Entries.COLUMN_NAME_AGE)));
            } catch (IllegalArgumentException e) {
                UI.setAge("Column Error");
                Log.d("USERINFOR DB ERROR","Column",e);
            }
            try {
                UI.setWeight(
                        c.getString(c.getColumnIndexOrThrow(UserInfoSQLHelper.Entries.COLUMN_NAME_WEIGHT)));
            } catch (IllegalArgumentException e) {
                UI.setWeight("Column Error");
                Log.d("USERINFOR DB ERROR","Column",e);
            }
            try {
                UI.setHeightIn(
                        c.getString(c.getColumnIndexOrThrow(UserInfoSQLHelper.Entries.COLUMN_NAME_HEIGHT_IN)));
            } catch (IllegalArgumentException e) {
                UI.setHeightIn("Column Error");
                Log.d("USERINFOR DB ERROR","Column",e);
            }
            try {
                UI.setHeightFt(
                        c.getString(c.getColumnIndexOrThrow(UserInfoSQLHelper.Entries.COLUMN_NAME_HEIGHT_FT)));
            } catch (IllegalArgumentException e) {
                UI.setHeightFt("Column Error");
                Log.d("USERINFOR DB ERROR","Column",e);
            }
            try {
                UI.setGender(
                        c.getString(c.getColumnIndexOrThrow(UserInfoSQLHelper.Entries.COLUMN_NAME_GENDER)));
            } catch (IllegalArgumentException e) {
                UI.setGender("Column Error");
                Log.d("USERINFOR DB ERROR","Column",e);
            }
            try {
                UI.setActivity(
                        c.getString(c.getColumnIndexOrThrow(UserInfoSQLHelper.Entries.COLUMN_NAME_ACTIVITY)));
            } catch (IllegalArgumentException e) {
                UI.setActivity("Column Error");
                Log.d("USERINFOR DB ERROR","Column",e);
            }
            try {
                UI.setOrientation(
                        c.getString(c.getColumnIndexOrThrow(UserInfoSQLHelper.Entries.COLUMN_NAME_ORIENTATION)));
            } catch (IllegalArgumentException e) {
                UI.setOrientation("Column Error");
                Log.d("USERINFOR DB ERROR","Column",e);
            }
            try {
                UI.setLogFiles(
                        c.getString(c.getColumnIndexOrThrow(UserInfoSQLHelper.Entries.COLUMN_NAME_LOGS)));
            } catch (IllegalArgumentException e) {
                UI.setOrientation("Column Error");
                Log.d("USERINFOR DB ERROR","Column",e);
            }
            try {
                UI.setEmail(
                        c.getString(c.getColumnIndexOrThrow(UserInfoSQLHelper.Entries.COLUMN_NAME_EMAIL)));
            } catch (IllegalArgumentException e) {
                UI.setOrientation("Column Error");
                Log.d("USERINFOR DB ERROR","Column",e);
            }


            mUIcontainer[c.getPosition()] = UI;
        }while(c.moveToNext());

        c.close();
    }

    public boolean insert(UserInfo UI)
    {
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        db.delete(UserInfoSQLHelper.Entries.TABLE_NAME,
                UserInfoSQLHelper.Entries.COLUMN_NAME_NAME + "='" + UI.getName() + "'",null);

        ContentValues values = new ContentValues();
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_NAME, UI.getName());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_AGE, UI.getAge());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_GENDER, UI.getGender());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_HEIGHT_FT, UI.getHeightFt());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_HEIGHT_IN, UI.getHeightIn());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_WEIGHT, UI.getWeight());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_ORIENTATION, UI.getOrientation());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_ACTIVITY, UI.getActivity());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_LOGS, UI.getLogFiles());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_EMAIL, UI.getEmail());

        long newRow =
                db.insert(UserInfoSQLHelper.Entries.TABLE_NAME, null, values);

        return (newRow>=0);
    }

    public boolean insert(UserInfo UI, SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_NAME, UI.getName());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_AGE, UI.getAge());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_GENDER, UI.getGender());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_HEIGHT_FT, UI.getHeightFt());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_HEIGHT_IN, UI.getHeightIn());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_WEIGHT, UI.getWeight());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_ORIENTATION, UI.getOrientation());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_ACTIVITY, UI.getActivity());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_LOGS, UI.getLogFiles());
        values.put(UserInfoSQLHelper.Entries.COLUMN_NAME_EMAIL, UI.getEmail());

        long newRow =
                db.insert(UserInfoSQLHelper.Entries.TABLE_NAME, null, values);

        return (newRow>=0);
    }

    public CharSequence[] getOrderedRowNames()
    {
        synchronize();

        CharSequence[] list = new CharSequence[mUIcontainer.length];

        for(int i =0; i < mUIcontainer.length; i++)
        {
            list[i] = mUIcontainer[i].getName();
        }

        return list;
    }

    public UserInfo getInfoAtRow(int row){
        return mUIcontainer[row];
    }

    @Override
    public void onDatabaseCreated(SQLiteDatabase DB) {
        UserInfo Andrew = new UserInfo();

        Andrew.setOrientation("Handheld");
        Andrew.setWeight("200");
        Andrew.setActivity("Pedometer Calorie Counter");
        Andrew.setGender(UserInfo.GENDER_MALE_STRING_VALUE);
        Andrew.setHeightFt("5");
        Andrew.setHeightIn("10");
        Andrew.setName("Andrew Miller");
        Andrew.setAge("24");
        Andrew.setLogFiles("Downloads:/");
        Andrew.setLogFiles("amiller@pnicorp.com");

        insert(Andrew, DB);


        Andrew.setHeightFt("5");
        Andrew.setHeightIn("11");
        Andrew.setName("Andrew Taylor");
        Andrew.setWeight("153");
        Andrew.setAge("");
        Andrew.setLogFiles("ataylor@pnicorp.com");

        insert(Andrew, DB);
    }

    @Override
    public void onDatabaseOpened() {

    }
}
