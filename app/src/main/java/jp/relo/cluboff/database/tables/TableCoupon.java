package jp.relo.cluboff.database.tables;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.database.ConstansDB;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.model.CouponDTO;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.Utils;

/**
 * Created by tonkhanh on 7/17/17.
 */

public class TableCoupon {

    public static final String TABLE_COUPON = "TB_COUPON";
    public static final String COLUMN_SHGRID = "shgrid";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY = "category_name";
    public static final String COLUMN_COUPON = "coupon_name";
    public static final String COLUMN_COUPON_EN = "coupon_name_en";
    public static final String COLUMN_COUPON_IAMGE_PATH = "coupon_image_path";
    public static final String COLUMN_COUPON_TYPE = "coupon_type";
    public static final String COLUMN_LINK_PATH = "link_path";
    public static final String COLUMN_EXPIRATION_FROM = "expiration_from";
    public static final String COLUMN_EXPIRATION_TO = "expiration_to";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_MEMO = "memo";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_BENEFIT = "benefit";
    public static final String COLUMN_BENEFIT_NOTES = "benefit_notes";

    public static Callable<List<CouponDTO>> getCouponWithDateCategoryID(final MyDatabaseHelper mMyDatabaseHelper,
                                                                        final String categoryID, final String area) {
        return new Callable<List<CouponDTO>>() {
            @Override
            public List<CouponDTO> call() {
                ArrayList<CouponDTO> datas= new ArrayList<>();
                String now= Utils.valueNowTime();
                String selectQuery = "";
                if(ConstansDB.COUPON_ALL.equals(categoryID)){

                    selectQuery = "SELECT  A.* , B."+TableFav.COLUMN_LIKE+" FROM " + TableCoupon.TABLE_COUPON + " AS A LEFT JOIN "+TableFav.TABLE_FAV
                            + " AS B ON A."+TableCoupon.COLUMN_SHGRID+ " = B."+TableFav.COLUMN_SHGRID +" WHERE "
                            +TableCoupon.COLUMN_EXPIRATION_FROM+" < "+ now + " AND "+
                            TableCoupon.COLUMN_EXPIRATION_TO +" > "+now + " AND "+TableCoupon.COLUMN_AREA  +" = '"+area+ "' ORDER BY A."+TableCoupon.COLUMN_PRIORITY +" DESC";

                }else if(ConstansDB.COUPON_FAV.equals(categoryID)) {

                    selectQuery = "SELECT  A.* , B."+TableFav.COLUMN_LIKE+" FROM " + TableCoupon.TABLE_COUPON + " AS A LEFT JOIN "+TableFav.TABLE_FAV
                            + " AS B ON A."+TableCoupon.COLUMN_SHGRID+ " = B."+TableFav.COLUMN_SHGRID
                            +" WHERE B."+TableFav.COLUMN_LIKE+" = '"+1+"' AND ("
                            +TableCoupon.COLUMN_EXPIRATION_FROM+" < "+ now + " AND "+
                            TableCoupon.COLUMN_EXPIRATION_TO +" > "+now+")" + " AND "+TableCoupon.COLUMN_AREA  +" = '"+area + "' ORDER BY A."+TableCoupon.COLUMN_PRIORITY +" DESC";

                }
                else{

                    selectQuery = "SELECT  A.* , B."+TableFav.COLUMN_LIKE+" FROM " + TableCoupon.TABLE_COUPON + " AS A LEFT JOIN "+TableFav.TABLE_FAV
                            + " AS B ON A."+TableCoupon.COLUMN_SHGRID+ " = B."+TableFav.COLUMN_SHGRID +" WHERE "+TableCoupon.COLUMN_CATEGORY_ID+" = '"+categoryID+"' AND ("
                            +TableCoupon.COLUMN_EXPIRATION_FROM+" < "+ now + " AND "+
                            TableCoupon.COLUMN_EXPIRATION_TO +" > "+now+")"   + " AND "+TableCoupon.COLUMN_AREA  +" = '"+area+  "' ORDER BY A."+TableCoupon.COLUMN_PRIORITY +" DESC";

                }
                AppLog.log("selectQuery: "+ selectQuery);
                SQLiteDatabase db = mMyDatabaseHelper.getSqLiteDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor!=null && cursor.getCount()>0 && cursor.moveToFirst()) {
                    do {
                        CouponDTO note = new CouponDTO();
                        note.setShgrid(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_SHGRID)));
                        note.setCategory_id(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_CATEGORY_ID)));
                        note.setCategory_name(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_CATEGORY)));
                        note.setCoupon_name(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_COUPON)));
                        note.setCoupon_name_en(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_COUPON_EN)));
                        note.setCoupon_image_path(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_COUPON_IAMGE_PATH)));
                        note.setCoupon_type(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_COUPON_TYPE)));
                        note.setLink_path(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_LINK_PATH)));
                        note.setExpiration_from(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_EXPIRATION_FROM)));
                        note.setExpiration_to(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_EXPIRATION_TO)));
                        note.setPriority(cursor.getInt(cursor.getColumnIndex(TableCoupon.COLUMN_PRIORITY)));
                        note.setMemo(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_MEMO)));
                        note.setArea(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_AREA)));
                        note.setBenefit(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_BENEFIT)));
                        note.setBenefit_notes(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_BENEFIT_NOTES)));

                        datas.add(note);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                if( db.isOpen()){
                    db.close();
                }
                return datas;
            }
        };
    }

    //detail
    public static Callable<CouponDTO> getCouponDetail(final MyDatabaseHelper mMyDatabaseHelper,
                                                                        final String couponID, final String area ) {
        return new Callable<CouponDTO>() {
            @Override
            public CouponDTO call() {
                CouponDTO note = new CouponDTO();
                String selectQuery = "select * from TB_COUPON where shgrid = '"+couponID+"' and area = '"+area+"'";
                AppLog.log("++++++: "+selectQuery);
                SQLiteDatabase db = mMyDatabaseHelper.getSqLiteDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor!=null && cursor.getCount()>0 && cursor.moveToFirst()) {
                        note.setShgrid(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_SHGRID)));
                        note.setCategory_id(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_CATEGORY_ID)));
                        note.setCategory_name(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_CATEGORY)));
                        note.setCoupon_name(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_COUPON)));
                        note.setCoupon_name_en(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_COUPON_EN)));
                        note.setCoupon_image_path(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_COUPON_IAMGE_PATH)));
                        note.setCoupon_type(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_COUPON_TYPE)));
                        note.setLink_path(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_LINK_PATH)));
                        note.setExpiration_from(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_EXPIRATION_FROM)));
                        note.setExpiration_to(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_EXPIRATION_TO)));
                        note.setPriority(cursor.getInt(cursor.getColumnIndex(TableCoupon.COLUMN_PRIORITY)));
                        note.setMemo(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_MEMO)));
                        note.setArea(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_AREA)));
                        note.setBenefit(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_BENEFIT)));
                        note.setBenefit_notes(cursor.getString(cursor.getColumnIndex(TableCoupon.COLUMN_BENEFIT_NOTES)));
                }
                cursor.close();
                if( db.isOpen()){
                    db.close();
                }
                return note;
            }
        };
    }


}
