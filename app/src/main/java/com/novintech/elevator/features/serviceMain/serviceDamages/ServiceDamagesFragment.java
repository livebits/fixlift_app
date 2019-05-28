package com.novintech.elevator.features.serviceMain.serviceDamages;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BaseFragment;
import com.novintech.elevator.features.serviceMain.ServiceMainFragmentsCallback;
import com.novintech.elevator.util.RecyclerItemClickListener;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class ServiceDamagesFragment extends BaseFragment implements ServiceDamageView {

    ServiceDamagesPresenter damagesPresenter;

    @BindView(R.id.recycler_service_damages_list1)
    RecyclerView recyclerViewDamages;

    @BindView(R.id.btn_filter)
    ImageButton btnFilter;

    @BindView(R.id.chip_no_item)
    Chip noItem;

    @BindView(R.id.ic_switch)
    SwitchCompat switchCompat;

    @BindView(R.id.building_name)
    Chip chipBuildingName;

    @BindView(R.id.ic_refresh)
    ImageButton btnRefresh;

    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;

    List<Damage> damages;

    Dialog dialog;
    RecyclerView buildingRV;
    CircularProgressBar dialogCircularProgressBar;

    public static ServiceDamagesFragment newInstance() {
        return new ServiceDamagesFragment();
    }

    public static String TAG = "ServiceDamagesFragment";

    ServiceDamageAdapter adapter;

    private ServiceMainFragmentsCallback serviceCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        damagesPresenter = new ServiceDamagesPresenter(ElevatorApplication.dataManager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceCallback = (ServiceMainFragmentsCallback) getActivity();
        chipBuildingName.setVisibility(View.GONE);

        damagesPresenter.getServiceDamages(ElevatorApplication.filter);

        chipBuildingName.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chipBuildingName.setVisibility(View.GONE);
                ElevatorApplication.filter.customerId = "";
                btnFilter.setColorFilter(getResources().getColor(R.color.black));
                damagesPresenter.getServiceDamages(ElevatorApplication.filter);
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ElevatorApplication.filter.showAll = "true";
                }
                else {
                    ElevatorApplication.filter.showAll = "false";
                }
                damagesPresenter.getServiceDamages(ElevatorApplication.filter);
            }
        });

        if(ElevatorApplication.filter != null) {

            if(ElevatorApplication.filter.customerId != "") {
                Gson g = new Gson();
                List<User> buildings = g.fromJson(PrefUtil.getString(ElevatorApplication.ApplicationContext, "buildings")
                        , new TypeToken<List<User>>(){}.getType());

                for (int i = 0; i < buildings.size(); i++) {
                    if(buildings.get(i).id == Integer.parseInt(ElevatorApplication.filter.customerId)) {
                        btnFilter.setColorFilter(getResources().getColor(R.color.colorSecondary));
                        chipBuildingName.setVisibility(View.VISIBLE);
                        chipBuildingName.setText(buildings.get(i).buildingName);
                        break;
                    }
                }
            }

            if(ElevatorApplication.filter.showAll.equals("true")) {
                switchCompat.setChecked(true);
            } else {
                switchCompat.setChecked(false);
            }

        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_service_damages;
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
        serviceCallback.showMessage(error);
    }

    @OnClick(R.id.btn_filter)
    public void onFilterClicked() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_building);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.show();

        //elements
        dialogCircularProgressBar = dialog.findViewById(R.id.progress);
        dialogCircularProgressBar.setVisibility(View.VISIBLE);

        ImageButton dismissBtn = dialog.findViewById(R.id.ic_close);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buildingRV = (RecyclerView) dialog.findViewById(R.id.recycler_view_factor);
        buildingRV.setHasFixedSize(true);
        buildingRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        damagesPresenter.getBuildings();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if (ElevatorApplication.isFilterChanged) {
                    ElevatorApplication.isFilterChanged = false;
                    damagesPresenter.getServiceDamages(ElevatorApplication.filter);
                }
            }
        });
    }

    @Override
    public void showBuilding(List<User> buildings) {
        BuildingsListAdapter rvAdapter = new BuildingsListAdapter(getActivity(), buildings);
        buildingRV.setAdapter(rvAdapter);

        buildingRV.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        ElevatorApplication.filter.customerId = buildings.get(position).id + "";
                        ElevatorApplication.isFilterChanged = true;
                        dialog.dismiss();
                        btnFilter.setColorFilter(getResources().getColor(R.color.colorSecondary));
                        chipBuildingName.setVisibility(View.VISIBLE);
                        chipBuildingName.setText(buildings.get(position).buildingName);
                    }
                })
        );

        dialogCircularProgressBar.setVisibility(View.INVISIBLE);

        rvAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.ic_refresh)
    public void refreshClicked() {

        Animation rotate = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_rotate);

        btnRefresh.startAnimation(rotate);

        damagesPresenter.getServiceDamages(ElevatorApplication.filter);
    }

    @Override
    public void showMessage(String message) {
        serviceCallback.showMessage(message);
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

            adapter = new ServiceDamageAdapter(getActivity(), this, damages);
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

        serviceCallback.showDamageDetail();
    }

}
