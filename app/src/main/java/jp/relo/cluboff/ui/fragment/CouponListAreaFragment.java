package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.util.OnSwipeTouchListener;

/**
 * Created by tonkhanh on 10/16/17.
 */

public class CouponListAreaFragment extends CouponListFragment {
    HorizontalScrollView svMenu;
    RadioGroup rgArea;
    RadioButton rbArea1;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        svMenu = (HorizontalScrollView)view.findViewById(R.id.svMenu);
        svMenu.setVisibility(View.VISIBLE);

        rgArea =(RadioGroup) view.findViewById(R.id.rgArea);
        rbArea1 =(RadioButton) view.findViewById(R.id.rbArea1);
        rgArea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rbArea1:
                        AppLog.log("rbArea1");
                        break;
                    case R.id.rbArea2:
                        AppLog.log("rbArea2");
                        break;
                    case R.id.rbArea3:
                        AppLog.log("rbArea3");
                        break;
                    case R.id.rbArea4:
                        AppLog.log("rbArea4");
                        break;
                    case R.id.rbArea5:
                        AppLog.log("rbArea5");
                        break;
                    case R.id.rbArea6:
                        AppLog.log("rbArea6");
                        break;
                    case R.id.rbArea7:
                        AppLog.log("rbArea7");
                        break;
                    case R.id.rbArea8:
                        AppLog.log("rbArea8");
                        break;
                    case R.id.rbArea9:
                        AppLog.log("rbArea9");
                        break;
                }
            }
        });

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                int radioButtonID = rgArea.getCheckedRadioButtonId();
                View radioButton = rgArea.findViewById(radioButtonID);
                int idx = rgArea.indexOfChild(radioButton);
                if(idx<8){
                    RadioButton temp =((RadioButton)rgArea.getChildAt(idx+1));
                    temp.setChecked(true);
                    svMenu.smoothScrollTo(temp.getLeft(),0);
                }
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                int radioButtonID = rgArea.getCheckedRadioButtonId();
                View radioButton = rgArea.findViewById(radioButtonID);
                int idx = rgArea.indexOfChild(radioButton);
                if(idx>0){

                    RadioButton temp =((RadioButton)rgArea.getChildAt(idx-1));
                    temp.setChecked(true);
                    svMenu.smoothScrollTo(temp.getLeft(),0);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rbArea1.setChecked(true);
    }

}
