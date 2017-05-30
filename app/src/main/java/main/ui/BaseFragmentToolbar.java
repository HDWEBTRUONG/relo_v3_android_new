package main.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import framework.phvtFragment.BaseFragment;
import main.R;

/**
 * Created by tonkhanh on 5/22/17.
 */

public abstract class BaseFragmentToolbar extends BaseFragment {
    protected Toolbar mToolbar;
    protected TextView mToolbarTilte;
    protected LinearLayout lnGroupArrow;
    protected ImageView imgBack;
    protected ImageView imgForward;
    protected RelativeLayout rlGroupClose;
    protected LinearLayout lnGroupTitle;
    protected ImageView imgClose;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbarTilte = (TextView) view.findViewById(R.id.title_toolbar);
        lnGroupArrow = (LinearLayout) view.findViewById(R.id.group_bt_arrow);
        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgForward = (ImageView) view.findViewById(R.id.imgForward);
        rlGroupClose = (RelativeLayout) view.findViewById(R.id.group_close);
        lnGroupTitle = (LinearLayout) view.findViewById(R.id.group_title);
        imgClose = (ImageView) view.findViewById(R.id.imgClose);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
    }

    public abstract void setupToolbar();
}
