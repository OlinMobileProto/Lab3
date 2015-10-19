package hieunguyen.com.scavengerhunt.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hieunguyen.com.scavengerhunt.Interfaces.DestinationCallback;

/**
 * Created by hieunguyen on 10/17/15.
 */
public class DbService {

    public static final String TAG = "DB";
    public Context context;
    public LocationDatabase locationDB;
    public SQLiteDatabase db;

    public DbService(Context context) {
        this.context = context;
        locationDB = new LocationDatabase(context);
    }

    public void populate(JSONArray data) {
        db = locationDB.getWritableDatabase();
        try {
            for (int i=0; i<data.length(); i++) {
                ContentValues cv = new ContentValues();
                Log.d(TAG, "populate " + i);
                JSONObject place = (JSONObject) data.get(i);
                cv.put(LocationDatabase.colID, place.get("id").toString());
                cv.put(LocationDatabase.colLat, place.get("latitude").toString());
                cv.put(LocationDatabase.colLong, place.get("longitude").toString());
                cv.put(LocationDatabase.colS3, place.get("s3id").toString());
                Log.d(TAG, "populate " + cv);
                db.insertOrThrow(LocationDatabase.tableName, LocationDatabase.colS3, cv);
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON EXCEPTION");
            e.printStackTrace();
        } catch (SQLException e) {
            Log.e(TAG, "SQL EXCEPTION");
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void clear() {
        Log.d(TAG, "CLEARING");
        db = locationDB.getWritableDatabase();
        db.delete(LocationDatabase.tableName, null, null);
        db.close();
    }

    public void update() {
        clear();
        HttpHandler handler = new HttpHandler(context);
        handler.getDestinations(new DestinationCallback() {
            @Override
            public void destCallback(boolean success, JSONArray locations) {
                if (success && locations != null) {
                    populate(locations);
                } else if (locations == null) {
                    Log.d(TAG, "LOCATIONS NULL");
                } else {
                    Log.e(TAG, "HTTP FAILED");
                }
            }
        });
    }

    public boolean isDbEmpty() {
        db = locationDB.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + LocationDatabase.tableName, null);
        Log.d(TAG, "EMPTY: " + String.valueOf(mCursor.getCount() == 0));
        return mCursor.getCount() == 0;
    }

    public ClueDAO getClue(int clueNumber) {
        db = locationDB.getReadableDatabase();
        
        Cursor c = db.rawQuery("SELECT * FROM " + LocationDatabase.tableName + " WHERE " +
                LocationDatabase.colID + "=?", new String[] {String.valueOf(clueNumber)});

        Log.d(TAG, "getClue");
        
        ClueDAO clue = null;
        
        c.moveToFirst();
        while (!c.isAfterLast()) {
            int result_id = c.getInt(0);
            double result_lat = c.getDouble(1);
            double result_long = c.getDouble(2);
            String result_s3id = c.getString(3);
            clue = new ClueDAO(result_id, result_lat, result_long, result_s3id);
            c.moveToNext();
        }
        c.close();

        db.close();
        return clue;
    }
}
