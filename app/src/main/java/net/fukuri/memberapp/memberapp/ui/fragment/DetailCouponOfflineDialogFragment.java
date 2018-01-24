package net.fukuri.memberapp.memberapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import framework.phvtUtils.AppLog;
import framework.phvtUtils.StringUtil;
import net.fukuri.memberapp.memberapp.R;
import net.fukuri.memberapp.memberapp.database.MyDatabaseHelper;
import net.fukuri.memberapp.memberapp.model.CouponDTO;
import net.fukuri.memberapp.memberapp.ui.BaseDialogFragmentToolbar;
import net.fukuri.memberapp.memberapp.util.LoginSharedPreference;
import net.fukuri.memberapp.memberapp.util.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tonkhanh on 11/30/17.
 */

public class DetailCouponOfflineDialogFragment extends BaseDialogFragmentToolbar{
    TextView tvDate, tvCouponName, tvBenefit, tvBenefitNote, tvNote, tvCopyRight,tvUserLogin;
    MyDatabaseHelper myDatabaseHelper;

    public  String couponID="";
    public  String areaID="";
    public  String userID="";

    public static final String BUNDLE_DATA ="BUNDLE_DATA";
    public static final String BUNDLE_DATA_AREA ="BUNDLE_DATA_AREA";

    public static DetailCouponOfflineDialogFragment newInstance(String couponID, String area) {

        Bundle args = new Bundle();
        args.putString(BUNDLE_DATA, couponID);
        args.putString(BUNDLE_DATA_AREA, area);
        DetailCouponOfflineDialogFragment fragment = new DetailCouponOfflineDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupActionBar() {
        tvMenuTitle.setText("Detail");
        tvMenuSubTitle.setText("Detail Coupon");
        ivMenuRight.setVisibility(View.VISIBLE);
        ivMenuRight.setImageResource(R.drawable.icon_close);
        ivMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void init(View view) {
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvCouponName = (TextView) view.findViewById(R.id.tvCouponName);
        tvBenefit = (TextView) view.findViewById(R.id.tvBenefit);
        tvBenefitNote = (TextView) view.findViewById(R.id.tvBenefitNote);
        tvNote = (TextView) view.findViewById(R.id.tvNote);
        tvCopyRight = (TextView) view.findViewById(R.id.tvCopyRight);
        tvUserLogin = (TextView) view.findViewById(R.id.tvUserLogin);
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.detail_coupon_offline_layout;
    }

    @Override
    public void bindView(View view) {

    }

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myDatabaseHelper= MyDatabaseHelper.getInstance(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            couponID = bundle.getString(BUNDLE_DATA);
            areaID = bundle.getString(BUNDLE_DATA_AREA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tvCopyRight.setText(MessageFormat.format(getString(R.string.detail_offline_copyright_template), Utils.getYear()));
        tvUserLogin.setText(LoginSharedPreference.getInstance(getActivity()).getUserName()+"");
        loadData();
        writeLog();
    }

    private void writeLog() {
        if(StringUtil.isEmpty(userID)){
            LoginSharedPreference loginSharedPreference = LoginSharedPreference.getInstance(getActivity());
            userID = loginSharedPreference.getUserName();
        }
        apiInterfaceLog.writeLog(couponID,userID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    AppLog.log(""+response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppLog.log(""+t.getMessage());
            }
        });
    }

    public  void loadData(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDateandTime = sdf.format(new Date());
        tvDate.setText(MessageFormat.format(getString(R.string.detail_offline_date),currentDateandTime));
        CouponDTO couponDTO = myDatabaseHelper.getCouponDetails(couponID, areaID);
        tvCouponName.setText(MessageFormat.format(getString(R.string.detail_offline_coupon_name_template),
                couponDTO.getCoupon_name(), couponDTO.getCoupon_name_en()));
        if(couponDTO.getBenefit()!=null){
            AppLog.log("getBenefit: "+couponDTO.getBenefit());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvBenefit.setText(Html.fromHtml(Utils.addTagRedBenefit(couponDTO.getBenefit()).replaceAll("\\\\n", "<br>"),Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvBenefit.setText(Html.fromHtml(Utils.addTagRedBenefit(couponDTO.getBenefit()).replaceAll("\\\\n", "<br>")));
            }
        }
        if(couponDTO.getBenefit_notes()!=null){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvBenefitNote.setText(Html.fromHtml(Utils.addTagRedBenefit(couponDTO.getBenefit_notes()).replaceAll("\\\\n", "<br>"),Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvBenefitNote.setText(Html.fromHtml(Utils.addTagRedBenefit(couponDTO.getBenefit_notes()).replaceAll("\\\\n", "<br>")));
            }
        }
        if(StringUtil.isEmpty(couponDTO.getBenefit_notes())){
            tvNote.setVisibility(View.GONE);
        }else{
            tvNote.setVisibility(View.VISIBLE);
        }
    }
}
