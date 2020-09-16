package com.qurankarim.moshaf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "SurahsDb";


    private static String TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "id";
    public static String SURAH_TITLE = "itemTitle";
    public static String SURAH_NUM = "itemNumber";
    public static String SURAH_VERSES_NUM = "itemVerse";
    public static String SURAH_REVELTION_TYPE = "itemType";
    public static String FAVORITE_STATUS = "fStatus";

    private static String TABLE_NAME1 = "favoriteQariTable";
    public static String KEY_ID1 = "qariId";
    public static String QARI_NAME = "qariName";
    public static String FAVORITE_STATUS1 = "fStatus";
    public static String QARI_RELATIVE_PATH = "qariRelativePath";

    // dont forget write this spaces
    private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + KEY_ID + " TEXT," + SURAH_TITLE+ " TEXT,"
            + SURAH_VERSES_NUM + " TEXT," + SURAH_REVELTION_TYPE + " TEXT,"
            + SURAH_NUM + " TEXT," + FAVORITE_STATUS+" TEXT)";


    private static String CREATE_TABLE1 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 + "("
            + KEY_ID1 + " TEXT," + QARI_NAME + " TEXT," + FAVORITE_STATUS1 + " TEXT,"
            + QARI_RELATIVE_PATH + " TEXT)";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // insert data into database
    public void insertIntoTheDatabaseQari(String id, String qari_name, String qari_path, String fav_status) {
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID1, id);
        cv.put(QARI_NAME, qari_name);
        cv.put(QARI_RELATIVE_PATH, qari_path);
        cv.put(FAVORITE_STATUS1, fav_status);
        db.insert(TABLE_NAME1, null, cv);
        Log.d("FavDB Status", qari_name + ", favstatus - " + fav_status + " - . " + cv);
    }

    // insert data into database
    public void insertIntoTheDatabaseSurah(String surah_title, int surah_num, String id, String fav_status, String surahType, String surahVersesNum) {
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SURAH_TITLE, surah_title);
        cv.put(SURAH_NUM, surah_num);
        cv.put(KEY_ID, id);
        cv.put(FAVORITE_STATUS, fav_status);
        cv.put(SURAH_REVELTION_TYPE, surahType);
        cv.put(SURAH_VERSES_NUM, surahVersesNum);
        db.insert(TABLE_NAME, null, cv);
        Log.d("FavDB Status", surah_title + ", favstatus - " + fav_status + " - . " + cv);
    }

    // read all data
    public Cursor read_all_data_qari(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME1 + " where " + KEY_ID1 + "=" + id + "";
        return db.rawQuery(sql, null, null);
    }

    // remove line from database
    public void remove_fav_qari(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME1 + " SET  " + FAVORITE_STATUS1 + " ='0' WHERE " + KEY_ID1 + "=" + id + "";
        db.execSQL(sql);
        Log.d("remove", id.toString());

    }

    // select all favorite list
    public Cursor select_all_favorite_list_qari() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME1 + " WHERE " + FAVORITE_STATUS1 + " ='1'";
        return db.rawQuery(sql, null, null);
    }


    // read all data
    public Cursor read_all_data_surah(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + KEY_ID + "=" + id + "";
        return db.rawQuery(sql, null, null);
    }

    // remove line from database
    public void remove_fav_surah(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET  " + FAVORITE_STATUS + " ='0' WHERE " + KEY_ID + "=" + id + "";
        db.execSQL(sql);
        Log.d("remove", id.toString());

    }

    // select all favorite list
    public Cursor select_all_favorite_list_surah() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + FAVORITE_STATUS + " ='1'";
        return db.rawQuery(sql, null, null);
    }
}
