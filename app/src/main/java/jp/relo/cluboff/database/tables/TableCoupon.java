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
    public static final String COLUMN_COUPON_IAMGE_PATH = "coupon_image_path";
    public static final String COLUMN_COUPON_TYPE = "coupon_type";
    public static final String COLUMN_LINK_PATH = "link_path";
    public static final String COLUMN_EXPIRATION_FROM = "expiration_from";
    public static final String COLUMN_EXPIRATION_TO = "expiration_to";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_MEMO = "memo";
    public static final String COLUMN_ADD_BLAND = "add_bland";
    public static final String COLUMN_BENEFIT = "benefit";
    public static final String COLUMN_BENEFIT_NOTES = "benefit_notes";
    public static final String COLUMN_AREA = "area";

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
                SQLiteDatabase db = mMyDatabaseHelper.getSqLiteDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor!=null && cursor.getCount()>0 && cursor.moveToFirst()) {
                    do {
                        CouponDTO note = new CouponDTO();
                        note.setShgrid(cursor.getString(0));
                        note.setCategory_id(cursor.getString(1));
                        note.setCategory_name(cursor.getString(2));
                        note.setCoupon_name(cursor.getString(3));
                        note.setCoupon_image_path(cursor.getString(4));
                        note.setCoupon_type(cursor.getString(5));
                        note.setLink_path(cursor.getString(6));
                        note.setExpiration_from(cursor.getString(7));
                        note.setExpiration_to(cursor.getString(8));
                        note.setPriority(cursor.getInt(9));
                        note.setMemo(cursor.getString(10));
                        note.setAdd_bland(cursor.getString(11));
                        note.setLiked(cursor.getInt(12));
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
}
