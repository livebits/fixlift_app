package com.novintech.elevator.features.main.damages;

import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.features.base.MvpView;

import java.util.List;

public interface DamageView extends MvpView {

    void showProgress(boolean show);

    void showError(String error);

    void showMessage(String message);

    void showDamagesList(List<Damage> damages);
}
