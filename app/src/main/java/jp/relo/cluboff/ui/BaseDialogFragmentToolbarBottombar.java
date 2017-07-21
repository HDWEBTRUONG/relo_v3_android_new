package jp.relo.cluboff.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.relo.cluboff.R;

/**
 * Created by tonkhanh on 7/21/17.
 */

public abstract class BaseDialogFragmentToolbarBottombar extends BaseDialogFragmentToolbar {
    protected LinearLayout lnBottom;
    protected ImageView imvBackBottomBar;
    protected ImageView imvForwardBottomBar;
    protected ImageView imvBrowserBottomBar;
    protected ImageView imvReloadBottomBar;

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lnBottom = (LinearLayout) view.findViewById(R.id.lnBottom);
        imvBackBottomBar = (ImageView) view.findViewById(R.id.imvBackBottomBar);
        imvForwardBottomBar = (ImageView) view.findViewById(R.id.imvForwardBottomBar);
        imvBrowserBottomBar = (ImageView) view.findViewById(R.id.imvBrowserBottomBar);
        imvReloadBottomBar = (ImageView) view.findViewById(R.id.imvReloadBottomBar);
    }

    @Override
    public void onActivityCreated(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupBottomlbar();
    }

    public abstract void setupBottomlbar();
}
