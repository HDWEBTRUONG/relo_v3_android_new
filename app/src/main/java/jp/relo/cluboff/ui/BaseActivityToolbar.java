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
    protected ImageView ivMenuRight;
    protected TextView tvMenuTitle;
    protected TextView tvMenuSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivMenuRight = (ImageView) findViewById(R.id.ivMenuRight);
        tvMenuTitle = (TextView) findViewById(R.id.tvMenuTitle);
        tvMenuSubTitle = (TextView) findViewById(R.id.tvMenuSubTitle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupToolbar();
    }
    public abstract void setupToolbar();
}
