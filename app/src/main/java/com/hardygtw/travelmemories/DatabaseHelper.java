package com.hardygtw.travelmemories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hardygtw.travelmemories.model.Photo;
import com.hardygtw.travelmemories.model.PlaceVisited;
import com.hardygtw.travelmemories.model.Trip;

import java.util.ArrayList;

/**
 * Created by gavin on 07/03/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "TravelMemories";

    private static final String TRIP_TABLE_NAME = "trips";
    public static final String COL_TRIP_ID = "tripId";
    public static final String COL_TRIP_NAME= "tripName";
    public static final String COL_TRIP_START_DATE = "startDate";
    public static final String COL_TRIP_END_DATE = "endDate";
    public static final String COL_TRIP_NOTES = "notes";

    private static final String PHOTOS_TABLE_NAME = "photos";
    public static final String COL_PHOTO_ID = "photoId";
    public static final String COL_PHOTO_SRC = "photoSrc";
    public static final String COL_TRIP_ID_FOREIGN_PHOTOS = "tripId";
    public static final String COL_PLACE_VISIT_ID_FOREIGN_PHOTOS = "placeVisitId";
    public static final String COL_TAGS = "tags";


    private static final String PLACE_VISIT_TABLE_NAME = "place_visit";
    public static final String COL_PLACE_NAME = "placeName";
    public static final String COL_PLACE_ADDRESS = "address";
    public static final String COL_PLACE_LONGITUDE = "longitude";
    public static final String COL_PLACE_LATITUDE = "latitude";
    public static final String COL_PLACE_VISIT_ID = "place_visit_id";
    public static final String COL_PLACE_VISIT_DATE = "visit_date";
    public static final String COL_PLACE_VISIT_NOTES = "notes";
    public static final String COL_PLACE_VISIT_TRAVEL_COMPANIONS = "travel_companions";
    public static final String COL_TRIP_ID_FOREIGN_PLACE_VISIT = "trip_id";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TRIP_TABLE_NAME + " ("
                + COL_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TRIP_NAME + " TEXT NOT NULL,"
                + COL_TRIP_START_DATE + " TEXT NOT NULL,"
                + COL_TRIP_END_DATE + " TEXT NOT NULL,"
                + COL_TRIP_NOTES + " TEXT" +
                ")");



        db.execSQL("CREATE TABLE " + PHOTOS_TABLE_NAME + " ("
                + COL_PHOTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PHOTO_SRC + " TEXT NOT NULL,"
                + COL_TRIP_ID_FOREIGN_PHOTOS + " INTEGER,"
                + COL_TAGS + " TEXT NOT NULL,"
                + COL_PLACE_VISIT_ID_FOREIGN_PHOTOS + " INTEGER" +
                ")");



        db.execSQL("CREATE TABLE " + PLACE_VISIT_TABLE_NAME + " ("
                + COL_PLACE_VISIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PLACE_VISIT_DATE + " TEXT NOT NULL,"
                + COL_PLACE_VISIT_NOTES + " TEXT NOT NULL,"
                + COL_PLACE_VISIT_TRAVEL_COMPANIONS + " TEXT NOT NULL,"
                + COL_PLACE_NAME + " TEXT NOT NULL,"
                + COL_PLACE_ADDRESS + "TEXT NOT NULL,"
                + COL_PLACE_LATITUDE + "REAL,"
                + COL_PLACE_LONGITUDE + "REAL,"
                + COL_TRIP_ID_FOREIGN_PLACE_VISIT + " INTEGER" +
                ")");


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TRIP_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLACE_VISIT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PHOTOS_TABLE_NAME);
        onCreate(db);
    }

    /**
     * This method updates a trip
     */
    public long updateTrip(int tripId, String trip_name, String startDate, String endDate, String notes)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TRIP_NAME, trip_name);
        cv.put(COL_TRIP_START_DATE, startDate);
        cv.put(COL_TRIP_END_DATE, endDate);
        cv.put(COL_TRIP_NOTES, notes);

        return db.update(TRIP_TABLE_NAME, cv, COL_TRIP_ID + " = ?", new String[] { String.valueOf(tripId) });
    }

    /**
     * This method creates a trip
     */
    public long createTrip(String trip_name, String startDate, String endDate, String notes)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TRIP_NAME, trip_name);
        cv.put(COL_TRIP_START_DATE, startDate);
        cv.put(COL_TRIP_END_DATE, endDate);
        cv.put(COL_TRIP_NOTES, notes);

        return db.insert(TRIP_TABLE_NAME, null, cv);
    }

    public Trip getTripDetails(int tripId) {

        SQLiteDatabase db = this.getReadableDatabase();
        Trip trip = new Trip();

        Cursor cursor = db.rawQuery("SELECT * from " + TRIP_TABLE_NAME + " WHERE tripId=" + tripId, new String [] {});

        if (cursor.moveToFirst())
        {
            trip.setTripId(cursor.getInt(cursor.getColumnIndex(COL_TRIP_ID)));
            trip.setTripName(cursor.getString(cursor.getColumnIndex(COL_TRIP_NAME)));
            trip.setStartDate(cursor.getString(cursor.getColumnIndex(COL_TRIP_START_DATE)));
            trip.setEndDate(cursor.getString(cursor.getColumnIndex(COL_TRIP_END_DATE)));
            trip.setPlacesVisited(new ArrayList<PlaceVisited>());
            trip.setTripPhotos(new ArrayList<Photo>());

            String notes = cursor.getString(cursor.getColumnIndex(COL_TRIP_NOTES));

            if (notes == null) {
                trip.setNotes("");
            } else {
                trip.setNotes(notes);
            }
        } else {
            return null;
        }

        cursor.close();

        return trip;
    }

    /**
     * //This method returns all trips from the database
     */
    public ArrayList<Trip> getAllTrips()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Trip> trips = new ArrayList<Trip>();

        Cursor cursor = db.rawQuery("SELECT * from " + TRIP_TABLE_NAME, new String [] {});

        if (cursor.moveToFirst())
        {
            do {
                Trip trip = new Trip();

                trip.setTripId(cursor.getInt(cursor.getColumnIndex(COL_TRIP_ID)));
                trip.setTripName(cursor.getString(cursor.getColumnIndex(COL_TRIP_NAME)));
                trip.setStartDate(cursor.getString(cursor.getColumnIndex(COL_TRIP_START_DATE)));
                trip.setEndDate(cursor.getString(cursor.getColumnIndex(COL_TRIP_END_DATE)));
                trip.setPlacesVisited(new ArrayList<PlaceVisited>());
                trip.setTripPhotos(new ArrayList<Photo>());

                String notes = cursor.getString(cursor.getColumnIndex(COL_TRIP_NOTES));

                if (notes == null) {
                    trip.setNotes("");
                } else {
                    trip.setNotes(notes);
                }

                trips.add(trip);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return trips;
    }

    /*
* //This method deletes a trip from the database.
*/
    public void deleteTrip(long id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String string = String.valueOf(id);
        db.execSQL("DELETE FROM " + TRIP_TABLE_NAME + " WHERE " + COL_TRIP_ID + "=" + id + "");
    }



    public long createPlaceVisit(String visitDate, String notes, String travelCompanions, long place_id, long trip_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PLACE_VISIT_DATE, visitDate);
        cv.put(COL_PLACE_VISIT_NOTES, notes);
        cv.put(COL_PLACE_VISIT_TRAVEL_COMPANIONS, travelCompanions);

        if (trip_id > 0) {
            cv.put(COL_TRIP_ID_FOREIGN_PLACE_VISIT, trip_id);
        }

        return db.insert(PLACE_VISIT_TABLE_NAME, null, cv);
    }



    public long createPhoto(String photoSrc, long placeVisitID, long tripID, String tag)
    {
       SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_PHOTO_SRC, photoSrc);

        if (placeVisitID > 0) {
            cv.put(COL_PLACE_VISIT_ID_FOREIGN_PHOTOS, placeVisitID);
        }

        if (tripID > 0) {
            cv.put(COL_TRIP_ID_FOREIGN_PHOTOS, tripID);
        }

        cv.put(COL_TAGS, tag);

        return db.insert(PHOTOS_TABLE_NAME, null, cv);
    }

    public ArrayList<Photo> getTravelGalleryPhotos() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Photo> photos = new ArrayList<Photo>();

        Cursor cursor = db.rawQuery(
                "SELECT ph.photoId, ph.photoSrc, ph.placeVisitId, ph.tripId FROM photos ph "
                , new String [] {});

        if (cursor.moveToFirst())
        {
            do {
                Photo photo = new Photo();

                photo.setPhotoId(cursor.getInt(cursor.getColumnIndex(COL_PHOTO_ID)));
                photo.setPath(cursor.getString(cursor.getColumnIndex(COL_PHOTO_SRC)));



                int placeVisitID = cursor.getInt(cursor.getColumnIndex(COL_PLACE_VISIT_ID_FOREIGN_PHOTOS));

                if (placeVisitID > 0) {
                    photo.setPlaceId(cursor.getInt(cursor.getColumnIndex(COL_PLACE_VISIT_ID_FOREIGN_PHOTOS)));
                } else {
                    photo.setPlaceId(0);
                }

                int tripID = cursor.getInt(cursor.getColumnIndex(COL_TRIP_ID_FOREIGN_PHOTOS));

                if (tripID > 0) {
                    photo.setTripId(cursor.getInt(cursor.getColumnIndex(COL_TRIP_ID_FOREIGN_PHOTOS)));
                } else {
                    photo.setTripId(0);
                }

                //Set Photo tags here
                photo.setTags("");

                photos.add(photo);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return photos;

    }

}
