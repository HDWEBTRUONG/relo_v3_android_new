package net.fukuri.memberapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import framework.phvtFragment.BaseFragment;
import net.fukuri.memberapp.R;

/**
 * Created by tonkhanh on 5/22/17.
 */

public abstract class BaseFragmentBottombar extends BaseFragment {

    protected LinearLayout lnBottom;
    protected ImageView imvBackBottomBar;
    protected ImageView imvForwardBottomBar;
    protected ImageView imvBrowserBottomBar;
    protected ImageView imvReloadBottomBar;

    protected LinearLayout llBack;
    protected LinearLayout llForward;
    protected LinearLayout llBrowser;
    protected LinearLayout llReload;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lnBottom = (LinearLayout) view.findViewById(R.id.lnBottom);
        imvBackBottomBar = (ImageView) view.findViewById(R.id.imvBackBottomBar);
        imvForwardBottomBar = (ImageView) view.findViewById(R.id.imvForwardBottomBar);
        imvBrowserBottomBar = (ImageView) view.findViewById(R.id.imvBrowserBottomBar);
        imvReloadBottomBar = (ImageView) view.findViewById(R.id.imvReloadBottomBar);

        llBack = (LinearLayout) view.findViewById(R.id.llBack);
        llForward = (LinearLayout) view.findViewById(R.id.llForward);
        llBrowser = (LinearLayout) view.findViewById(R.id.llBrowser);
        llReload = (LinearLayout) view.findViewById(R.id.llReload);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupBottombar();
    }

    public abstract void setupBottombar();
}
