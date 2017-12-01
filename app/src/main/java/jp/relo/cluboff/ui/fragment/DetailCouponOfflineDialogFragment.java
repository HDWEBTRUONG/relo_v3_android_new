package jp.relo.cluboff.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.relo.cluboff.R;
import jp.relo.cluboff.database.ConstansDB;
import jp.relo.cluboff.database.MyDatabaseHelper;
import jp.relo.cluboff.model.CatagoryDTO;
import jp.relo.cluboff.model.CouponDTO;
import jp.relo.cluboff.ui.BaseDialogFragmentToolbar;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by tonkhanh on 11/30/17.
 */

public class DetailCouponOfflineDialogFragment extends BaseDialogFragmentToolbar{
    TextView tvDate, tvCouponName, tvBenefit, tvBenefitNote;
    MyDatabaseHelper myDatabaseHelper;

    public  String couponID="";

    public static final String BUNDLE_DATA ="BUNDLE_DATA";

    public static DetailCouponOfflineDialogFragment newInstance(String couponID) {

        Bundle args = new Bundle();
        args.putString(BUNDLE_DATA, couponID);
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public  void loadData(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String currentDateandTime = sdf.format(new Date());
        tvDate.setText(MessageFormat.format(getString(R.string.detail_offline_date),currentDateandTime));

        myDatabaseHelper.getCouponDetail(couponID).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CouponDTO>() {
                    @Override
                    public void call(CouponDTO couponDTO) {
                        tvCouponName.setText(MessageFormat.format(getString(R.string.detail_offline_coupon_name_template),
                                couponDTO.getCoupon_name(), couponDTO.getCoupon_name_en()));
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            tvBenefit.setText(Html.fromHtml(couponDTO.getBenefit(),Html.FROM_HTML_MODE_LEGACY));
                            tvBenefitNote.setText(Html.fromHtml(couponDTO.getBenefit_notes(),Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            tvBenefit.setText(Html.fromHtml(couponDTO.getBenefit()));
                            tvBenefitNote.setText(Html.fromHtml(couponDTO.getBenefit_notes()));
                        }
                    }
                });
    }
}
