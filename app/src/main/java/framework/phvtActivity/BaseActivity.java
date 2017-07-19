package framework.phvtActivity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Calendar;
import java.util.Locale;

import framework.phvtCommon.AppState;
import framework.phvtCommon.FragmentTransitionInfo;
import framework.phvtFragment.BaseFragment;
import framework.phvtFragment.FragmentHelper;
import jp.relo.cluboff.ReloApp;
import jp.relo.cluboff.api.ApiClient;
import jp.relo.cluboff.api.ApiClientJP;
import jp.relo.cluboff.api.ApiInterface;
import jp.relo.cluboff.util.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Base Fragment Activity for the application
 *
 * @author thaonp & phatvt
 */
public abstract class BaseActivity extends AppCompatActivity {
    //----------------------------------------------------------------------------------------------------
//    /**
//     * Waiting pop up for waiting doing background task
//     */
//    protected SimpleWaitingPopUp mWaitingPopUp;
//    protected ErrorPopUp mErrorPopup;

    /**
     * Current main fragment of the fragment activity
     */
    protected BaseFragment mMainActiveFragment;
    protected ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    protected ApiInterface apiInterfaceJP = ApiClientJP.getClient().create(ApiInterface.class);

    //----------------------------------------------------------------------------------------------------

    /**
     * Get class tag
     *
     * @return Full quality class name
     */
    public static String classTag() {
        return BaseActivity.class.getName();
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Inflate layout and get mandatory Views
     */
    private CompositeSubscription mCompositeSubscription;
    private KProgressHUD kProgressHUDloading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());
        getMandatoryViews(savedInstanceState);
        registerEventHandlers();
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Should not use this method directly and should implement <code>getActivityLayoutId()</code> to
     * return the View layout to <code>setContentView()</code> <br/>
     * Set the activity content from a layout resource. The resource will be inflated, adding all top-level views to the activity.
     *
     * @param layoutResID Resource ID to be inflated.
     */
    @Override
    @Deprecated
    final public void setContentView(int layoutResID) {
        super.setContentView(getActivityLayoutId());
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Get Activity Layout resource ID
     *
     * @return
     */
    protected abstract @LayoutRes
    int getActivityLayoutId();
    //----------------------------------------------------------------------------------------------------

    /**
     * Get mandatory Views for later using of this activity
     *
     * @param savedInstanceState
     */
    protected abstract void getMandatoryViews(Bundle savedInstanceState);
    //----------------------------------------------------------------------------------------------------

    protected abstract void registerEventHandlers();

    /**
     * Set current main fragment of the fragment activity
     *
     * @param currentMainFragment
     */
    public void setMainActiveFragment(BaseFragment currentMainFragment) {
        mMainActiveFragment = currentMainFragment;
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Get current main fragment of the fragment activity
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseFragment> T getMainActiveFragment() {
        return (T) mMainActiveFragment;
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Clear activity transition
     */
    public void clearTransitionAnimation() {
        getWindow().setWindowAnimations(0);
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Set activity transition
     *
     * @param transitionAnimationResourceId animation resource id
     */
    public void setTransitionAnimation(int transitionAnimationResourceId) {
        getWindow().setWindowAnimations(transitionAnimationResourceId);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Add fragment to Fragment Activity
     *
     * @param fragment    Fragment will be added
     * @param viewGroupId Fragment container ID
     * @param tag         Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
     * @param transition  Transition animations
     * @return <code>true</code> if added successfully, otherwise is <code>false</code>
     */
    public boolean addFragment(final Fragment fragment, int viewGroupId, String tag, FragmentTransitionInfo transition) {
        return FragmentHelper.addFragment(this, fragment, viewGroupId, tag, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Add fragment to Fragment Activity by fragment class tag name.
     *
     * @param viewGroupId          Fragment container ID
     * @param fragmentClassNameTag Full quality fragment class name
     * @param addToBackStack       <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>fragmentClassNameTag</code> tag name
     * @param data                 Passed data
     * @param transition           Transition animations
     * @return <code>true</code> if added successfully, otherwise is <code>false</code>
     */
    public boolean addFragment(int viewGroupId, String fragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
        return FragmentHelper.addFragment(this, viewGroupId, fragmentClassNameTag, addToBackStack, data, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Replace fragment from Fragment Activity of specific ViewGroup ID
     *
     * @param fragment    Fragment will be replaced
     * @param viewGroupId Fragment container ID
     * @param tag         Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
     * @param transition  Transition animations
     * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
     */
    public boolean replaceFragment(final Fragment fragment, int viewGroupId, final String tag, FragmentTransitionInfo transition) {
        return FragmentHelper.replaceFragment(this, fragment, viewGroupId, tag, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Replace fragment from Fragment Activity of specific ViewGroup ID by fragment class tag name.
     *
     * @param viewGroupId          Fragment container ID
     * @param fragmentClassNameTag Full quality fragment class name
     * @param addToBackStack       <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>fragmentClassNameTag</code> tag name
     * @param data                 Passed data
     * @param transition           Transition animations
     * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
     */
    public boolean replaceFragment(int viewGroupId, String fragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
        return FragmentHelper.replaceFragment(this, viewGroupId, fragmentClassNameTag, addToBackStack, data, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Find fragment from Fragment Activity by tab name.
     *
     * @param tag Tag of fragment when add or replace by transaction
     * @return Found <code>Fragment</code> or <code>null</code> if not found
     */
    public Fragment findFragmentByTag(final String tag) {
        return FragmentHelper.findFragmentByTag(this, tag);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Clear fragment back stack by position of fragment in Stack
     */
    public void clearBackStackByPosition(int position) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(position).getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Clear fragment back stack by fragment name
     */
    public void returnBackStackByName(String name) {
        AppState.instance().disableFragmentTranstitionAnimation();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        AppState.instance().enableFragmentTranstitionAnimation();
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Clear fragment back stack by position of fragment in Stack
     */
    public int getBackStackEntyCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Clear fragment back stack
     */
    public void clearBackStack() {
        FragmentHelper.clearBackStack(this);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Clear fragment back stack by Iterator
     */
    public void clearBackStackByIterator() {
        FragmentHelper.clearBackStackByIterator(this);
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Clear back stack without any transition animation
     */
    public void clearBackStackWithoutTransitionAnimation() {
        AppState.instance().disableFragmentTranstitionAnimation();
        FragmentHelper.clearBackStackImmediate(this);
        AppState.instance().enableFragmentTranstitionAnimation();
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Pop Fragment Back Stack
     */
    public void popFragmentBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    //----------------------------------------------------------------------------------------------------
    public void popFragmentByTag(String tag) {
        FragmentHelper.popByTag(this, tag);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Open URL by Default Browser from Activity
     */
    public void openUrlByDefaultBrowser(String url) {
        if (url == null || url.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Can not open null or empty URL", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } else {
            Toast.makeText(getApplicationContext(), "Can not open null or empty URL", Toast.LENGTH_SHORT).show();
        }
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Show waiting pop up
     */
    public void showWaitingPopUp() {
//        if (mWaitingPopUp == null)
//            mWaitingPopUp = new SimpleWaitingPopUp(this, null);
//        mWaitingPopUp.show();
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Hide waiting pop up
     */
    public void hideWaitingPopUp() {
//        if (mWaitingPopUp != null) {
//            mWaitingPopUp.dismiss();
//        }
    }
    //----------------------------------------------------------------------------------------------------


    public void showErrorPopup() {
//        mErrorPopup = new ErrorPopUp(this, null);
//        mErrorPopup.show();
    }


    public void showErrorPopup(String error_content) {
//        if (mErrorPopup != null) {
//            mErrorPopup.dismiss();
//            mErrorPopup = null;
//        }
//        mErrorPopup = new ErrorPopUp(this, null, error_content);
//        mErrorPopup.show();
    }

    public void hideErrorPopup() {
//        if (mErrorPopup != null) {
//            mErrorPopup.dismiss();
//        }
    }

    /**
     * get the current time in a string with a separate char
     */
    protected String getCurrentTime(String separate) {
        String result = "";
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        result += String.valueOf(calendar.get(Calendar.YEAR)) + separate;
        result += String.valueOf(calendar.get(Calendar.MONTH)) + separate;
        result += String.valueOf(calendar.get(Calendar.DATE)) + separate;
        result += String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + separate;
        result += String.valueOf(calendar.get(Calendar.MINUTE)) + separate;
        result += String.valueOf(calendar.get(Calendar.MILLISECOND));
        return result;
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        if(kProgressHUDloading==null){
            kProgressHUDloading=KProgressHUD.create(context)
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
    public void openDialogFragment(DialogFragment dialogFragment) {
        android.app.FragmentManager mFragmentManager = getFragmentManager();
        android.app.Fragment prev = mFragmentManager.findFragmentByTag(dialogFragment.getClass().getName());
        if (prev == null) {
            dialogFragment.show(mFragmentManager, dialogFragment.getClass().getName());
        }
    }
    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}
