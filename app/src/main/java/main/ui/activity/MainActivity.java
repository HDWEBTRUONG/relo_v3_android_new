package main.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import framework.phvtActivity.BaseActivity;
import main.R;
import main.ui.fragment.CouponAreaFragment;
import main.ui.fragment.CouponListFragment;
import main.ui.fragment.MembershipFragment;

public class MainActivity extends BaseActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(R.id.content, CouponListFragment.class.getName(), false, null, null);
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(R.id.content, CouponAreaFragment.class.getName(), false, null, null);
                    return true;
                case R.id.navigation_notifications:
                    replaceFragment(R.id.content, MembershipFragment.class.getName(), false, null, null);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(R.id.content, CouponListFragment.class.getName(), false, null, null);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

}
