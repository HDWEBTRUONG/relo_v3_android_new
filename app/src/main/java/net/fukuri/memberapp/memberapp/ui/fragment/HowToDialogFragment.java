package net.fukuri.memberapp.memberapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.ReloApp;
import net.fukuri.memberapp.memberapp.ui.BaseDialogFragment;
import net.fukuri.memberapp.memberapp.util.Constant;

/**
 * Created by tonkhanh on 7/31/17.
 */

public class HowToDialogFragment extends BaseDialogFragment {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] layouts;
    private LinearLayout dotsLayout;
    private TextView[] dots;

    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ((ReloApp)getActivity().getApplication()).trackingAnalytics(Constant.GA_HOWTO_SCREEN);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void init(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) view.findViewById(R.id.layoutDots);
        layouts = new int[]{
                R.layout.tutorial_1_layout,
                R.layout.tutorial_2_layout,
                R.layout.tutorial_3_layout,
                R.layout.tutorial_4_layout};
        addBottomDots(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.dialog_fragment_howto;
    }

    @Override
    public void bindView(View view) {

    }
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_COMPACT));
            } else {
                dots[i].setText(Html.fromHtml("&#8226;"));
            }
            dots[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._35dp));
            if(i==currentPage){
                dots[i].setTextColor(ContextCompat.getColor(getActivity(),R.color.dot_seleced));
            }else{
                dots[i].setTextColor(ContextCompat.getColor(getActivity(),R.color.dot));
            }
            dots[i].setLayoutParams(layoutParams);
            dotsLayout.addView(dots[i]);
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        boolean lastPageChange = false;
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            int lastIdx = myViewPagerAdapter.getCount() - 1;
            if(lastPageChange && position == lastIdx) {
                dismiss();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            int lastIdx = myViewPagerAdapter.getCount() - 1;
            int curItem = viewPager.getCurrentItem();
            if(curItem==lastIdx  && state==1){
                lastPageChange = true;
            }else  {
                lastPageChange = false;
            }
        }
    };

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
