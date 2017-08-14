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
import jp.relo.cluboff.database.tables.TableFav;
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
                + TableCoupon.COLUMN_SHGRID + " TEXT PRIMARY KEY,"
                + TableCoupon.COLUMN_CATEGORY_ID + " TEXT,"
                + TableCoupon.COLUMN_CATEGORY + " TEXT,"
                + TableCoupon.COLUMN_COUPON + " TEXT,"
                + TableCoupon.COLUMN_COUPON_IAMGE_PATH + " TEXT,"
                + TableCoupon.COLUMN_COUPON_TYPE + " TEXT,"
                + TableCoupon.COLUMN_LINK_PATH + " TEXT,"
                + TableCoupon.COLUMN_EXPIRATION_FROM + " TEXT,"
                + TableCoupon.COLUMN_EXPIRATION_TO + " TEXT,"
                + TableCoupon.COLUMN_PRIORITY + " INTEGER,"
                + TableCoupon.COLUMN_MEMO + " TEXT,"
                + TableCoupon.COLUMN_ADD_BLAND + " TEXT)";
        // Execute script.
        db.execSQL(scriptCoupon);

        String scriptPush = "CREATE TABLE " + TablePush.TABLE_PUSH + "("
                + TablePush.COLUMN_PUSH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TablePush.COLUMN_PUSH_TITLE + " TEXT,"
                + TablePush.COLUMN_PUSH_TIME + " TEXT,"
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

        String scriptFav = "CREATE TABLE " + TableFav.TABLE_FAV + "("
                + TableFav.COLUMN_SHGRID + " TEXT PRIMARY KEY,"
                + TableFav.COLUMN_LIKE + " INTEGER"
                + ")";
        // Execute script.
        db.execSQL(scriptFav);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TableCoupon.TABLE_COUPON);
        db.execSQL("DROP TABLE IF EXISTS " + TablePush.TABLE_PUSH);
        db.execSQL("DROP TABLE IF EXISTS " + TableFav.TABLE_FAV);
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

    public void clearData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableCoupon.TABLE_COUPON, null, null);
    }
    public void likeCoupon(String id, int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(TableFav.COLUMN_SHGRID, id);
        newValues.put(TableFav.COLUMN_LIKE, value);
        db.insertWithOnConflict(TableFav.TABLE_FAV, null, newValues, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void savePush(HistoryPushDTO data, boolean isURL){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TablePush.COLUMN_PUSH_TITLE, data.getTitlePush());
        values.put(TablePush.COLUMN_PUSH_TIME, String.valueOf(data.getTimeHis()));
        values.put(TablePush.COLUMN_PUSH_CONTENT, data.getContentHis());
        values.put(TablePush.COLUMN_PUSH_X, data.getxHis());
        values.put(TablePush.COLUMN_PUSH_Y, data.getyHis());
        values.put(TablePush.COLUMN_PUSH_Z, data.getzHis());
        values.put(TablePush.COLUMN_PUSH_W, data.getwHis());
        values.put(TablePush.COLUMN_PUSH_URL, data.getUrlHis());
        values.put(TablePush.COLUMN_PUSH_READ, 0);
        db.insert(TablePush.TABLE_PUSH, null, values);
        db.close();
    }
    public void updateRead(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(TablePush.COLUMN_PUSH_READ, 1);
        db.update(TablePush.TABLE_PUSH, newValues, TablePush.COLUMN_PUSH_ID +"="+id, null);
        db.close();
    }

    public long countPushUnread(){
        SQLiteDatabase db = this.getWritableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TablePush.TABLE_PUSH,TablePush.COLUMN_PUSH_READ+" = 0");
        db.close();
        return cnt;
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
    public Observable<List<CouponDTO>> getCouponWithDateCategoryIDRX(String categoryID,String brandID) {
        return makeObservable(TableCoupon.getCouponWithDateCategoryID(this,categoryID, brandID))
                .subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io()
    }


    public Observable<List<HistoryPushDTO>> getPushRX() {
        return makeObservable(TablePush.getPush(this))
                .subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io()
    }



}
