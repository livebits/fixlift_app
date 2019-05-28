package com.novintech.elevator.features.main.payments;

import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.FactorPayment;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.MvpView;

import java.util.List;

public interface PaymentsView extends MvpView {

    void showProgress(boolean show);

    void showError(String error);

    void showMessage(String message);

    void showTransactionsList(List<Damage> factors);
}
