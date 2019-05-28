package com.novintech.elevator.features.auth.verify;

import com.novintech.elevator.features.base.MvpView;

public interface VerifyView extends MvpView {

    void showProgress(boolean show);

    void showError(String error);

    void sendCode(String code, String fcmToken);

    void showMainActivity();

    void showServiceMainActivity();
}
