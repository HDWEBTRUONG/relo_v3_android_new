package main.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import main.R;
import main.ui.model.Coupon;

/**
 * Created by quynguyen on 3/27/17.
 */

public class CouponListAdapter extends BaseAdapter{
    private ArrayList<Coupon> listData;
    private LayoutInflater layoutInflater;

    public CouponListAdapter(Context aContext, ArrayList<Coupon> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Coupon getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list_coupon, null);
            holder = new ViewHolder();
            holder.categoryView = (TextView) convertView.findViewById(R.id.tvCategoryName);
            holder.companyView = (TextView) convertView.findViewById(R.id.tvCompanyName);
            holder.durationCoupon = (TextView) convertView.findViewById(R.id.tvDurationCoupon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.categoryView.setText(listData.get(position).getCategoryName());
        holder.companyView.setText(listData.get(position).getCompanyName());
        holder.durationCoupon.setText(listData.get(position).getDurationCoupon());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Selected :", Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView categoryView;
        TextView companyView;
        TextView durationCoupon;
    }
}
