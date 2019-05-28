package com.novintech.elevator.features.auth.verify;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.features.auth.AuthFragmentsCallback;
import com.novintech.elevator.features.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class VerifyFragment extends BaseFragment implements VerifyView {

    VerifyPresenter presenter;

    @BindView(R.id.txt_mobile_number)
    TextView mobile;

    @BindView(R.id.edt_verify_code)
    EditText verifyCode;

    @BindView(R.id.btn_verify)
    Button verifyButton;

    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;

    public static VerifyFragment newInstance() {
        return new VerifyFragment();
    }

    public static String TAG = "VerifyFragment";

    private AuthFragmentsCallback callback;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (AuthFragmentsCallback) activity;
        presenter = new VerifyPresenter(ElevatorApplication.dataManager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        String phone_number = PrefUtil.getString(getActivity(), "PHONE_NUMBER");
        mobile.setText(phone_number);
    }

    @OnClick(R.id.btn_verify)
    public void onVerifyClicked() {

        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        String code = verifyCode.getText().toString();

        //get fcm token
        FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    String token = "";
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                    }

                    // Get new Instance ID token
                    token = task.getResult().getToken();
                    presenter.verify(code, token);
                }
            });

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_verify;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    protected void attachView() {
        presenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {

    }

    @Override
    public void showProgress(boolean show){
        verifyButton.setEnabled(!show);
        circularProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showError(String error){

        callback.showMessage(error);
    }

    @Override
    public void sendCode(String code, String fcmToken) {
        presenter.verify(code, fcmToken);
    }

    @Override
    public void showMainActivity() {

        callback.showMainActivity();
    }

    @Override
    public void showServiceMainActivity() {

        callback.showServiceMainActivity();
    }
}
