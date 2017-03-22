package net.fukuri.memberapp2.relo.main.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import net.fukuri.memberapp2.relo.R;
import net.fukuri.memberapp2.relo.framework.phvtActivity.BaseActivity;
import net.fukuri.memberapp2.relo.main.ui.fragment.CouponAreaFragment;
import net.fukuri.memberapp2.relo.main.ui.fragment.CouponFragment;
import net.fukuri.memberapp2.relo.main.ui.fragment.MemberFragment;

public class MainActivity extends BaseActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(R.id.content, CouponFragment.class.getName(), false, null, null);
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(R.id.content, CouponAreaFragment.class.getName(), false, null, null);
                    return true;
                case R.id.navigation_notifications:
                    replaceFragment(R.id.content, MemberFragment.class.getName(), false, null, null);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(R.id.content, CouponFragment.class.getName(), false, null, null);
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
