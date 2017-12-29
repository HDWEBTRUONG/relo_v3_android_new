package jp.relo.cluboff.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import framework.phvtActivity.BaseActivity;
import jp.relo.cluboff.R;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.LoginSharedPreference;

/**
 * Created by HuyTran on 3/21/17.
 */

public class SplashScreenActivity extends BaseActivity {
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] layouts;
    private LinearLayout dotsLayout;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushvisorHandlerActivity.checkOpenedThisScreen=false;
    }


    private void goNextScreen() {
        PushvisorHandlerActivity.checkOpenedThisScreen = true;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void getMandatoryViews(Bundle savedInstanceState) {
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        layouts = new int[]{
                R.layout.splash_1_layout,
                R.layout.splash_2_layout,
                R.layout.splash_3_layout,
                R.layout.splash_5_layout,
                R.layout.splash_1_layout};
        addBottomDots(0);
        myViewPagerAdapter = new SplashScreenActivity.MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

    }

    @Override
    protected void registerEventHandlers() {
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
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            int lastIdx = myViewPagerAdapter.getCount() - 1;
            int curItem = viewPager.getCurrentItem();
            /*if(curItem==lastIdx  && state==1){
                lastPageChange = true;
            }else  {
                lastPageChange = false;
            }*/

            if (curItem == lastIdx){
                goNextScreen();
            }
        }
    };

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length - 1; i++) {
            dots[i] = new TextView(this);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int)getResources().getDimension(R.dimen._2dp),(int)getResources().getDimension(R.dimen._1dp),
                    (int)getResources().getDimension(R.dimen._2dp),(int)getResources().getDimension(R.dimen._1dp));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_COMPACT));
            } else {
                dots[i].setText(Html.fromHtml("&#8226;"));
            }
            dots[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._27dp));
            if(i==currentPage){
                dots[i].setTextColor(ContextCompat.getColor(this,R.color.dot_seleced));
            }else{
                dots[i].setTextColor(ContextCompat.getColor(this,R.color.dot));
            }
            dots[i].setLayoutParams(layoutParams);
            dotsLayout.addView(dots[i]);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        Button btnCloseSplash;
        ImageView ivSpash;
        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            ivSpash = (ImageView) view.findViewById(R.id.ivSpash);
            switch (position){
                case 0:
                    Glide.with(SplashScreenActivity.this).load(R.drawable.wt_1).into(ivSpash);
                    break;
                case 1:
                    Glide.with(SplashScreenActivity.this).load(R.drawable.wt_2).into(ivSpash);
                    break;
                case 2:
                    Glide.with(SplashScreenActivity.this).load(R.drawable.wt_3).into(ivSpash);
                    break;
                case 3:
                    Glide.with(SplashScreenActivity.this).load(R.drawable.wt_4).into(ivSpash);
                    break;
                case 4 :
                    Glide.with(SplashScreenActivity.this).load(R.color.white).into(ivSpash);
                    break;
            }

            if(position==layouts.length-2){
                btnCloseSplash = (Button) view.findViewById(R.id.btnCloseSplash);
                if(btnCloseSplash!=null){
                    btnCloseSplash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean notFirst = LoginSharedPreference.getInstance(SplashScreenActivity.this).get(Constant.TAG_IS_FIRST, Boolean.class);
                            if(notFirst){
                                finish();
                            }else{
                                LoginSharedPreference.getInstance(SplashScreenActivity.this).put(Constant.TAG_IS_FIRST, true);
                                goNextScreen();
                            }
                        }
                    });
                }
            }
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
