package com.novintech.elevator.features.main;

import com.novintech.elevator.data.model.response.CompanyInfo;
import com.novintech.elevator.features.base.MvpView;

public interface MainView extends MvpView {

    void showProgress(boolean show);

    void showError(String error);

    void showMessage(String message);

    void showCompanyInfo(CompanyInfo companyInfo);
}
