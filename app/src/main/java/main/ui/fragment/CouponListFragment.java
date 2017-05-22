package main.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import framework.phvtFragment.BaseFragment;
import main.R;
import main.ReloApp;
import main.ui.BaseFragmentToolbar;
import main.ui.adapter.CouponListAdapter;
import main.ui.model.Coupon;
import main.util.Constant;

/**
 * Created by HuyTran on 3/21/17.
 */

public class CouponListFragment extends BaseFragmentToolbar implements View.OnClickListener{

    Button btnMenuCategory;
    ListView lvCategoryMenu;
    String[] listCategoryCoupon;
    ArrayAdapter<String> itemsAdapter;
    MaterialSpinner spinner;

    private void init(View view) {
        btnMenuCategory = (Button) view.findViewById(R.id.bt_menu_category);
        lvCategoryMenu = (ListView) view.findViewById(R.id.list_category_listview);
        spinner = (MaterialSpinner) view.findViewById(R.id.spinnerCategory);
        spinner.setItems("カテゴリを選ぶ 1", "カテゴリを選ぶ 2", "カテゴリを選ぶ 3", "カテゴリを選ぶ 4", "カテゴリを選ぶ 5");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(getActivity(), "Clicked " + item, Toast.LENGTH_SHORT).show();
            }
        });
        btnMenuCategory.setOnClickListener(this);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_coupon_list;
    }
    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {
        init(root);
        //setToolbar();

        ArrayList listCoupon = getListData();
        final ListView lvCategory = (ListView)root.findViewById(R.id.list_category_listview);
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bt_menu_category){
            //clickCategoryMenu();
            spinner.expand();
        }
    }

    @Override
    public void setupToolbar() {
        mToolbarTilte.setText(getString(R.string.title_coupon_list));
        imgClose.setVisibility(View.GONE);
        lnGroupTitle.setVisibility(View.VISIBLE);
        lnGroupArrow.setVisibility(View.GONE);
        rlGroupClose.setVisibility(View.GONE);
    }

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/
}