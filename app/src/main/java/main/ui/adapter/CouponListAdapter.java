package main.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;

import main.R;
import main.model.CouponDTO;
import main.ui.model.Coupon;

/**
 * Created by quynguyen on 3/27/17.
 */

public class CouponListAdapter extends BaseAdapter{
    private ArrayList<CouponDTO> listData;
    private LayoutInflater layoutInflater;
    private Context mContext;
    iClickButton miClickButton;

    public CouponListAdapter(Context mContext, ArrayList<CouponDTO> listData,iClickButton miClickButton) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.miClickButton = miClickButton;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public CouponDTO getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list_coupon, null);
            holder = new ViewHolder();
            holder.categoryView = (TextView) convertView.findViewById(R.id.tvCategoryName);
            holder.companyView = (TextView) convertView.findViewById(R.id.tvCompanyName);
            holder.durationCoupon = (TextView) convertView.findViewById(R.id.tvDurationCoupon);
            holder.img_item_coupon = (ImageView) convertView.findViewById(R.id.img_item_coupon);
            holder.bntDetail = (Button) convertView.findViewById(R.id.btnDetail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.categoryView.setText(listData.get(position).getCategory());
        holder.companyView.setText(listData.get(position).getName());
        holder.durationCoupon.setText(MessageFormat.format(mContext.getString(R.string.limit_time),listData.get(position).getLimit()));

        Picasso.with(mContext)
                .load(listData.get(position).getP_url())
                .into(holder.img_item_coupon);

        holder.bntDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(miClickButton!=null){
                    miClickButton.callback(listData.get(position));
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView categoryView;
        TextView companyView;
        TextView durationCoupon;
        ImageView img_item_coupon;
        Button bntDetail;
    }
    public interface iClickButton{
        void callback(CouponDTO data);
    }
}
