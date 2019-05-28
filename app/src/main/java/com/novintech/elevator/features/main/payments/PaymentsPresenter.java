package com.novintech.elevator.features.main.payments;

import android.util.Log;

import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.Factor;
import com.novintech.elevator.data.model.response.FactorPayment;
import com.novintech.elevator.data.model.response.Payment;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BasePresenter;
import com.novintech.elevator.util.rx.scheduler.SchedulerUtils;

import java.util.ArrayList;
import java.util.List;


public class PaymentsPresenter extends BasePresenter<PaymentsView> {

    public User user;
    public final DataManager dataManager;

    public PaymentsPresenter(DataManager dataManager) {

        this.dataManager = dataManager;
    }

    @Override
    public void attachView(PaymentsView mvpView) {
        super.attachView(mvpView);
    }

    public void getPayments() {

        checkViewAttached();

        if(!ElevatorApplication.isNetworkAvailable()) {
            getView().showError("ارتباط با اینترنت برقرار نمی باشد");
            return;
        }

        getView().showProgress(true);
        dataManager
                .customerFactorsToPay()
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        damageList -> {

                            getView().showProgress(false);
                            Gson g = new Gson();
                            PrefUtil.putString(ElevatorApplication.ApplicationContext,
                                    "transactions", g.toJson(damageList));
                            getView().showTransactionsList(damageList);
                        },
                        throwable -> {

                            getView().showProgress(false);
                            getView().showError("خطای سرور");
                        });

    }

    public int calculateBalance(List<Damage> factors) {

        int paidSum = 0, notPaidSum =0, balance = 0;
        Damage factor;

        for(int i =0 ; i< factors.size(); i++){

            factor = factors.get(i);

            if(factor.factors.status.equals("paid")){

                paidSum += factor.factors.sumPrice;

            }else if(factor.factors.status.equals("notpaid")){

                notPaidSum += factor.factors.sumPrice;

            }
        }

        balance = notPaidSum - paidSum;
        return balance;
    }
}
