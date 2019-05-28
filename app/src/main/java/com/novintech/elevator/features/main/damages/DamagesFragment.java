package com.novintech.elevator.features.main.damages;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.google.android.material.chip.Chip;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.features.base.BaseFragment;
import com.novintech.elevator.features.main.MainFragmentsCallback;
import com.novintech.elevator.util.RecyclerItemClickListener;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class DamagesFragment extends BaseFragment implements DamageView {

    DamagesPresenter damagesPresenter;

    @BindView(R.id.recycler_damages_list)
    RecyclerView recyclerViewDamages;

    @BindView(R.id.chip_no_item)
    Chip noItem;

    @BindView(R.id.ic_refresh)
    ImageButton btnRefresh;

    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;

    List<Damage> damages;

    public static DamagesFragment newInstance() {
        return new DamagesFragment();
    }

    public static String TAG = "ServiceDamagesFragment";

    DamageAdapter adapter;

    private MainFragmentsCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        damagesPresenter = new DamagesPresenter(ElevatorApplication.dataManager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callback = (MainFragmentsCallback) getActivity();

        damagesPresenter.getCustomerDamages();
//        callback.changeColor("damage");
    }



    @Override
    protected int getLayout() {
        return R.layout.activity_damages;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    protected void attachView() {
        damagesPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {

    }

    @Override
    public void showProgress(boolean show) {
        circularProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showError(String error) {
        callback.showMessage(error);
    }

    @OnClick(R.id.ic_refresh)
    public void refreshClicked() {

        Animation rotate = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_rotate);

        btnRefresh.startAnimation(rotate);

        damagesPresenter.getCustomerDamages();
    }

    @Override
    public void showMessage(String message) {
        callback.showMessage(message);
    }

    @Override
    public void showDamagesList(List<Damage> damages) {

        this.damages = damages;

        if(damages.size() == 0) {
            noItem.setVisibility(View.VISIBLE);
            recyclerViewDamages.setVisibility(View.INVISIBLE);
        } else {

            noItem.setVisibility(View.GONE);
            recyclerViewDamages.setVisibility(View.VISIBLE);

            adapter = new DamageAdapter(getActivity(), this, damages);
            recyclerViewDamages.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewDamages.setItemAnimator(new DefaultItemAnimator());
            recyclerViewDamages.setAdapter(adapter);

            recyclerViewDamages.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            showDamageDetail(position);
                        }
                    })
            );

            adapter.notifyDataSetChanged();
        }

    }

    public void showDamageDetail(int position) {

        ElevatorApplication.damage = this.damages.get(position);

        callback.showDamageDetail();
    }

}
