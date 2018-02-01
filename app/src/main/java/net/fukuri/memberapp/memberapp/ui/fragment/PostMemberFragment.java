package net.fukuri.memberapp.memberapp.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import framework.phvtUtils.AppLog;
import net.fukuri.memberapp.memberapp.BuildConfig;
import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.ReloApp;
import net.fukuri.memberapp.memberapp.api.ApiClientJP;
import net.fukuri.memberapp.memberapp.api.ApiInterface;
import net.fukuri.memberapp.memberapp.model.MessageEvent;
import net.fukuri.memberapp.memberapp.ui.BaseFragmentToolbarBottombar;
import net.fukuri.memberapp.memberapp.ui.webview.MyWebViewClient;
import net.fukuri.memberapp.memberapp.util.Constant;
import net.fukuri.memberapp.memberapp.util.ImageUtils;
import net.fukuri.memberapp.memberapp.util.LoginSharedPreference;
import net.fukuri.memberapp.memberapp.util.Utils;
import net.fukuri.memberapp.memberapp.util.ase.EvenBusLoadWebMembersite;
import net.fukuri.memberapp.memberapp.views.MyWebview;

import framework.phvtUtils.StringUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static framework.phvtUtils.RealPathUtil.getDataColumn;

/**
 * Created by tqk666 on 12/28/17.
 */

public class PostMemberFragment extends BaseFragmentToolbarBottombar {
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private static final String UTF8 = "UTF-8";
    private static final String TYPE_IMAGE = "image/*";
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_FROM_JS = 2;

    private Uri m_uri;
    private static final int REQUEST_CHOOSER = 1000;

    private MyWebview mWebView;
    private int checkWebview;
    private FrameLayout fragmentContainer;
    private ProgressBar horizontalProgress;
    Handler handler;
    public static final int LOAD_URL_WEB =1;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static PostMemberFragment newInstance(String key, String url, int keyCheckWebview){
        PostMemberFragment memberFragment = new PostMemberFragment();
        Bundle bundle = new Bundle();
        bundle.putString(key,url);
        bundle.putInt(Constant.KEY_CHECK_WEBVIEW, keyCheckWebview);
        memberFragment.setArguments(bundle);
        return memberFragment;
    }

