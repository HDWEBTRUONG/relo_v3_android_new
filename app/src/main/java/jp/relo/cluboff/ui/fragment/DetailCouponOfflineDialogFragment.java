package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.view.View;

import jp.relo.cluboff.R;
import jp.relo.cluboff.ui.BaseDialogFragmentToolbar;

/**
 * Created by tonkhanh on 11/30/17.
 */

public class DetailCouponOfflineDialogFragment extends BaseDialogFragmentToolbar{

    public static DetailCouponOfflineDialogFragment newInstance() {

        Bundle args = new Bundle();

        DetailCouponOfflineDialogFragment fragment = new DetailCouponOfflineDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupActionBar() {
        tvMenuTitle.setText("Detail");
        tvMenuSubTitle.setText("Detail Coupon");
        ivMenuRight.setVisibility(View.VISIBLE);
        ivMenuRight.setImageResource(R.drawable.icon_close);
        ivMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.detail_coupon_offline_layout;
    }

    @Override
    public void bindView(View view) {

    }
}
