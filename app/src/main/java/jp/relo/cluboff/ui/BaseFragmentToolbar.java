package jp.relo.cluboff.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import org.jetbrains.annotations.Nullable;

import framework.phvtFragment.BaseFragment;
import jp.relo.cluboff.R;

/**
 * Created by tonkhanh on 6/8/17.
 */

public abstract class BaseFragmentToolbar extends BaseFragment{
    protected ImageView ivMenuRight;
    protected TextView tvMenuTitle;
    protected TextView tvMenuSubTitle;
    protected TextView tvTitleCenter;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivMenuRight = (ImageView) view.findViewById(R.id.ivMenuRight);
        tvMenuTitle = (TextView) view.findViewById(R.id.tvMenuTitle);
        tvMenuSubTitle = (TextView) view.findViewById(R.id.tvMenuSubTitle);
        tvTitleCenter = (TextView) view.findViewById(R.id.tvTitleCenter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
    }

    public abstract void setupToolbar();
}
