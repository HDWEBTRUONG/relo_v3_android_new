package net.fukuri.memberapp.memberapp.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import framework.phvtActivity.BaseActivity;
import net.fukuri.memberapp.memberapp.R;

/**
 * Created by tonkhanh on 6/8/17.
 */

public abstract class BaseActivityToolbar extends BaseActivity {
    protected ImageView ivMenuRight;
    protected TextView tvMenuTitle;
    protected TextView tvMenuSubTitle;
    protected TextView tvTitleCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivMenuRight = (ImageView) findViewById(R.id.ivMenuRight);
        tvMenuTitle = (TextView) findViewById(R.id.tvMenuTitle);
        tvMenuSubTitle = (TextView) findViewById(R.id.tvMenuSubTitle);
        tvTitleCenter = (TextView) findViewById(R.id.tvTitleCenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupToolbar();
    }
    public abstract void setupToolbar();
}