    @Override
    public void onViewCreated(View view, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ReloApp)getActivity().getApplication()).trackingAnalytics(Constant.GA_MEMBER_SCREEN);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK&&event.getAction() == KeyEvent.ACTION_UP){
                    EventBus.getDefault().post(new MessageEvent(Constant.TOP_COUPON));
                    return true;

                }
                return false;
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==LOAD_URL_WEB) {
                    if (!checkPermissions()) {
                        requestPermission();
                    }else{
                        loadGetUrl();
                    }
                }
                return false;
            }
        });

        checkWebview = getArguments().getInt(Constant.KEY_CHECK_WEBVIEW, Constant.MEMBER_COUPON);
        mWebView = (MyWebview) view.findViewById(R.id.wvCoupon);
        horizontalProgress = (ProgressBar) view.findViewById(R.id.horizontalProgress);
        fragmentContainer = (FrameLayout)getActivity().findViewById(R.id.container_member_fragment);
        setupWebView();
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void setupToolbar() {
        tvMenuTitle.setText(R.string.member_site_title);
        tvMenuSubTitle.setText(R.string.title_member);
        ivMenuRight.setVisibility(View.VISIBLE);
        ivMenuRight.setImageResource(R.drawable.icon_close);
        ivMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
                fragmentContainer.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void setupBottombar() {
        switch (checkWebview){
            case Constant.MEMBER_COUPON:
                lnBottom.setVisibility(View.VISIBLE);
                imvBackBottomBar.setVisibility(View.VISIBLE);
                imvForwardBottomBar.setVisibility(View.VISIBLE);


                //Test
                imvBrowserBottomBar.setVisibility(View.VISIBLE);
                llBrowser.setEnabled(true);

                imvReloadBottomBar.setVisibility(View.VISIBLE);
                break;
        }
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goBack();
                imvBackBottomBar.setEnabled(false);
                llBack.setEnabled(false);
            }
        });
        llForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goForward();
                imvForwardBottomBar.setEnabled(false);
                llForward.setEnabled(false);
            }
        });
        llBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
                if(loginSharedPreference!=null){
                    try {
                        Utils.showDialogBrowser(getActivity(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent internetIntent = new Intent(Intent.ACTION_VIEW);
                                Uri uri = Uri.parse(Constant.URL_MEMBER_BROWSER)
                                        .buildUpon()
                                        .appendQueryParameter("APPU", loginSharedPreference.getKEY_APPU())
                                        .appendQueryParameter("APPP", loginSharedPreference.getKEY_APPP())
                                        .build();
                                internetIntent.setData(uri);
                                getActivity().startActivity(internetIntent);
//                                dismiss();
                                fragmentContainer.setVisibility(View.GONE);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        llReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl( "javascript:window.location.reload( true )" );
            }
        });

        imvBackBottomBar.setEnabled(mWebView.canGoBack());
        imvForwardBottomBar.setEnabled(mWebView.canGoForward());
        llBack.setEnabled(mWebView.canGoBack());
        llForward.setEnabled(mWebView.canGoForward());

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void getMandatoryViews(View root, Bundle savedInstanceState) {

    }

    @Override
    protected void registerEventHandlers() {

    }

    private boolean checkPermissions() {
        int findLoca = ContextCompat.checkSelfPermission(getActivity(), permissions[0]);
        int writeStorage = ContextCompat.checkSelfPermission(getActivity(), permissions[1]);
        return (findLoca == PackageManager.PERMISSION_GRANTED&& writeStorage == PackageManager.PERMISSION_GRANTED);

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
                    boolean writeSto = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (location&&writeSto) {
                        Toast.makeText(getActivity(), R.string.premission_accepted, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), R.string.premissionaccepted_no_accepted, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.premission_error, Toast.LENGTH_SHORT).show();
                }
                loadGetUrl();
                break;

        }
    }
    String pdfURL;
    private void setupWebView() {
        mWebView.addJavascriptInterface(new WebViewJavaScriptInterface(getActivity()), "Native");
        mWebView.setWebViewClient(new MyWebViewClient(getActivity()) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                AppLog.log("PAGE ON START URL  = "+url);

                if(url.endsWith(".pdf") && !url.startsWith(Constant.URL_READ_FDF) && !url.startsWith(Constant.URLS_READ_FDF)){
                    pdfURL = Constant.URL_READ_FDF+url;
                }else{
                    pdfURL ="";

                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                AppLog.log_url(view.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(isVisible()){
                    imvBackBottomBar.setEnabled(mWebView.canGoBack());
                    imvForwardBottomBar.setEnabled(mWebView.canGoForward());
                    llBack.setEnabled(mWebView.canGoBack());
                    llForward.setEnabled(mWebView.canGoForward());


                    AppLog.log("Page on FINISH URL  = "+url);
                    if(!StringUtil.isEmpty(pdfURL)){
                        mWebView.loadUrl(pdfURL);
                        pdfURL = "";
                    }
                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
//                    dismiss();
                    fragmentContainer.setVisibility(View.GONE);
                }
                return false;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    horizontalProgress.setVisibility(View.GONE);
                } else {
                    horizontalProgress.setVisibility(View.VISIBLE);
                    horizontalProgress.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(Constant.TITLE_LOGOUT.equalsIgnoreCase(title)){
                    Utils.forceLogout(getActivity());
                }
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(view);
                resultMsg.sendToTarget();
                return true;
            }
            // For Android 4.4.3+
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                if(mUploadMessage != null){
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = uploadFile;


                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType(TYPE_IMAGE);
                }

                //カメラの起動Intentの用意
                String photoName = System.currentTimeMillis() + ".jpg";
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.TITLE, photoName);
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                m_uri = getActivity().getContentResolver()
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, m_uri);

                Intent intentChooser = Intent.createChooser(intentCamera, getString(R.string.title_chooser_file));
                intentChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {intent});
                startActivityForResult(intentChooser, INPUT_FILE_REQUEST_CODE);
            }

            // For Android 5.0+
            @Override public boolean onShowFileChooser(WebView webView,
                                                       ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                // Set up the intent to get an existing image
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType(TYPE_IMAGE);
                }


                //カメラの起動Intentの用意
                String photoName = System.currentTimeMillis() + ".jpg";
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.TITLE, photoName);
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                m_uri = getActivity().getContentResolver()
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, m_uri);

                Intent intentChooser = Intent.createChooser(intentCamera, getString(R.string.title_chooser_file));
                intentChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {intent});
                startActivityForResult(intentChooser, INPUT_FILE_REQUEST_CODE);

                return true;
            }

        });

        if (!checkPermissions()) {
            requestPermission();
        }else{
            loadGetUrl();
        }
    }

    private void loadGetUrl(){
        String  userID = "";
        String  pass = "";
        ApiInterface apiInterface = ApiClientJP.getClient().create(ApiInterface.class);
        LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
        userID = loginSharedPreference.getUserName();
        pass = loginSharedPreference.getPass();
        apiInterface.memberAuthHTML(userID, pass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response!=null && response.body()!=null){
                        Document document = Jsoup.parse(response.body().string());
                        Element divChildren = document.select("html").first();
                        for (int i = 0; i < divChildren.childNodes().size(); i++) {
                            Node child = divChildren.childNode(i);
                            if (child.nodeName().equals("#comment")) {
                                String valueAuth = child.toString();
                                int valueHandleLogin = BuildConfig.DEBUG? 0:1;
                                if(Utils.parserInt(valueAuth.substring(valueAuth.indexOf("<STS>")+5,valueAuth.indexOf("</STS>")))==valueHandleLogin){
                                    LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
                                    loginSharedPreference.setKEY_APPU(valueAuth.substring(valueAuth.indexOf("<APPU>")+6,valueAuth.indexOf("</APPU>")));
                                    loginSharedPreference.setKEY_APPP(valueAuth.substring(valueAuth.indexOf("<APPP>")+6,valueAuth.indexOf("</APPP>")));
                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    loadUrlWeb();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppLog.log("Err: "+t.toString());
                loadUrlWeb();
            }
        });

    }
    public void loadUrlWeb(){
        LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
        StringBuffer url=new StringBuffer(Constant.URL_MEMBER_BROWSER);
        url.append("?APPU="+ URLEncoder.encode(loginSharedPreference.getKEY_APPU()));
        url.append("&APPP="+URLEncoder.encode(loginSharedPreference.getKEY_APPP()));
        mWebView.loadUrl(url.toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        mWebView.stopLoading();
    }


    @Override
    public void onPause() {
        super.onPause();
        /*if (fragmentContainer.getVisibility() == View.VISIBLE){
            fragmentContainer.setVisibility(View.GONE);
            //((MainTabActivity)getActivity()).resetCurrentTab();
        }*/
    }

    @Subscribe
    public void onEvent(EvenBusLoadWebMembersite event) {
        handler.sendEmptyMessage(LOAD_URL_WEB);
        AppLog.log("Web loaded");
    }
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == INPUT_FILE_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mFilePathCallback == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri[] results = null;

                // Check that the response is a good one
                if (resultCode == RESULT_OK) {
                    if(data==null || data.getData()==null){
                        if (m_uri != null) {
                            results = new Uri[] { m_uri };
                        }
                    }else{
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[] { Uri.parse(dataString) };
                        }
                    }

                }

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
            }
            else {
                if (mUploadMessage == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }

                Uri result = null;

                if (resultCode == RESULT_OK) {
                    if(data==null || data.getData()==null){
                        if (m_uri != null) {
                            result =  m_uri ;
                        }
                    }else{
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            result = data.getData();
                        }
                    }
                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }else if(requestCode == REQUEST_CODE_FROM_JS){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // setting file name
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//      String imgPath = cursor.getString(columnIndex);
            String imgPath = ImageUtils.getPath(getActivity(), selectedImage);
            cursor.close();
//      String fileNameSegments[] = imgPath.split("/");
//      String fileName = fileNameSegments[fileNameSegments.length - 1];

            // encode image to string
            BitmapFactory.Options options = null;
            options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] b = stream.toByteArray();
            String encodedString = Base64.encodeToString(b, Base64.DEFAULT);
//      Map<String, String> params = new HashMap<String, String>();
//      params.put("filename", fileName);
//      params.put("image", encodedString);
            mWebView.loadUrl("javascript:chooseImgResult(" + encodedString + ")");
        }else{
            super.onActivityResult(requestCode,resultCode, data);
        }
    }


    /**
     * JavaScript Interface. Web code can access methods in here
     * (as long as they have the @JavascriptInterface annotation)
     */
    public class WebViewJavaScriptInterface {

        private Context context;

        /**
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void onShowFileChooser() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType(TYPE_IMAGE);
            startActivityForResult(intent, REQUEST_CODE_FROM_JS);
        }
    }

}
