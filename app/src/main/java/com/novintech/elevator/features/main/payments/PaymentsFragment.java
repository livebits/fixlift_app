package com.novintech.elevator.features.main.payments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.FactorPayment;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BaseFragment;
import com.novintech.elevator.features.main.MainFragmentsCallback;
import com.novintech.elevator.features.main.damages.DamageAdapter;
import com.novintech.elevator.util.PersianCalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class PaymentsFragment extends BaseFragment implements PaymentsView {

    PaymentsPresenter paymentsPresenter;
    List<Damage> factors;

    @BindView(R.id.chip_no_item)
    Chip noItem;

    @BindView(R.id.balance)
    TextView tv_balance;

    @BindView(R.id.balance_date)
    TextView balanceDate;

    @BindView(R.id.ic_refresh)
    ImageButton refresh;

    @BindView(R.id.recycler_payments_list)
    RecyclerView recyclerPaymentsList;

    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;

    public static PaymentsFragment newInstance() {
        return new PaymentsFragment();
    }

    public static String TAG = "PaymentsFragment";

    PaymentAdapter adapter;

    private MainFragmentsCallback callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MainFragmentsCallback) activity;
        paymentsPresenter = new PaymentsPresenter(ElevatorApplication.dataManager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        paymentsPresenter.getPayments();
    }

    @OnClick(R.id.ic_refresh)
    public void refreshClicked() {

        Animation rotate = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_rotate);

        refresh.startAnimation(rotate);

        paymentsPresenter.getPayments();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_transaction;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    protected void attachView() {
        paymentsPresenter.attachView(this);
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

        callback.showMessage(error);
    }

    @Override
    public void showMessage(String message) {
        callback.showMessage(message);
    }

    @Override
    public void showTransactionsList(List<Damage> factors) {

        this.factors = factors;

        tv_balance.setText(paymentsPresenter.calculateBalance(factors) + "");

        Date c = Calendar.getInstance().getTime();

        balanceDate.setText(PersianCalendar.getPersianDate(c));

        if(factors.size() == 0) {
            noItem.setVisibility(View.VISIBLE);
            recyclerPaymentsList.setVisibility(View.INVISIBLE);
        } else {
            noItem.setVisibility(View.GONE);
            recyclerPaymentsList.setVisibility(View.VISIBLE);

            adapter = new PaymentAdapter(getActivity(), this, factors);
            recyclerPaymentsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerPaymentsList.setItemAnimator(new DefaultItemAnimator());
            recyclerPaymentsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }



    public void showFactorDetail(int position){

        ElevatorApplication.damage = factors.get(position);
        callback.showDamageDetail();
    }

}
