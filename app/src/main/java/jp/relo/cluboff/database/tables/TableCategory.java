package jp.relo.cluboff.database.tables;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.util.Constant;

/**
 * Created by tonkhanh on 7/21/17.
 */

public class TableCategory {

    public static Callable<List<CatagoryDTO>> getCategory(final MyDatabaseHelper mMyDatabaseHelper) {
        return new Callable<List<CatagoryDTO>>() {
            @Override
            public List<CatagoryDTO> call() {
                List<CatagoryDTO> datas= new ArrayList<>();
                String selectQuery = "SELECT DISTINCT "+TableCoupon.COLUMN_CATEGORY_ID+", "
                        +TableCoupon.COLUMN_CATEGORY+" FROM " + TableCoupon.TABLE_COUPON ;
                SQLiteDatabase db = mMyDatabaseHelper.getSqLiteDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        CatagoryDTO catagoryDTO = new CatagoryDTO();
                        catagoryDTO.setCatagoryID(cursor.getString(0));
                        catagoryDTO.setGetCatagoryName(cursor.getString(1));
                        datas.add(catagoryDTO);
                    } while (cursor.moveToNext());
                }
                db.close();


                return sortBSJ(datas);
            }
        };
    }
    private static List<CatagoryDTO> sortBSJ(List<CatagoryDTO> list){
        List<CatagoryDTO> datas= new ArrayList<>();
        List<String> temp = java.util.Arrays.asList(Constant.listCategoryName);
        for(String item : temp){
            for(int i = 0; i < list.size(); i++){
                if(item.equalsIgnoreCase(list.get(i).getGetCatagoryName())){
                    datas.add(list.get(i));
                    list.remove(i);
                    break;
                }
            }
        }

        //add item orther
        for(int i = 0; i < list.size(); i++){
            datas.add(list.get(i));
        }
        return datas;
    }
}
