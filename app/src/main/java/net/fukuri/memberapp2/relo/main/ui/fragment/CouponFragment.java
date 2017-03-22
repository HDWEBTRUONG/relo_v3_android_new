package net.fukuri.memberapp2.relo.main.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.fukuri.memberapp2.relo.R;
import net.fukuri.memberapp2.relo.framework.phvtFragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HuyTran on 3/21/17.
 */

public class CouponFragment extends BaseFragment {

    @BindView(R.id.textView)
    TextView tvCouponTitle;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_coupon;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        ButterKnife.bind(this, root);
        tvCouponTitle.setText("Hello world");
    }

    @Override
    protected void registerEventHandlers() {

    }
}
