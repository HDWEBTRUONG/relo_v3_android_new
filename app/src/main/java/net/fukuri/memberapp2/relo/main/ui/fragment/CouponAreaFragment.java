package net.fukuri.memberapp2.relo.main.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import net.fukuri.memberapp2.relo.R;
import net.fukuri.memberapp2.relo.framework.phvtFragment.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by HuyTran on 3/21/17.
 */

public class CouponAreaFragment extends BaseFragment {
    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_area_coupon;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        Toolbar toolbar = ButterKnife.findById(root, R.id.toolbar);
        TextView title = ButterKnife.findById(toolbar, R.id.toolbar_title);
        title.setText(R.string.title_coupon_area);
    }

    @Override
    protected void registerEventHandlers() {

    }
}
