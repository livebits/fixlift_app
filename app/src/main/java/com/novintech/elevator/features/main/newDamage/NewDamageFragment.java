package com.novintech.elevator.features.main.newDamage;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.features.base.BaseDialogFragment;
import com.novintech.elevator.features.main.MainFragmentsCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class NewDamageFragment extends BaseDialogFragment implements NewDamageView {

    NewDamagePresenter newDamagePresenter;

    @BindView(R.id.ic_close)
    ImageButton btnClose;

    @BindView(R.id.chip)
    Chip address;

    @BindView(R.id.edt_description)
    EditText edtDescription;

    @BindView(R.id.ic_send)
    ImageButton btnSend;

    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;

    Boolean actionDone = false;

    public static NewDamageFragment newInstance() {
        NewDamageFragment mDialogFragment = new NewDamageFragment();
        mDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return mDialogFragment;
    }

    public static String TAG = "NewDamageFragment";

    private MainFragmentsCallback callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MainFragmentsCallback) activity;
        newDamagePresenter = new NewDamagePresenter(ElevatorApplication.dataManager);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        address.setText(ElevatorApplication.AppUser.address);
    }


    @OnClick(R.id.ic_send)
    public void sendClicked(){
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        newDamagePresenter.sendDamage(edtDescription.getText().toString(), address.getText().toString());
    }

    @OnClick(R.id.ic_close)
    public void backClicked(){

        actionDone = false;
        dismiss();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_new_damage;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    protected void attachView() {
        newDamagePresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {

    }

    @Override
    public void showProgress(boolean show){
        circularProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showError(String error){

        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
//        callback.showMessage(error);
    }

    @Override
    public void showMessage(String message) {
        callback.showMessage(message);
    }

    @Override
    public void showDamagesList() {

        actionDone = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        if(actionDone) {
            callback.showMessage("خرابی با موفقیت ثبت شد.");
            callback.showDamagesList();
        }
        super.onDismiss(dialog);
    }
}
