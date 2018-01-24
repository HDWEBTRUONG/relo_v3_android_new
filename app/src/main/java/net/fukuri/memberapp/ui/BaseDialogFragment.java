package net.fukuri.memberapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.kaopiz.kprogresshud.KProgressHUD;

import net.fukuri.memberapp.R;
import net.fukuri.memberapp.ReloApp;
import net.fukuri.memberapp.api.ApiClientLog;
import net.fukuri.memberapp.api.ApiInterface;
import net.fukuri.memberapp.util.Utils;

/**
 * Created by tonkhanh on 6/8/17.
 */

public abstract class BaseDialogFragment  extends DialogFragment {
    protected Activity mMainActivity;
    private KProgressHUD kProgressHUDloading;
    protected ApiInterface apiInterfaceLog = ApiClientLog.getClient().create(ApiInterface.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = activity;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getRootLayoutId(), container);
        bindView(view);
        init(view);
        setEvent(view);
        executeBase();
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity(),R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.dialog_animation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected abstract void init(View view);

    protected abstract void setEvent(View view);

    public abstract int getRootLayoutId();

    public abstract void bindView(View view);


    protected void executeBase() {
    }

    public void showLoading(Context context){
        if(kProgressHUDloading==null){
            kProgressHUDloading= KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        }
        if(!kProgressHUDloading.isShowing()){
            kProgressHUDloading.show();
        }

    }
    public void hideLoading(){
        if(kProgressHUDloading!=null&&kProgressHUDloading.isShowing()){
            kProgressHUDloading.dismiss();
        }
    }
    public void setGoogleAnalyticDetailCoupon(String category, String action, String lable, String value){
        long _value = Utils.convertLong(value);
        ReloApp reloApp = (ReloApp) getActivity().getApplication();
        reloApp.trackingWithAnalyticGoogleServices(category,action,lable,_value);
    }
    public void setGoogleAnalyticDetailCoupon(String category, String action, String lable, long value){
        ReloApp reloApp = (ReloApp) getActivity().getApplication();
        reloApp.trackingWithAnalyticGoogleServices(category,action,lable,value);
    }

}
