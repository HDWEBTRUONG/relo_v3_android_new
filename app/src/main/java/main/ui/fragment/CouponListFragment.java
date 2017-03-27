package main.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import framework.phvtFragment.BaseFragment;
import main.R;
import main.ReloApp;
import main.ui.adapter.CouponListAdapter;
import main.ui.model.Coupon;
import main.util.Constant;

/**
 * Created by HuyTran on 3/21/17.
 */

public class CouponListFragment extends BaseFragment {

    @BindView(R.id.bt_menu_category)
    Button btnMenuCategory;

    @BindView(R.id.list_category_listview)
    ListView lvCategoryMenu;

    @BindArray(R.array.fragment_list_category_coupon)
    String[] listCategoryCoupon;

    ArrayAdapter<String> itemsAdapter;

    private Unbinder unbinder;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_coupon_list;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        ButterKnife.bind(this, root);
        Toolbar toolbar = ButterKnife.findById(root, R.id.toolbar);
        TextView title = ButterKnife.findById(toolbar, R.id.toolbar_title);
        title.setText(R.string.title_coupon_list);

        ArrayList listCoupon = getListData();
        final ListView lvCategory = ButterKnife.findById(root, R.id.list_category_listview);
        lvCategory.setAdapter(new CouponListAdapter(getContext(), listCoupon));

    }


    private ArrayList getListData() {
        ArrayList<Coupon> results = new ArrayList<Coupon>();

        String categoryName = getResources().getString(R.string.item_coupon_category_name);
        String companyName = getResources().getString(R.string.item_coupon_company_name);
        String durationCoupon = getResources().getString(R.string.item_coupon_duration);

        for (int i=0; i<10; i++) {
            Coupon data = new Coupon();
            data.setCategoryName(categoryName);
            data.setCompanyName(companyName);
            data.setDurationCoupon(durationCoupon);
            results.add(data);
        }

        // Add some more dummy data for testing
        return results;
    }

    @OnClick(R.id.bt_menu_category)
    public void clickCategoryMenu(){
        //Set Style for PopupMenu
        Context wrapper = new ContextThemeWrapper(getContext(), R.style.popup_menu);
        PopupMenu popup = new PopupMenu(wrapper, btnMenuCategory);
        popup.getMenuInflater().inflate(R.menu.popup_categories, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();//showing popup menu
    }


    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //Test GA
        Activity act = getActivity();
        if (act != null) {
            ReloApp app = (ReloApp) act.getApplication();
            app.trackingAnalytics(Constant.GA_POPULAR_COUPON_SCREEN);
        }
    }

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/
}