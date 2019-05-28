package com.novintech.elevator.features.auth;

public interface AuthFragmentsCallback {

    public void showVerify();
    public void showLogin();
    public void showMainActivity();
    public void showServiceMainActivity();
    public void showMessage(String message);
    public void showInternetMessage(String message);
}
