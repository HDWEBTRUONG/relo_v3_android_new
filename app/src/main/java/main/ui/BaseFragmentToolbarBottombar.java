package main.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import org.jetbrains.annotations.Nullable;

import framework.phvtFragment.BaseFragment;
import main.R;

/**
 * Created by tonkhanh on 6/8/17.
 */

public abstract class BaseFragmentToolbarBottombar extends BaseFragmentBottombar{
    Toolbar toolbar;
    ImageView imvMenu;
    ImageView imvInfo;
    TextView tvCount;
    TextView title_toolbar;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
