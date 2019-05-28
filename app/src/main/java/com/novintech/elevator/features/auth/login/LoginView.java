package com.novintech.elevator.features.auth.login;

import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.MvpView;

public interface LoginView extends MvpView {

    void showProgress(boolean show);

    void showError(String error);

    void showVerifyPage(User user);
}
