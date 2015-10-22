package hieunguyen.com.scavengerhunt.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    // Fill in database with JSON response from HTTP request
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
                cv.put(LocationDatabase.colActive, 0);
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

    // Clear database
    public void clear() {
        Log.d(TAG, "CLEARING");
        db = locationDB.getWritableDatabase();
        db.delete(LocationDatabase.tableName, null, null);
        db.close();
    }

    // Update: Clears then populates
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

    // Retrieve a clue object by id; if id is -1 get active clue
    public ClueDAO getClue(int clueNumber) {
        db = locationDB.getReadableDatabase();

        String query = "SELECT * FROM " + LocationDatabase.tableName + " WHERE ";

        String[] args;

        if (clueNumber == 0) {
            query += LocationDatabase.colActive + "=?";
            args = new String[] {String.valueOf(1)};
        } else {
            query += LocationDatabase.colID + "=?";
            args = new String[] {String.valueOf(clueNumber)};
        }
        
        Cursor c = db.rawQuery(query, args);

        Log.d(TAG, "getClue");
        
        ClueDAO clue = null;
        
        c.moveToFirst();
        while (!c.isAfterLast()) {
            int result_id = c.getInt(0);
            double result_lat = c.getDouble(1);
            double result_long = c.getDouble(2);
            String result_s3id = c.getString(3);
            boolean result_isActive = c.getInt(4) > 0;
            clue = new ClueDAO(result_id, result_lat, result_long, result_s3id, result_isActive);
            c.moveToNext();
        }
        c.close();

        db.close();
        return clue;
    }

    // When a clue is completed, this function updates the status of the old and new clues
    public void changeActiveClue(int oldClue, int newClue) {
        // Setting a new active clue. if there is no oldClue, set oldClue to -1
        db = locationDB.getWritableDatabase();

        Log.d(TAG, "oldClue: " + String.valueOf(oldClue) + ", newClue: " + String.valueOf(newClue));

        if (oldClue != -1) {
            ContentValues oldCv = new ContentValues();
            oldCv.put(LocationDatabase.colActive, 0);
            db.update(LocationDatabase.tableName, oldCv,
                    LocationDatabase.colID + "=?", new String[]{String.valueOf(oldClue)});
        }

        ContentValues newCv = new ContentValues();
        newCv.put(LocationDatabase.colActive, 1);
        db.update(LocationDatabase.tableName, newCv,
                LocationDatabase.colID + "=?", new String[]{String.valueOf(newClue)});

        db.close();
    }
}
