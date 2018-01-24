package net.fukuri.memberapp.database.tables;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import net.fukuri.memberapp.database.ConstansDB;
import net.fukuri.memberapp.database.MyDatabaseHelper;
import net.fukuri.memberapp.model.HistoryPushDTO;
import net.fukuri.memberapp.util.Utils;

/**
 * Created by tonkhanh on 7/17/17.
 */

public class TablePush {
    public static final String TABLE_PUSH = "TB_PUSH";
    public static final String COLUMN_PUSH_ID = "push_id";
    public static final String COLUMN_PUSH_TITLE = "push_title";
    public static final String COLUMN_PUSH_TIME = "push_time";
    public static final String COLUMN_PUSH_CONTENT = "push_content";
    public static final String COLUMN_PUSH_X = "push_x";
    public static final String COLUMN_PUSH_Y = "push_y";
    public static final String COLUMN_PUSH_Z= "push_z";
    public static final String COLUMN_PUSH_W = "push_w";
    public static final String COLUMN_PUSH_READ = "push_read";

    public static Callable<List<HistoryPushDTO>> getPush(final MyDatabaseHelper mMyDatabaseHelper) {
        return new Callable<List<HistoryPushDTO>>() {
            @Override
            public List<HistoryPushDTO> call() {
                ArrayList<HistoryPushDTO> datas= new ArrayList<>();
                String selectQuery = "SELECT * FROM " + TablePush.TABLE_PUSH  +
                        " ORDER BY "+ COLUMN_PUSH_ID + " DESC " + " LIMIT "+ ConstansDB.LIMIT_PUSH_LIST;
                SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        HistoryPushDTO item = new HistoryPushDTO();
                        item.setIdHis(cursor.getInt(0));
                        item.setTitlePush(cursor.getString(1));
                        item.setTimeHis(Utils.parserLong(cursor.getString(2)));
                        item.setContentHis(cursor.getString(3));
                        item.setxHis(cursor.getString(4));
                        item.setyHis(cursor.getString(5));
                        item.setzHis(cursor.getString(6));
                        item.setwHis(cursor.getString(7));
                        item.setIsReaded(cursor.getInt(8));

                        datas.add(item);
                    } while (cursor.moveToNext());
                }
                db.close();
                return datas;
            }
        };
    }
}
