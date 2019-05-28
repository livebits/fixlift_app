package com.novintech.elevator.features.main.damageDetail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.features.base.BaseFragment;
import com.novintech.elevator.features.main.MainActivity;
import com.novintech.elevator.features.main.MainFragmentsCallback;
import com.novintech.elevator.features.serviceMain.ServiceMainActivity;
import com.novintech.elevator.features.serviceMain.ServiceMainFragmentsCallback;
import com.novintech.elevator.util.PersianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class DamageDetailFragment extends BaseFragment implements DamageDetailView {

    DamageDetailPresenter damageDetailPresenter;

    @BindView(R.id.ic_back)
    ImageButton btnBack;

//    @BindView(R.id.btn_rate)
//    FloatingActionButton btnRate;

    @BindView(R.id.chip_address)
    Chip chipAddress;

    @BindView(R.id.txt_damage_date)
    TextView txtDamageDate;

    @BindView(R.id.txt_damage_description)
    TextView txtDamageDescription;

    @BindView(R.id.recycler_view_factor)
    RecyclerView recyclerViewFactor;

    @BindView(R.id.factor_total_price)
    TextView txtFactorTotalPrice;

    @BindView(R.id.txt_report_date)
    TextView txtReportDate;

    @BindView(R.id.txt_report)
    TextView txtReportDescription;

    @BindView(R.id.cardView_factor)
    MaterialCardView layoutFactor;

    @BindView(R.id.cardView_report)
    MaterialCardView layoutReport;

    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;

    public static DamageDetailFragment newInstance() {
        return new DamageDetailFragment();
    }

    public static String TAG = "ServiceDamageDetailFragment";

    FactorAdapter adapter;

    private MainFragmentsCallback callback;
    private ServiceMainFragmentsCallback serviceCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof MainActivity) {
            callback = (MainFragmentsCallback) activity;

        } else if(activity instanceof ServiceMainActivity) {
            serviceCallback = (ServiceMainFragmentsCallback) activity;

        }
        damageDetailPresenter = new DamageDetailPresenter(ElevatorApplication.dataManager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showDamageDetail();

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_damage_datail;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    protected void attachView() {
        damageDetailPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {

    }

//    @OnClick(R.id.btn_rate)
//    public void rateClicked(){
//
//        callback.popStack();
//    }

    @OnClick(R.id.ic_back)
    public void backClicked(){

        callback.popStack();
    }

    @OnClick(R.id.chip_address)
    public void onAddressClicked() {
        Damage damage = ElevatorApplication.damage;

        if(damage.appUser != null
                && damage.appUser.latitude != null
                && !damage.appUser.latitude.equals("")
                && damage.appUser.longitude != null
                && !damage.appUser.longitude.equals("")
                ) {

            String uri = "geo:" + damage.appUser.latitude + "," + damage.appUser.longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void showProgress(boolean show){
        circularProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showError(String error){
        if(callback != null) {
            callback.showMessage(error);

        } else if(serviceCallback != null) {
            serviceCallback.showMessage(error);
        }
    }

    @Override
    public void showMessage(String message) {
        if(callback != null) {
            callback.showMessage(message);

        } else if(serviceCallback != null) {
            serviceCallback.showMessage(message);
        }
    }

    @Override
    public void showDamageDetail() {

        Damage damage =  ElevatorApplication.damage;
        if(ElevatorApplication.handleNotif) {
            ElevatorApplication.handleNotif = false;
            damageDetailPresenter.getDamage(damage.id);
            return;
        }
        Log.e("description", ElevatorApplication.damage.description);

        chipAddress.setText(damage.appUser.address);
        txtDamageDescription.setText(damage.description);

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            Date jalaliDate = df1.parse(damage.createdAt);
            txtDamageDate.setText(PersianCalendar.getPersianDate(jalaliDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        factor
        if(damage.factors != null) {

            layoutFactor.setVisibility(View.VISIBLE);

            adapter = new FactorAdapter(getActivity(), damage.factors.factorItems);
            recyclerViewFactor.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewFactor.setItemAnimator(new DefaultItemAnimator());
            recyclerViewFactor.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            txtFactorTotalPrice.setText(damage.factors.sumPrice + "");
        } else {
            txtFactorTotalPrice.setText("0");
            layoutFactor.setVisibility(View.GONE);
        }

        //report
        if(damage.reports != null) {

            layoutReport.setVisibility(View.VISIBLE);
            txtReportDescription.setText(damage.reports.body);
        } else {
            layoutReport.setVisibility(View.GONE);
        }
    }

}
