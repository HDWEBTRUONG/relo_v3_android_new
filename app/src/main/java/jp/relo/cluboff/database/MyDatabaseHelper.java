package jp.relo.cluboff.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.database.tables.TableCategory;
import jp.relo.cluboff.database.tables.TableCoupon;
import jp.relo.cluboff.database.tables.TablePush;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.model.CouponDTO;
import jp.relo.cluboff.model.HistoryPushDTO;
import jp.relo.cluboff.util.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * Created by tonkhanh on 5/23/17.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;

    public MyDatabaseHelper(Context context)  {
        super(context, ConstansDB.DATABASE_NAME, null, ConstansDB.DATABASE_VERSION);
    }
    public SQLiteDatabase getSqLiteDatabase(){
        return this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script to create table.
        String scriptCoupon = "CREATE TABLE " + TableCoupon.TABLE_COUPON + "("
                + TableCoupon.COLUMN_COUPON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TableCoupon.COLUMN_SHGRID + " TEXT,"
                + TableCoupon.COLUMN_CATEGORY_ID + " TEXT,"
                + TableCoupon.COLUMN_CATEGORY + " TEXT,"
                + TableCoupon.COLUMN_COUPON + " TEXT,"
                + TableCoupon.COLUMN_COUPON_IAMGE_PATH + " TEXT,"
                + TableCoupon.COLUMN_COUPON_TYPE + " TEXT,"
                + TableCoupon.COLUMN_LINK_PATH + " TEXT,"
                + TableCoupon.COLUMN_EXPIRATION_FROM + " TEXT,"
                + TableCoupon.COLUMN_EXPIRATION_TO + " TEXT,"
                + TableCoupon.COLUMN_PRIORITY + " TEXT,"
                + TableCoupon.COLUMN_MEMO + " TEXT,"
                + TableCoupon.COLUMN_ADD_BLAND + " TEXT,"
                + TableCoupon.COLUMN_LIKE + " INTEGER"+ ")";
        // Execute script.
        db.execSQL(scriptCoupon);

        String scriptPush = "CREATE TABLE " + TablePush.TABLE_PUSH + "("
                + TablePush.COLUMN_PUSH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TablePush.COLUMN_PUSH_TITLE + " TEXT,"
                + TablePush.COLUMN_PUSH_TIME + " INTEGER,"
                + TablePush.COLUMN_PUSH_CONTENT + " TEXT,"
                + TablePush.COLUMN_PUSH_X + " TEXT,"
                + TablePush.COLUMN_PUSH_Y + " TEXT,"
                + TablePush.COLUMN_PUSH_Z + " TEXT,"
                + TablePush.COLUMN_PUSH_W + " TEXT,"
                + TablePush.COLUMN_PUSH_URL + " TEXT,"
                + TablePush.COLUMN_PUSH_READ + " INTEGER"
                + ")";
        // Execute script.
        db.execSQL(scriptPush);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TableCoupon.TABLE_COUPON);
        db.execSQL("DROP TABLE IF EXISTS " + TablePush.TABLE_PUSH);
        // Recreate
        onCreate(db);
    }
    public void saveCoupon(CouponDTO data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableCoupon.COLUMN_SHGRID, data.getShgrid());
        values.put(TableCoupon.COLUMN_CATEGORY_ID, data.getCategory_id());
        values.put(TableCoupon.COLUMN_CATEGORY, data.getCategory_name());
        values.put(TableCoupon.COLUMN_COUPON, data.getCoupon_name());
        values.put(TableCoupon.COLUMN_COUPON_IAMGE_PATH, data.getCoupon_image_path());
        values.put(TableCoupon.COLUMN_COUPON_TYPE, data.getCoupon_type());
        values.put(TableCoupon.COLUMN_LINK_PATH, data.getLink_path());
        values.put(TableCoupon.COLUMN_EXPIRATION_FROM, data.getExpiration_from());
        values.put(TableCoupon.COLUMN_EXPIRATION_TO, data.getExpiration_to());
        values.put(TableCoupon.COLUMN_PRIORITY, data.getPriority());
        values.put(TableCoupon.COLUMN_MEMO, data.getMemo());
        values.put(TableCoupon.COLUMN_ADD_BLAND, data.getAdd_bland());
        values.put(TableCoupon.COLUMN_LIKE, 0);

        db.insert(TableCoupon.TABLE_COUPON, null, values);
        db.close();
    }
    public void saveCouponList(ArrayList<CouponDTO> datas){
        SQLiteDatabase db = this.getWritableDatabase();
        for(CouponDTO data:datas){
            saveCoupon(data);
        }
        db.close();
    }

    public ArrayList<CouponDTO> getCouponWithDateCategoryID(String categoryID){
        ArrayList<CouponDTO> datas= new ArrayList<>();
        String now= Utils.valueNowTime();
        String selectQuery = "";
        if(ConstansDB.COUPON_ALL.equals(categoryID)){
            selectQuery = "SELECT  * FROM " + TableCoupon.TABLE_COUPON +" WHERE "
                    +TableCoupon.COLUMN_EXPIRATION_FROM+" < "+ now + " AND "+
                    TableCoupon.COLUMN_EXPIRATION_TO +" > "+now;
        }else if(ConstansDB.COUPON_FAV.equals(categoryID)) {
            selectQuery = "SELECT  * FROM " + TableCoupon.TABLE_COUPON +" WHERE "+TableCoupon.COLUMN_LIKE +" = '"+1+"' AND ("
                    +TableCoupon.COLUMN_EXPIRATION_FROM+" < "+ now + " AND "+
                    TableCoupon.COLUMN_EXPIRATION_TO +" > "+now+")";
        }
        else{
            selectQuery = "SELECT  * FROM " + TableCoupon.TABLE_COUPON +" WHERE "+TableCoupon.COLUMN_CATEGORY_ID+" = '"+categoryID+"' AND ("
                    +TableCoupon.COLUMN_EXPIRATION_FROM+" < "+ now + " AND "+
                    TableCoupon.COLUMN_EXPIRATION_TO +" > "+now+")";
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
        db.close();
        return datas;
    }

    public void clearData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableCoupon.TABLE_COUPON, null, null);
    }
    public void likeCoupon(int id, int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(TableCoupon.COLUMN_LIKE, value);
        db.update(TableCoupon.TABLE_COUPON, newValues, TableCoupon.COLUMN_COUPON_ID+"="+id, null);
        db.close();
    }

    public void savePush(HistoryPushDTO data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TablePush.COLUMN_PUSH_TITLE, data.getTitlePush());
        values.put(TablePush.COLUMN_PUSH_TIME, data.getTimeHis());
        values.put(TablePush.COLUMN_PUSH_CONTENT, data.getContentHis());
        values.put(TablePush.COLUMN_PUSH_X, data.getxHis());
        values.put(TablePush.COLUMN_PUSH_Y, data.getyHis());
        values.put(TablePush.COLUMN_PUSH_Z, data.getzHis());
        values.put(TablePush.COLUMN_PUSH_W, data.getzHis());
        values.put(TablePush.COLUMN_PUSH_URL, data.getUrlHis());
        values.put(TablePush.COLUMN_PUSH_READ, 0);
        db.insert(TablePush.TABLE_PUSH, null, values);
        db.close();
    }

    public ArrayList<HistoryPushDTO> getPush(){
        ArrayList<HistoryPushDTO> datas= new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TablePush.TABLE_PUSH + " LIMIT "+ ConstansDB.LIMIT_PUSH_LIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HistoryPushDTO item = new HistoryPushDTO();
                item.setIdHis(cursor.getInt(0));
                item.setTitlePush(cursor.getString(1));
                item.setTimeHis(cursor.getInt(2));
                item.setContentHis(cursor.getString(3));
                item.setxHis(cursor.getString(4));
                item.setyHis(cursor.getString(5));
                item.setzHis(cursor.getString(6));
                item.setwHis(cursor.getString(7));
                item.setUrlHis(cursor.getString(8));
                item.setIsReaded(cursor.getInt(9));

                datas.add(item);
            } while (cursor.moveToNext());
        }
        db.close();
        return datas;
    }
    public long getCountPush(){
        SQLiteDatabase db = this.getWritableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TablePush.TABLE_PUSH,TablePush.COLUMN_PUSH_READ+" = 0");
        db.close();
        return cnt;
    }

    public void readCoupon(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(TablePush.COLUMN_PUSH_READ, 1);
        db.update(TablePush.TABLE_PUSH, newValues, TablePush.COLUMN_PUSH_ID+"="+id, null);
        db.close();
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch(Exception ex) {
                            Log.e("s", "Error reading from the database", ex);
                        }
                    }
                });
    }

    public Observable<List<CatagoryDTO>> getCatagorysRX(String categoryID) {
        return makeObservable(TableCategory.getCategory(this))
                .subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io()
    }
    public Observable<List<CouponDTO>> getCouponWithDateCategoryIDRX(String categoryID) {
        return makeObservable(TableCoupon.getCouponWithDateCategoryID(this,categoryID))
                .subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io()
    }



}
