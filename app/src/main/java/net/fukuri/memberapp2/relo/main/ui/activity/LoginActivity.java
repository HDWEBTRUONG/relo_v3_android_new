package net.fukuri.memberapp2.relo.main.ui.activity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.fukuri.memberapp2.relo.R;
import net.fukuri.memberapp2.relo.framework.phvtActivity.BaseActivity;
import net.fukuri.memberapp2.relo.main.ui.fragment.CouponFragment;
import net.fukuri.memberapp2.relo.main.util.Utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by quynguyen on 3/22/17.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title_toolbar)
    TextView mToolbarTilte;

    /*@BindView(R.id.text_view_left_toolbar)
    TextView mLeftToolbar;

    @BindView(R.id.text_view_right_toolbar)
    TextView mRightToolbar;*/

    @BindView(R.id.edit_login_username)
    EditText editUsername;

    @BindView(R.id.edit_login_password)
    EditText editPassword;

    @BindView(R.id.txt_show_error)
    TextView txtShowError;

    @BindView(R.id.bt_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show toolbar
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewToolbar = inflater.inflate(R.layout.layout_toolbar_default, null);
        mToolbar = ButterKnife.findById(viewToolbar, R.id.toolbar);
        mToolbarTilte = ButterKnife.findById(viewToolbar, R.id.title_toolbar);
        mToolbar.setBackgroundResource(R.color.colorMineShaft);
        mToolbarTilte.setVisibility(View.VISIBLE);
        mToolbarTilte.setText(getString(R.string.txt_title_login));
        mToolbarTilte.setTextColor(R.color.white);

        View view = inflater.inflate(R.layout.activity_login, null);
        editUsername = ButterKnife.findById(view, R.id.edit_login_username);
        editPassword = ButterKnife.findById(view, R.id.edit_login_password);
        btnLogin = ButterKnife.findById(view, R.id.bt_login);
        txtShowError = ButterKnife.findById(view, R.id.txt_show_error);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    public void clickLogin(View view){
        boolean isNetworkAvailable = Utils.isNetworkAvailable(this);
        if(isNetworkAvailable) {
            // Get Data From UI
            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();
            try {
                if ((username != null && !username.isEmpty()) && (password != null && !password.isEmpty())) {
                    if("admin".equals(username) && "123456".equals(password)) {
                        Intent mainActivity = new Intent(this, MainActivity.class);
                        startActivity(mainActivity);
                    }else{
                        txtShowError.setText(getResources().getString(R.string.error_login_wrong_id_password));
                        txtShowError.setVisibility(View.VISIBLE);
                    }
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle(getResources().getString(R.string.popup_title_login));
                    alertDialog.setMessage(getResources().getString(R.string.error_blank_id_password));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getResources().getString(R.string.popup_title_login));
            alertDialog.setMessage(getResources().getString(R.string.error_connect_network));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void clickForget(View view){
        String url = "http://google.com";
    }

    public void clickLinkNotLogin(View view){
        String url = "http://google.com";
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
