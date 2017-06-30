package jp.relo.cluboff.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.MessageFormat;

import framework.phvtFragment.BaseFragment;
import framework.phvtUtils.AppLog;
import jp.relo.cluboff.R;
import jp.relo.cluboff.util.Constant;
import jp.relo.cluboff.util.Utils;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by HuyTran on 3/21/17.
 */

public class CouponAreaFragment extends BaseFragment {
    private Subscription locationUpdatesObservable;
    ReactiveLocationProvider locationProvider;
    private final static int REQUEST_CHECK_SETTINGS = 0;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locationProvider = new ReactiveLocationProvider(getActivity());
        if (!checkPermissions()) {
            requestPermission();
        }else{
            requestLocation4Webview();
        }
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

    private void requestLocation4Webview(){
        if(!Utils.isGooglePlayServicesAvailable(getActivity())){
            return;
        }

        final LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100);//interval 0 seconds

        locationUpdatesObservable = locationProvider.checkLocationSettings(
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(request)
                        .setAlwaysShow(true)
                        .build()
        )
                .doOnNext(new Action1<LocationSettingsResult>() {
                    @Override
                    public void call(LocationSettingsResult locationSettingsResult) {
                        Status status = locationSettingsResult.getStatus();
                        if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            try {
                                status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException th) {
                                AppLog.log("Error opening settings activity: "+ th);
                            }
                        }
                    }
                })
                .flatMap(new Func1<LocationSettingsResult, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(LocationSettingsResult locationSettingsResult) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        }
                        return locationProvider.getUpdatedLocation(request);
                    }
                }).subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        String url = MessageFormat.format("https://www.google.com/maps/@{0},{1},18z?nogmmr=1",location.getLatitude(),location.getLongitude());
                        Bundle bundle = getArguments();
                        bundle.putString(Constant.KEY_LOGIN_URL,url);
                        addChildFragment(R.id.frContainerWebview, WebViewFragment.class.getName(),true,bundle,null);
                    }
                });

        // addChildFragment(R.id.frContainerWebview, WebViewFragment.class.getName(),false,getArguments(),null);
    }

    private boolean checkPermissions() {
        int findLoca = ContextCompat.checkSelfPermission(getActivity(), permissions[0]);
        return findLoca == PackageManager.PERMISSION_GRANTED;

    }
    private void requestPermission() {
        requestPermissions(permissions, MULTIPLE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean location = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (location) {
                        Toast.makeText(getActivity(), "Permission is Accepted", Toast.LENGTH_SHORT).show();
                        requestLocation4Webview();
                    }else{
                        Toast.makeText(getActivity(), "Please accept permission access to get location", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please accept permission access", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(locationUpdatesObservable!=null){
            locationUpdatesObservable.unsubscribe();
        }
    }

}
