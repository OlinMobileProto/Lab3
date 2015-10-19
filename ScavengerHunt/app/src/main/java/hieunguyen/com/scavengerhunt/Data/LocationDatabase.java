package hieunguyen.com.scavengerhunt.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hieunguyen on 10/17/15.
 */
public class LocationDatabase extends SQLiteOpenHelper {

    public static final int dbVersion = 1;
    public static final String dbName = "ClueLocations.db";
    public static final String tableName = "Locations";

    public static final String colID = "ID";
    public static final String colLat = "Latitude";
    public static final String colLong = "Longitude";
    public static final String colS3 = "S3ID";
    public static final String colActive = "Active";

    public LocationDatabase(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + tableName + "("
                + colID + " INTEGER PRIMARY KEY, "
                + colLat + " DECIMAL(8,6), "
                + colLong + " DECIMAL(8,6), "
                + colS3 + " TEXT, "
                + colActive + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }
}
