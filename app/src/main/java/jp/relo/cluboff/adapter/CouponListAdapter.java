package jp.relo.cluboff.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;

import jp.relo.cluboff.R;
import jp.relo.cluboff.model.CouponDTO;
import jp.relo.cluboff.util.Utils;

/**
 * Created by quynguyen on 3/27/17.
 */

public class CouponListAdapter extends BaseAdapter{
    private ArrayList<CouponDTO> listData;
    private LayoutInflater layoutInflater;
    private Context mContext;
    iClickButton miClickButton;
    public static int ISLIKED =0;

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
    public void setDataChange(ArrayList<CouponDTO> listData){
        this.listData = listData;
        notifyDataSetChanged();
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
            holder.tvLike = (TextView) convertView.findViewById(R.id.tvLike);
            holder.img_item_coupon = (ImageView) convertView.findViewById(R.id.img_item_coupon);
            holder.imvLike = (ImageView) convertView.findViewById(R.id.imvLike);
            holder.lnBtnLike = (LinearLayout) convertView.findViewById(R.id.lnBtnLike);
            holder.lnBtnDetail = (LinearLayout) convertView.findViewById(R.id.lnBtnDetail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CouponDTO item = listData.get(position);
        holder.categoryView.setText(item.getCategory_name());
        holder.companyView.setText(item.getCoupon_name());
        holder.durationCoupon.setText(MessageFormat.format(mContext.getString(R.string.limit_time), Utils.convertDateShort(item.getExpiration_to())));

        Picasso.with(mContext)
                .load(item.getCoupon_image_path())
                .into(holder.img_item_coupon);

        holder.lnBtnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(miClickButton!=null){
                    miClickButton.callback(item);
                }
            }
        });

        //like button
        holder.lnBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miClickButton.like(item.getID(),item.getLiked());
            }
        });

        if(item.getLiked()==ISLIKED){
            holder.imvLike.setImageResource(R.drawable.icon_star_off);
            holder.tvLike.setTextColor(ContextCompat.getColor(mContext,R.color.pinkishGreyTwo));
        }else{
            holder.imvLike.setImageResource(R.drawable.icon_star_on);
            holder.tvLike.setTextColor(ContextCompat.getColor(mContext,R.color.yellowOrange));
        }

        return convertView;
    }

    static class ViewHolder {
        TextView categoryView;
        TextView companyView;
        TextView durationCoupon;
        TextView tvLike;
        ImageView img_item_coupon;
        ImageView imvLike;
        LinearLayout lnBtnLike;
        LinearLayout lnBtnDetail;
    }
    public interface iClickButton{
        void callback(CouponDTO data);
        void like(int id, int isLiked);
    }
}
