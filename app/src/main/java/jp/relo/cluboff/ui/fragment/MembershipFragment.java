package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.view.View;

import framework.phvtFragment.BaseFragment;
import jp.relo.cluboff.R;

/**
 * Created by HuyTran on 3/21/17.
 */

public class MembershipFragment extends BaseFragment {
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
