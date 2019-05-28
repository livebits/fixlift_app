package com.novintech.elevator.features.auth.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.auth.AuthFragmentsCallback;
import com.novintech.elevator.features.base.BaseFragment;


import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class LoginFragment extends BaseFragment implements LoginView{

    LoginPresenter loginPresenter;

    @BindView(R.id.edt_phone_number)
    EditText phoneNumber;

    @BindView(R.id.btn_login)
    Button loginButton;

    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public static String TAG = "PaymentsFragment";

    private AuthFragmentsCallback callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (AuthFragmentsCallback) activity;
        loginPresenter = new LoginPresenter(ElevatorApplication.dataManager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showProgress(true);
        if(ElevatorApplication.Token.equals("")) {
            showProgress(false);
        } else {
            if(ElevatorApplication.AppUser.role.equals("customer")) {
                callback.showMainActivity();
            } else {
                callback.showServiceMainActivity();
            }
        }
    }

    @OnClick(R.id.btn_login)
    public void onLoginClicked() {

        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        String phoneNum = phoneNumber.getText().toString();

        loginPresenter.login(phoneNum);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    protected void attachView() {
        loginPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {

    }

    @Override
    public void showProgress(boolean show){
        loginButton.setEnabled(!show);
        circularProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showError(String error){

        callback.showMessage(error);
    }

    @Override
    public void showVerifyPage(User user){
        PrefUtil.putString(this.getActivity(), "PHONE_NUMBER", phoneNumber.getText().toString());

        callback.showVerify();
    }
}
