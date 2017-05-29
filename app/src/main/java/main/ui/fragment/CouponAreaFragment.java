package main.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import framework.phvtFragment.BaseFragment;
import main.R;

/**
 * Created by HuyTran on 3/21/17.
 */

public class CouponAreaFragment extends BaseFragment {
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        addChildFragment(R.id.frContainerWebview, WebViewFragment.class.getName(),false,getArguments(),null);
    }

    @Override
    protected void registerEventHandlers() {

    }

}
