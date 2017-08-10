package jp.relo.cluboff.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import jp.relo.cluboff.R;

/**
 * Created by tonkhanh on 6/8/17.
 */

public abstract class BaseFragmentToolbarBottombar extends BaseFragmentBottombar{

    protected LinearLayout lnToolbar;
    protected Toolbar toolbar;
    protected ImageView imvMenu;
    protected ImageView imvInfo;
    protected TextView tvCount;
    protected TextView title_toolbar;
    protected RelativeLayout rlMenu;
    protected RelativeLayout flInfo;

    protected LinearLayout llBack;
    protected LinearLayout llForward;
    protected LinearLayout llBrowser;
    protected LinearLayout llReload;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lnToolbar = (LinearLayout) view.findViewById(R.id.lnToolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        imvMenu = (ImageView) view.findViewById(R.id.imvMenu);
        imvInfo = (ImageView) view.findViewById(R.id.imvInfo);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        title_toolbar = (TextView) view.findViewById(R.id.title_toolbar);
        rlMenu = (RelativeLayout) view.findViewById(R.id.rlMenu);
        flInfo = (RelativeLayout) view.findViewById(R.id.flInfo);

        llBack = (LinearLayout) view.findViewById(R.id.llBack);
        llForward = (LinearLayout) view.findViewById(R.id.llForward);
        llBrowser = (LinearLayout) view.findViewById(R.id.llBrowser);
        llReload = (LinearLayout) view.findViewById(R.id.llReload);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
    }

    public abstract void setupToolbar();
}
