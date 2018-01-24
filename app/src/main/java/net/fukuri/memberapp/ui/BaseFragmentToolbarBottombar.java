package net.fukuri.memberapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import net.fukuri.memberapp.R;

/**
 * Created by tonkhanh on 6/8/17.
 */

public abstract class BaseFragmentToolbarBottombar extends BaseFragmentBottombar{

    protected ImageView ivMenuRight;
    protected TextView tvMenuTitle;
    protected TextView tvMenuSubTitle;

    protected LinearLayout llBack;
    protected LinearLayout llForward;
    protected LinearLayout llBrowser;
    protected LinearLayout llReload;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivMenuRight = (ImageView) view.findViewById(R.id.ivMenuRight);
        tvMenuTitle = (TextView) view.findViewById(R.id.tvMenuTitle);
        tvMenuSubTitle = (TextView) view.findViewById(R.id.tvMenuSubTitle);

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
