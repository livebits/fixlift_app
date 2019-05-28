package com.novintech.elevator.features.main.newDamage;

import com.novintech.elevator.features.base.MvpView;

public interface NewDamageView extends MvpView {

    void showProgress(boolean show);

    void showError(String error);

    void showMessage(String message);

    void showDamagesList();

    void dismiss();
}
