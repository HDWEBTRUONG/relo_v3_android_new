package net.fukuri.memberapp.views;

import android.content.Context;
import android.widget.ListAdapter;

import com.jaredrummler.materialspinner.MaterialSpinnerBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonkhanh on 7/11/17.
 */

public class MaterialSpinnerAdapterWrapper  extends MaterialSpinnerBaseAdapter {

    private final ListAdapter listAdapter;

    public MaterialSpinnerAdapterWrapper(Context context, ListAdapter toWrap) {
        super(context);
        listAdapter = toWrap;
    }

    @Override public int getCount() {
        return listAdapter.getCount();
    }

    @Override public Object getItem(int position) {
        return listAdapter.getItem(position);
    }

    @Override public Object get(int position) {
        return listAdapter.getItem(position);
    }

    @Override public List<Object> getItems() {
        List<Object> items = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            items.add(getItem(i));
        }
        return items;
    }

}
