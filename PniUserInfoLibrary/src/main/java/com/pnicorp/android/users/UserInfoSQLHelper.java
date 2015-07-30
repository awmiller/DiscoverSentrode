package com.pnicorp.android.users;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by amiller on 6/17/2015.
 */
public class UserInfoSQLHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserProfiles.db";


    public DatabaseLifecycleListener mLifecycleListener;

    /**
     * Usefull Projections
     */
    public static final String[] ALL_COLUMNS = {
            Entries.COLUMN_NAME_NAME,
            Entries.COLUMN_NAME_ORIENTATION,
            Entries.COLUMN_NAME_AGE,
            Entries.COLUMN_NAME_HEIGHT_FT,
            Entries.COLUMN_NAME_HEIGHT_IN,
            Entries.COLUMN_NAME_WEIGHT,
            Entries.COLUMN_NAME_GENDER,
            Entries.COLUMN_NAME_ACTIVITY,
            Entries.COLUMN_NAME_LOGS,
            Entries.COLUMN_NAME_EMAIL
    };

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = " , ";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Entries.TABLE_NAME
            + " (" + Entries._ID + " INTEGER PRIMARY KEY , "
            + Entries.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP
            + Entries.COLUMN_NAME_AGE + TEXT_TYPE + COMMA_SEP
            + Entries.COLUMN_NAME_GENDER + TEXT_TYPE + COMMA_SEP
            + Entries.COLUMN_NAME_HEIGHT_FT + TEXT_TYPE + COMMA_SEP
            + Entries.COLUMN_NAME_HEIGHT_IN + TEXT_TYPE + COMMA_SEP
            + Entries.COLUMN_NAME_WEIGHT + TEXT_TYPE + COMMA_SEP
            + Entries.COLUMN_NAME_ACTIVITY + TEXT_TYPE + COMMA_SEP
            + Entries.COLUMN_NAME_ORIENTATION + TEXT_TYPE + COMMA_SEP
            + Entries.COLUMN_NAME_LOGS + TEXT_TYPE + COMMA_SEP
            + Entries.COLUMN_NAME_EMAIL + TEXT_TYPE
            + " )";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Entries.TABLE_NAME;
    private final Context mApplicatonContext;

    public static abstract class Entries implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ORIENTATION = "orientation";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_HEIGHT_FT = "height_ft";
        public static final String COLUMN_NAME_HEIGHT_IN = "height_in";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_ACTIVITY = "activity";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_LOGS = "logs";
        public static final String COLUMN_NAME_EMAIL = "email";
    }

    public UserInfoSQLHelper(Context context)
    {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
        mApplicatonContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        if(mLifecycleListener!=null)
        {
            mLifecycleListener.onDatabaseCreated(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void attachDatabaseLifecycleListener(DatabaseLifecycleListener listener)
    {
        mLifecycleListener = listener;
    }

    public interface DatabaseLifecycleListener
    {
        public void onDatabaseCreated(SQLiteDatabase DB);

        public void onDatabaseOpened();
    }
}
