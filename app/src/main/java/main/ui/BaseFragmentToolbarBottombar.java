package main.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import framework.phvtFragment.BaseFragment;
import main.R;

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



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lnToolbar = (LinearLayout) view.findViewById(R.id.lnToolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        imvMenu = (ImageView) view.findViewById(R.id.imvMenu);
        imvInfo = (ImageView) view.findViewById(R.id.imvInfo);
        tvCount = (TextView) view.findViewById(R.id.tvCount);
        title_toolbar = (TextView) view.findViewById(R.id.title_toolbar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
    }

    public abstract void setupToolbar();
}
