package net.fukuri.memberapp.memberapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.fukuri.memberapp.memberapp.R;


/**
 * Created by tonkhanh on 6/9/17.
 */

public class MenuListAdapter extends BaseAdapter {

    //---------------------------------------------------------------------
    //base context
    Context context;

    //title menu string arrays
    String[] mTitle;

    //view Inflater
    LayoutInflater inflater;


    //---------------------------------------------------------------------
    public MenuListAdapter(Context context, String[] title) {
        this.context = context;
        this.mTitle = title;
    }

    //---------------------------------------------------------------------
    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitle[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //title
        TextView txtTitle;

        //icon
        ImageView imgIcon;

        //inflated view
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.drawer_list_item, parent,
                false);

        // Locate the TextViews in drawer_list_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.title);

        // Locate the ImageView in drawer_list_item.xml
        imgIcon = (ImageView) itemView.findViewById(R.id.icon);

        // Set the results into TextViews
        if(position == mTitle.length-1){
            txtTitle.setText(mTitle[position]);
            txtTitle.setTypeface(null, Typeface.BOLD);

        }else{
            txtTitle.setText(mTitle[position]);
        }
        return itemView;
    }

}