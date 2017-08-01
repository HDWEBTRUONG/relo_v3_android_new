package jp.relo.cluboff.ui.fragment;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.relo.cluboff.R;
import jp.relo.cluboff.ui.BaseDialogFragment;

/**
 * Created by tonkhanh on 7/31/17.
 */

public class HowToDialogFragment extends BaseDialogFragment {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] layouts;

    @Override
    protected void init(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        layouts = new int[]{
                R.layout.tutorial_1_layout,
                R.layout.tutorial_2_layout,
                R.layout.tutorial_3_layout,
                R.layout.tutorial_4_layout};
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


    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        boolean lastPageChange = false;
        @Override
        public void onPageSelected(int position) {
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
