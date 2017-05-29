package main.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import main.model.CouponDTO;

/**
 * Created by tonkhanh on 5/23/17.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_COUPON = "TB_COUPON";
    public static final String COLUMN_CATEGORY_ID = "couponID";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LIMIT = "limits";
    public static final String COLUMN_P_URL = "p_url";
    public static final String COLUMN_C_URL = "c_url";
    public static final String COLUMN_LIKE = "like";

    public static final String DATABASE_NAME = "relo.db";
    public static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script to create table.
        String scriptCoupon = "CREATE TABLE " + TABLE_COUPON + "("
                + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_LIMIT + " TEXT,"
                + COLUMN_P_URL + " TEXT,"
                + COLUMN_C_URL + " TEXT,"
                + COLUMN_LIKE + " INTEGER"+ ")";
        // Execute script.
        db.execSQL(scriptCoupon);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPON);
        // Recreate
        onCreate(db);
    }
    public void saveCoupon(CouponDTO data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, data.getCategory());
        values.put(COLUMN_NAME, data.getName());
        values.put(COLUMN_LIMIT, data.getLimit());
        values.put(COLUMN_P_URL, data.getP_url());
        values.put(COLUMN_C_URL, data.getC_url());
        values.put(COLUMN_LIKE, 0);

        db.insert(TABLE_COUPON, null, values);
        db.close();
    }
    public void saveCouponList(List<CouponDTO> datas){
        SQLiteDatabase db = this.getWritableDatabase();
        for(CouponDTO data:datas){
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY, data.getCategory());
            values.put(COLUMN_NAME, data.getName());
            values.put(COLUMN_LIMIT, data.getLimit());
            values.put(COLUMN_P_URL, data.getP_url());
            values.put(COLUMN_C_URL, data.getC_url());
            values.put(COLUMN_C_URL, data.getC_url());
            values.put(COLUMN_LIKE, 0);
            db.insert(TABLE_COUPON, null, values);
        }
        db.close();
    }
    public List<CouponDTO> getAllCoupon(){
        List<CouponDTO> datas= new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COUPON;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CouponDTO note = new CouponDTO();
                note.setId(cursor.getInt(0));
                note.setCategory(cursor.getString(1));
                note.setName(cursor.getString(2));
                note.setLimit(cursor.getString(3));
                note.setP_url(cursor.getString(5));
                note.setC_url(cursor.getString(6));
                note.setLike(cursor.getInt(7));
                datas.add(note);
            } while (cursor.moveToNext());
        }

        return datas;
    }
    public void clearData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUPON, null, null);
    }
    public void likeCoupon(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_LIKE, 1);

        db.update(TABLE_COUPON, newValues, COLUMN_CATEGORY_ID+"="+id, null);
    }

}
