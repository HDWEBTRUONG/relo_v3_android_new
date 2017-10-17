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
 * Created by tonkhanh on 6/8/17.
 */

public abstract class BaseDialogFragmentToolbar extends BaseDialogFragment{
    protected ImageView ivMenuRight;
    protected TextView tvMenuTitle;
    protected TextView tvMenuSubTitle;

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ivMenuRight = (ImageView) view.findViewById(R.id.ivMenuRight);
        tvMenuTitle = (TextView) view.findViewById(R.id.tvMenuTitle);
        tvMenuSubTitle = (TextView) view.findViewById(R.id.tvMenuSubTitle);
        setupActionBar();
    }

    public abstract void setupActionBar();
}
