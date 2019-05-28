package com.novintech.elevator.features.main.damageDetail;

import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.features.base.MvpView;

import java.util.List;

public interface DamageDetailView extends MvpView {

    void showProgress(boolean show);

    void showError(String error);

    void showMessage(String message);

    void showDamageDetail();
}
