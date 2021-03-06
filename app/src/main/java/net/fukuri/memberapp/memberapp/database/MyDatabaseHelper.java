package net.fukuri.memberapp.memberapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import framework.phvtUtils.AppLog;
import net.fukuri.memberapp.memberapp.database.tables.TableCategory;
import net.fukuri.memberapp.memberapp.database.tables.TableCoupon;
import net.fukuri.memberapp.memberapp.database.tables.TableFav;
import net.fukuri.memberapp.memberapp.database.tables.TablePush;
import net.fukuri.memberapp.memberapp.model.CatagoryDTO;
import net.fukuri.memberapp.memberapp.model.CouponDTO;
import net.fukuri.memberapp.memberapp.model.HistoryPushDTO;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * Created by tonkhanh on 5/23/17.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static MyDatabaseHelper myDatabaseHelper;
    private Context mContext;
    SQLiteDatabase sqLiteDatabase;

    public static MyDatabaseHelper getInstance(Context context) {
        if(myDatabaseHelper == null){
            myDatabaseHelper = new MyDatabaseHelper(context);
        }
        return myDatabaseHelper;
    }

    public MyDatabaseHelper(Context context)  {
        super(context, ConstansDB.DATABASE_NAME, null, ConstansDB.DATABASE_VERSION);
        mContext= context;
    }
    public SQLiteDatabase getSqLiteDatabase(){
        if(sqLiteDatabase==null){
            sqLiteDatabase = this.getWritableDatabase();
        }
        return this.getReadableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script to create table.
        String scriptCoupon = "CREATE TABLE " + TableCoupon.TABLE_COUPON + "("
                + TableCoupon.COLUMN_SHGRID + " TEXT,"
                + TableCoupon.COLUMN_CATEGORY_ID + " TEXT,"
                + TableCoupon.COLUMN_CATEGORY + " TEXT,"
                + TableCoupon.COLUMN_COUPON + " TEXT,"
                + TableCoupon.COLUMN_COUPON_EN + " TEXT,"
                + TableCoupon.COLUMN_COUPON_IAMGE_PATH + " TEXT,"
                + TableCoupon.COLUMN_COUPON_TYPE + " TEXT,"
                + TableCoupon.COLUMN_LINK_PATH + " TEXT,"
                + TableCoupon.COLUMN_EXPIRATION_FROM + " TEXT,"
                + TableCoupon.COLUMN_EXPIRATION_TO + " TEXT,"
                + TableCoupon.COLUMN_PRIORITY + " INTEGER,"
                + TableCoupon.COLUMN_MEMO + " TEXT,"
                + TableCoupon.COLUMN_AREA + " TEXT,"
                + TableCoupon.COLUMN_BENEFIT + " TEXT,"
                + TableCoupon.COLUMN_BENEFIT_NOTES + " TEXT)";
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
    public void saveCoupon(CouponDTO data, String area){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableCoupon.COLUMN_SHGRID, data.getShgrid());
        values.put(TableCoupon.COLUMN_CATEGORY_ID, data.getCategory_id());
        values.put(TableCoupon.COLUMN_CATEGORY, data.getCategory_name());
        values.put(TableCoupon.COLUMN_COUPON, data.getCoupon_name());
        values.put(TableCoupon.COLUMN_COUPON_EN, data.getCoupon_name_en());
        values.put(TableCoupon.COLUMN_COUPON_IAMGE_PATH, data.getCoupon_image_path());
        values.put(TableCoupon.COLUMN_COUPON_TYPE, data.getCoupon_type());
        values.put(TableCoupon.COLUMN_LINK_PATH, data.getLink_path());
        values.put(TableCoupon.COLUMN_EXPIRATION_FROM, data.getExpiration_from());
        values.put(TableCoupon.COLUMN_EXPIRATION_TO, data.getExpiration_to());
        values.put(TableCoupon.COLUMN_PRIORITY, data.getPriority());
        values.put(TableCoupon.COLUMN_MEMO, data.getMemo());
        values.put(TableCoupon.COLUMN_AREA, area);
        values.put(TableCoupon.COLUMN_BENEFIT, data.getBenefit());
        values.put(TableCoupon.COLUMN_BENEFIT_NOTES, data.getBenefit_notes());
        try {
            db.insert(TableCoupon.TABLE_COUPON, null, values);
        }catch (Exception ex){
            AppLog.log(ex.toString());
        }finally {
            db.close();
        }

    }
    public void saveCouponList(ArrayList<CouponDTO> datas, String area){
        SQLiteDatabase db = this.getWritableDatabase();
        for(CouponDTO data:datas){
            saveCoupon(data, area);
        }
        db.close();
    }

    public void clearData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableCoupon.TABLE_COUPON, null, null);
        db.close();
    }
    public void likeCoupon(String id, int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(TableFav.COLUMN_SHGRID, id);
        newValues.put(TableFav.COLUMN_LIKE, value);
        db.insertWithOnConflict(TableFav.TABLE_FAV, null, newValues, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void savePush(HistoryPushDTO data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TablePush.COLUMN_PUSH_TITLE, data.getTitlePush());
        values.put(TablePush.COLUMN_PUSH_TIME, String.valueOf(data.getTimeHis()));
        values.put(TablePush.COLUMN_PUSH_CONTENT, data.getContentHis());
        values.put(TablePush.COLUMN_PUSH_X, data.getxHis());
        values.put(TablePush.COLUMN_PUSH_Y, data.getyHis());
        values.put(TablePush.COLUMN_PUSH_Z, data.getzHis());
        values.put(TablePush.COLUMN_PUSH_W, data.getwHis());
        values.put(TablePush.COLUMN_PUSH_READ, 0);
        db.insert(TablePush.TABLE_PUSH, null, values);
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
                            subscriber.onError(new Throwable());
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                ;
    }

    public List<CatagoryDTO> getCatagorys(String area) {
        return TableCategory.getCategorys(MyDatabaseHelper.getInstance(mContext),area);

    }

    public List<CouponDTO> getCouponWithDateCategoryIDs(String categoryID, String area) {
        return TableCoupon.getCouponWithDateCategoryIDs(MyDatabaseHelper.getInstance(mContext),categoryID, area);
    }

    public CouponDTO getCouponDetails(String couponID, String area) {
        return TableCoupon.getCouponDetails(MyDatabaseHelper.getInstance(mContext),couponID, area);
    }


    public Observable<List<CatagoryDTO>> getCatagorysRX(String area) {
        return makeObservable(TableCategory.getCategory(MyDatabaseHelper.getInstance(mContext),area))
                .subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io()
    }
    public Observable<List<CouponDTO>> getCouponWithDateCategoryIDRX(String categoryID, String area) {
        return makeObservable(TableCoupon.getCouponWithDateCategoryID(MyDatabaseHelper.getInstance(mContext),categoryID, area));
                //.subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io()
    }
    public Observable <CouponDTO> getCouponDetail(String couponID, String area) {
        return makeObservable(TableCoupon.getCouponDetail(MyDatabaseHelper.getInstance(mContext),couponID, area));
                //.subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io()
    }


}
