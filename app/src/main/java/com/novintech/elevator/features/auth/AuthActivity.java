package com.novintech.elevator.features.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.novintech.elevator.Constants;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.auth.login.LoginFragment;
import com.novintech.elevator.features.auth.verify.VerifyFragment;
import com.novintech.elevator.features.base.BaseActivity;
import com.novintech.elevator.features.main.MainActivity;
import com.novintech.elevator.features.serviceMain.ServiceMainActivity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;

public class AuthActivity extends BaseActivity implements AuthFragmentsCallback {

    @BindView(R.id.auth_activity)
    LinearLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_activity, LoginFragment.newInstance()).commit();

        ElevatorApplication.snackBarView = mainView;

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_auth;
    }

    @Override
    protected void attachView() {

    }

    @Override
    protected void detachPresenter() {

    }

    public static Intent getStartIntent(Context context, User user) {
        Intent intent = new Intent(context, VerifyFragment.class);
        String phoneNumber = user.mobile;
        intent.putExtra(Constants.PHONE_NUMBER, phoneNumber);
        return intent;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showVerify() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(VerifyFragment.TAG)
                .setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left,
                        R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
                .replace(R.id.auth_activity, VerifyFragment.newInstance(), VerifyFragment.TAG)
                .commit();
    }

    @Override
    public void showLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(LoginFragment.TAG)
                .setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
                .replace(R.id.auth_activity, LoginFragment.newInstance(), LoginFragment.TAG)
                .commit();
    }

    @Override
    public void showMainActivity() {
        finishAffinity();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void showServiceMainActivity() {
        finishAffinity();
        startActivity(new Intent(this, ServiceMainActivity.class));
    }

    @Override
    public void showMessage(String message) {

        Snackbar snackbar = Snackbar.make(mainView, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            sbView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        snackbar.show();
    }

    @Override
    public void showInternetMessage(String message) {

        Snackbar snackbar = Snackbar.make(mainView, message, Snackbar.LENGTH_LONG)
                .setAction("تنظیمات",new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            sbView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        snackbar.show();
    }

}
