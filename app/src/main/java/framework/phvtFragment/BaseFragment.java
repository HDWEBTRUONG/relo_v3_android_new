package framework.phvtFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import framework.phvtActivity.BaseActivity;
import framework.phvtCommon.AppState;
import framework.phvtCommon.FragmentTransitionInfo;
import framework.phvtRest.HttpRequestClient;
import framework.phvtUtils.AppLog;

import net.fukuri.memberapp.R;
import net.fukuri.memberapp.ReloApp;
import net.fukuri.memberapp.api.ApiClient;
import net.fukuri.memberapp.api.ApiClientForceUpdate;
import net.fukuri.memberapp.api.ApiClientJP;
import net.fukuri.memberapp.api.ApiClientLog;
import net.fukuri.memberapp.api.ApiInterface;
import net.fukuri.memberapp.ui.activity.MainTabActivity;
import net.fukuri.memberapp.util.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Base Fragment for all Fragment in the application
 *
 * @author thaonp & phatvt
 */
public abstract class BaseFragment extends Fragment {

    private KProgressHUD kProgressHUDloading;
    private CompositeSubscription mCompositeSubscription;
    protected ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    protected ApiInterface apiInterfaceJP = ApiClientJP.getClient().create(ApiInterface.class);
    protected ApiInterface apiInterfaceLog = ApiClientLog.getClient().create(ApiInterface.class);
    protected ApiInterface apiInterfaceForceUpdate = ApiClientForceUpdate.getClient().create(ApiInterface.class);

    public MainTabActivity mainTabActivity;
    //----------------------------------------------------------------------------------------------------
    /**
     * Root layout view
     */
    protected View mRootLayout;
    //----------------------------------------------------------------------------------------------------

