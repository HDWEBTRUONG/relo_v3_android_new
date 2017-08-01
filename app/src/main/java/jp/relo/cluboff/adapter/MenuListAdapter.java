package jp.relo.cluboff.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import jp.relo.cluboff.R;


/**
 * Created by tonkhanh on 6/9/17.
 */

public class MenuListAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    String[] mTitle;
    LayoutInflater inflater;

    public MenuListAdapter(Context context, String[] title) {
        this.context = context;
        this.mTitle = title;
    }

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
        // Declare Variables
        TextView txtTitle;
        TextView txtSubTitle;
        ImageView imgIcon;

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
            String txt = "<b>"+mTitle[position]+"</b>";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                txtTitle.setText(Html.fromHtml(txt,Html.FROM_HTML_MODE_LEGACY));
            } else {
                txtTitle.setText(Html.fromHtml(txt));
            }
        }else{
            txtTitle.setText(mTitle[position]);
        }

        return itemView;
    }

}