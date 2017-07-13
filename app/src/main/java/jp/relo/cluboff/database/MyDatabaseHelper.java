package jp.relo.cluboff.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.model.CouponDTO;
import jp.relo.cluboff.util.Utils;

/**
 * Created by tonkhanh on 5/23/17.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_COUPON = "TB_COUPON";
    public static final String COLUMN_SHGRID = "shgrid";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY = "category_name";
    public static final String COLUMN_COUPON_ID = "couponID";
    public static final String COLUMN_COUPON = "coupon_name";
    public static final String COLUMN_COUPON_IAMGE_PATH = "coupon_image_path";
    public static final String COLUMN_COUPON_TYPE = "coupon_type";
    public static final String COLUMN_LINK_PATH = "link_path";
    public static final String COLUMN_EXPIRATION_FROM = "expiration_from";
    public static final String COLUMN_EXPIRATION_TO = "expiration_to";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_MEMO = "memo";
    public static final String COLUMN_ADD_BLAND = "add_bland";
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
                + COLUMN_COUPON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SHGRID + " TEXT,"
                + COLUMN_CATEGORY_ID + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_COUPON + " TEXT,"
                + COLUMN_COUPON_IAMGE_PATH + " TEXT,"
                + COLUMN_COUPON_TYPE + " TEXT,"
                + COLUMN_LINK_PATH + " TEXT,"
                + COLUMN_EXPIRATION_FROM + " TEXT,"
                + COLUMN_EXPIRATION_TO + " TEXT,"
                + COLUMN_PRIORITY + " TEXT,"
                + COLUMN_MEMO + " TEXT,"
                + COLUMN_ADD_BLAND + " TEXT,"
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
        values.put(COLUMN_SHGRID, data.getShgrid());
        values.put(COLUMN_CATEGORY_ID, data.getCategory_id());
        values.put(COLUMN_CATEGORY, data.getCategory_name());
        values.put(COLUMN_COUPON, data.getCoupon_name());
        values.put(COLUMN_COUPON_IAMGE_PATH, data.getCoupon_image_path());
        values.put(COLUMN_COUPON_TYPE, data.getCoupon_type());
        values.put(COLUMN_LINK_PATH, data.getLink_path());
        values.put(COLUMN_EXPIRATION_FROM, data.getExpiration_from());
        values.put(COLUMN_EXPIRATION_TO, data.getExpiration_to());
        values.put(COLUMN_PRIORITY, data.getPriority());
        values.put(COLUMN_MEMO, data.getMemo());
        values.put(COLUMN_ADD_BLAND, data.getAdd_bland());
        values.put(COLUMN_LIKE, 0);

        db.insert(TABLE_COUPON, null, values);
        db.close();
    }
    public void saveCouponList(List<CouponDTO> datas){
        SQLiteDatabase db = this.getWritableDatabase();
        for(CouponDTO data:datas){
            saveCoupon(data);
        }
        db.close();
    }
    public ArrayList<CouponDTO> getAllCoupon(){
        ArrayList<CouponDTO> datas= new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COUPON;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CouponDTO note = new CouponDTO();
                note.setID(cursor.getInt(0));
                note.setShgrid(cursor.getString(1));
                note.setCategory_id(cursor.getString(2));
                note.setCategory_name(cursor.getString(3));
                note.setCoupon_name(cursor.getString(4));
                note.setCoupon_image_path(cursor.getString(5));
                note.setCoupon_type(cursor.getString(6));
                note.setLink_path(cursor.getString(7));
                note.setExpiration_from(cursor.getString(8));
                note.setExpiration_to(cursor.getString(9));
                note.setPriority(cursor.getInt(10));
                note.setMemo(cursor.getString(11));
                note.setAdd_bland(cursor.getString(12));
                note.setLiked(cursor.getInt(13));
                datas.add(note);
            } while (cursor.moveToNext());
        }

        return datas;
    }


    public ArrayList<CouponDTO> getCouponWithDateCategoryID(String categoryID){
        ArrayList<CouponDTO> datas= new ArrayList<>();
        String now= Utils.valueNowTime();
        String selectQuery = "";
        if("".equals(categoryID)){
            selectQuery = "SELECT  * FROM " + TABLE_COUPON +" WHERE "
                    +COLUMN_EXPIRATION_FROM+" < "+ now + " AND "+
                    COLUMN_EXPIRATION_TO +" > "+now;
        }else{
            selectQuery = "SELECT  * FROM " + TABLE_COUPON +" WHERE "+COLUMN_CATEGORY_ID+" = '"+categoryID+"' AND ("
                    +COLUMN_EXPIRATION_FROM+" < "+ now + " AND "+
                    COLUMN_EXPIRATION_TO +" > "+now+")";
        }
        AppLog.log(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CouponDTO note = new CouponDTO();
                note.setID(cursor.getInt(0));
                note.setShgrid(cursor.getString(1));
                note.setCategory_id(cursor.getString(2));
                note.setCategory_name(cursor.getString(3));
                note.setCoupon_name(cursor.getString(4));
                note.setCoupon_image_path(cursor.getString(5));
                note.setCoupon_type(cursor.getString(6));
                note.setLink_path(cursor.getString(7));
                note.setExpiration_from(cursor.getString(8));
                note.setExpiration_to(cursor.getString(9));
                note.setPriority(cursor.getInt(10));
                note.setMemo(cursor.getString(11));
                note.setAdd_bland(cursor.getString(12));
                note.setLiked(cursor.getInt(13));
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

        db.update(TABLE_COUPON, newValues, COLUMN_COUPON_ID+"="+id, null);
    }
    public ArrayList<CatagoryDTO> getCategory(){
        ArrayList<CatagoryDTO> datas= new ArrayList<>();
        String selectQuery = "SELECT DISTINCT "+COLUMN_CATEGORY_ID+", "+COLUMN_CATEGORY+" FROM " + TABLE_COUPON;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CatagoryDTO catagoryDTO = new CatagoryDTO();
                catagoryDTO.setCatagoryID(cursor.getString(0));
                catagoryDTO.setGetCatagoryName(cursor.getString(1));
                datas.add(catagoryDTO);
            } while (cursor.moveToNext());
        }

        return datas;
    }


}
