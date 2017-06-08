package main.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import framework.phvtActivity.BaseActivity;
import main.R;

/**
 * Created by tonkhanh on 6/8/17.
 */

public abstract class BaseActivityToolbar extends BaseActivity {
    protected Toolbar toolbar;
    protected ImageView imvMenu;
    protected ImageView imvInfo;
    protected TextView tvCount;
    protected TextView title_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imvMenu = (ImageView) findViewById(R.id.imvMenu);
        imvInfo = (ImageView) findViewById(R.id.imvInfo);
        tvCount = (TextView) findViewById(R.id.tvCount);
        title_toolbar = (TextView) findViewById(R.id.title_toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupToolbar();
    }
    public abstract void setupToolbar();
}
