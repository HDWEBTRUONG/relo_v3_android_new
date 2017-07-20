package jp.relo.cluboff.ui;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import framework.phvtActivity.BaseActivity;
import jp.relo.cluboff.R;

/**
 * Created by tonkhanh on 6/8/17.
 */

public abstract class BaseActivityToolbar extends BaseActivity {
    protected LinearLayout lnToolbar;
    protected Toolbar toolbar;
    protected ImageView imvMenu;
    protected ImageView imvInfo;
    protected TextView tvCount;
    protected TextView title_toolbar;
    protected RelativeLayout rlMenu;
    protected RelativeLayout flInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lnToolbar = (LinearLayout) findViewById(R.id.lnToolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imvMenu = (ImageView) findViewById(R.id.imvMenu);
        imvInfo = (ImageView) findViewById(R.id.imvInfo);
        tvCount = (TextView) findViewById(R.id.tvCount);
        title_toolbar = (TextView) findViewById(R.id.title_toolbar);
        rlMenu = (RelativeLayout) findViewById(R.id.rlMenu);
        flInfo = (RelativeLayout) findViewById(R.id.flInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupToolbar();
    }
    public abstract void setupToolbar();
}