    /**
     * Get class tag
     *
     * @return Full quality class name
     */
    public static String classTag() {
        return BaseFragment.class.getName();
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Get root layout view id of the fragment.
     *
     * @return Root layout view id
     */
    public abstract @LayoutRes
    int getRootLayoutId();

    protected abstract void getMandatoryViews(View root, Bundle savedInstanceState);

    protected abstract void registerEventHandlers();
    //----------------------------------------------------------------------------------------------------

    /**
     * Get top navigation fragment container ID
     *
     * @return
     */
    public int getTopNavigationFragmentContainerId() {
        return -1;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootLayout =  inflater.inflate(getRootLayoutId(), container, false);
        getMandatoryViews(mRootLayout, savedInstanceState);
        registerEventHandlers();
        if(getActivity() instanceof MainTabActivity){
            mainTabActivity = (MainTabActivity) getActivity();
        }
        return mRootLayout;
    }

    //--------------------------------------------------------------------------------------------------------------------
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!AppState.instance().getIsEnableFragmentTransitionAnimation()) {
            Animation nullAnimation = new Animation() {
            };
            nullAnimation.setDuration(0);
            return nullAnimation;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Add child fragment to parent fragment
     *
     * @param child       Child fragment
     * @param viewGroupId Fragment container ID
     * @param tag         Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
     * @param transition  Transition animations
     * @return <code>true</code> if added successfully, otherwise is <code>false</code>
     */
    public boolean addChildFragment(final Fragment child, int viewGroupId, String tag, FragmentTransitionInfo transition) {
        return FragmentHelper.addChildFragment(this, child, viewGroupId, tag, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Add child fragment to parent fragment by child fragment class tag name
     *
     * @param viewGroupId               Fragment container ID
     * @param childFragmentClassNameTag Full quality child fragment class name
     * @param addToBackStack            <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>childFragmentClassNameTag</code> tag name
     * @param data                      Passed data
     * @param transition                Transition animations
     * @return <code>true</code> if added successfully, otherwise is <code>false</code>
     */
    public boolean addChildFragment(int viewGroupId, String childFragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
        return FragmentHelper.addChildFragment(this, viewGroupId, childFragmentClassNameTag, addToBackStack, data, transition);
    }
    public void switchChildFragment(Fragment fragment, FragmentManager childFragmentManager, int idContent) {
        String FRAGMENT_TAG = fragment.getClass().getSimpleName();
        childFragmentManager.beginTransaction()
                .replace(idContent, fragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }
    public void switchFragment(Fragment fragment, MainTabActivity activity, Bundle bundle) {
        String FRAGMENT_TAG = fragment.getClass().getSimpleName();
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction()
                //.setCustomAnimations(R.anim.fadein,R.anim.fadeout)
                .replace(R.id.flContainerCouponList, fragment, FRAGMENT_TAG)
                .addToBackStack(FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Replace fragment from parent fragment of specific ViewGroup ID
     *
     * @param child       Child fragment
     * @param viewGroupId Fragment container ID
     * @param tag         Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
     * @param transition  Transition animations
     * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
     */
    public boolean replaceChildFragment(final Fragment child, int viewGroupId, String tag, FragmentTransitionInfo transition) {
        return FragmentHelper.replaceChildFragment(this, child, viewGroupId, tag, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Replace fragment from parent fragment of specific ViewGroup ID by child fragment class tag name
     *
     * @param viewGroupId               Fragment container ID
     * @param childFragmentClassNameTag Full quality child fragment class name
     * @param addToBackStack            <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>childFragmentClassNameTag</code> tag name
     * @param data                      Passed data
     * @param transition                Transition animations
     * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
     */
    public boolean replaceChildFragment(int viewGroupId, String childFragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
        return FragmentHelper.replaceChildFragment(this, viewGroupId, childFragmentClassNameTag, addToBackStack, data, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Find child fragment from parent fragment by tab name.
     *
     * @param tag Tag of child fragment when add or replace to parent fragment by transaction
     * @return Found <code>Fragment</code> or <code>null</code> if not found
     */
    public Fragment findChildFragmentByTag(final String tag) {
        return FragmentHelper.findChildFragmentByTag(this, tag);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Clear children fragment back stack
     */
    public void clearChildBackStack() {
        FragmentHelper.clearChildBackStack(this);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Clear children fragment back stack by Iterator
     */
    public void clearChildrenBackStackByIterator() {
        FragmentHelper.clearChildrenBackStackByIterator(this);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Pop back stack fragment
     */
    public void popChildFragmentBackStack() {
        getChildFragmentManager().popBackStack();
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Open URL by Default Browser from Fragment
     */
    public void openUrlByDefaultBrowser(String url) {
        if (url == null || url.trim().isEmpty()) {
            Toast.makeText(this.getActivity().getApplicationContext(), "Can not open null or empty URL", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Get owner class which is holding this fragment
     *
     * @return BaseActivity instance or null if activity of fragment is not an instance of SampleActivity
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseActivity>T getOwnerActivity() {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            return (T) activity;
        } else {
            return null;
        }
    }
    //----------------------------------------------------------------------------------------------------


    // Asynctask Management
    private ArrayList<AsyncTask> serviceList;
    private final int MAX_REQUEST = 5;

    //----------------------------------------------------------------------------------------------------

    /**
     * Add running AsyncHttpTask
     *
     * @param aRunningTask
     */
    public void addAsyncHttpTask(AsyncTask aRunningTask) {
        if (aRunningTask == null) {
            return;
        }
        if (serviceList == null) {
            serviceList = new ArrayList<>();
        }
        if (serviceList.size() > MAX_REQUEST) {
            AppLog.log("[PHVT Network]", "Network Sync is full. It's need recycling...Remove top ! Current Size = " + serviceList.size());
            AsyncTask request = serviceList.get(0);
            if (request != null)
                request.cancel(true);

            serviceList.remove(0);
        }
        serviceList.add(aRunningTask);
        AppLog.log("[PHVT Network]", "Manager Async :: Size = " + serviceList.size() + " ");
    }

    /**
     * Remove running AsyncHttpTask out of the list
     *
     * @param aRunningTask
     */
    public void removeAsyncHttpTask(HttpRequestClient aRunningTask) {
        if (aRunningTask == null || serviceList == null) {
            return;
        }
        serviceList.remove(aRunningTask);
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Cancel (stop/interrupt) running AsyncHttpTask and remove it out of the list
     *
     * @param aRunningTask
     */
    public void cancelAysncHttpTask(AsyncTask aRunningTask, boolean mayInterrrup) {
        if (aRunningTask == null || serviceList == null) {
            return;
        }
        int indexOfRunningTask = serviceList.indexOf(aRunningTask);
        if (indexOfRunningTask != -1) {
            AsyncTask asyncHttpRequest = serviceList.get(indexOfRunningTask);
            asyncHttpRequest.cancel(mayInterrrup);
            serviceList.remove(indexOfRunningTask);
            asyncHttpRequest = null;
        }
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Cancel (stop/interrupt) all running AsyncHttpTask and clear the list
     */
    public void cancelAllAsyncHttpTask(boolean mayInterrrup) {

        if (serviceList != null) {
            for (AsyncTask asyncHttpRequest : serviceList) {
                AppLog.log("[PHVT Network]", "CLear - Cancel Asyntask " + asyncHttpRequest.getClass().hashCode());
                asyncHttpRequest.cancel(mayInterrrup);
                asyncHttpRequest = null;
            }
            serviceList.clear();
        }
    }


    //----------------------------------------------------------------------------------------------------

    /**
     * Client request :
     * Request API Method general
     *
     * @param client
     */

    public void requestAPI(HttpRequestClient client) {
        addAsyncHttpTask(client);
        client.execute();
    }
    public void removeView(){
        ((ViewGroup)mRootLayout).removeAllViews();
    }

    public void showLoadingData(Context context){
        if(kProgressHUDloading==null){
            kProgressHUDloading=KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Downloading data")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        }
        if(!kProgressHUDloading.isShowing()){
            kProgressHUDloading.show();
        }

    }
    public void showLoading(Context context){
        if(context==null) return;
        if(isVisible()){
            if(kProgressHUDloading==null){
                kProgressHUDloading=KProgressHUD.create(context)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f);
            }
            if(!kProgressHUDloading.isShowing()){
                try{
                    kProgressHUDloading.show();
                }catch (Exception ex){
                    AppLog.log(ex.toString());
                }
            }
        }

    }

    public void hideLoading(){
        if(isVisible()){
            if(kProgressHUDloading!=null&&kProgressHUDloading.isShowing()){
                try{
                    kProgressHUDloading.dismiss();
                }catch (Exception ex){
                    AppLog.log(ex.toString());
                }
            }
        }

    }
    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(subscriber));


    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions())
            mCompositeSubscription.unsubscribe();
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
