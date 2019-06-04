package com.novintech.elevator.features.serviceMain.serviceDamageDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Checklist;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.Factor;
import com.novintech.elevator.data.model.response.FactorItem;
import com.novintech.elevator.data.model.response.Report;
import com.novintech.elevator.data.model.response.SaveReport;
import com.novintech.elevator.features.base.BaseFragment;
import com.novintech.elevator.features.serviceMain.ServiceMainFragmentsCallback;
import com.novintech.elevator.util.PersianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class ServiceDamageDetailFragment extends BaseFragment implements DamageDetailView {

    ServiceDamageDetailPresenter serviceDamageDetailPresenter;

    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;

    @BindView(R.id.ic_back)
    ImageButton btnBack;

    @BindView(R.id.fab_button)
    FloatingActionButton save;

    @BindView(R.id.chip_address)
    Chip chipAddress;

    @BindView(R.id.txt_damage_date)
    TextView txtDamageDate;

    @BindView(R.id.txt_damage_description)
    TextView txtDamageDescription;

    @BindView(R.id.recycler_view_factor)
    RecyclerView recyclerViewFactor;

    @BindView(R.id.btn_plus)
    ImageButton btnPlus;

    @BindView(R.id.factor_total_price)
    TextView txtFactorTotalPrice;

    @BindView(R.id.txt_report)
    TextInputEditText txtReportDescription;

    @BindView(R.id.txt_report_disable)
    TextView txtReportDisable;

    @BindView(R.id.txt_report_date)
    TextView txtReportDate;


    @BindView(R.id.cardView_factor)
    MaterialCardView layoutFactor;

    @BindView(R.id.cardView_report)
    MaterialCardView layoutReport;

    @BindView(R.id.cardView_report_disable)
    MaterialCardView layoutReportDisable;

    @BindView(R.id.checklist)
    TextView txtChecklist;

    Dialog dialog;
    FactorItem currentFactorItem;
    MaterialButton factorItemSave;
    MaterialButton factorItemSaveNew;

    public static ServiceDamageDetailFragment newInstance() {
        return new ServiceDamageDetailFragment();
    }

    public static String TAG = "ServiceDamageDetailFragment";

    FactorAdapter adapter;

    private ServiceMainFragmentsCallback serviceCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        serviceCallback = (ServiceMainFragmentsCallback) activity;
        serviceDamageDetailPresenter = new ServiceDamageDetailPresenter(ElevatorApplication.dataManager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(ElevatorApplication.handleNotif) {
            ElevatorApplication.handleNotif = false;
            serviceDamageDetailPresenter.getDamage(ElevatorApplication.damage.id);
        } else {
            showDamageDetail(ElevatorApplication.damage);
        }
        if(ElevatorApplication.damage.reports != null
                && ElevatorApplication.damage.reports.createdAt != null
                && ElevatorApplication.damage.factors != null
                && ElevatorApplication.damage.factors.factorItems != null) {
            disableAddFactor();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_service_damage_datail;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    protected void attachView() {
        serviceDamageDetailPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {

    }

    @OnClick(R.id.chip_address)
    public void onAddressClicked() {
        Damage damage = ElevatorApplication.damage;

        if(damage.appUser != null
                && damage.appUser.latitude != null
                && !damage.appUser.latitude.equals("")
                && damage.appUser.longitude != null
                && !damage.appUser.longitude.equals("")
                ) {

            String uri = "geo:" + damage.appUser.latitude + "," + damage.appUser.longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            getActivity().startActivity(intent);
        }
    }

    @OnClick(R.id.ic_back)
    public void backClicked() {

        serviceCallback.popStack();
    }

    @OnClick(R.id.checklist)
    public void checklistClicked() {

        serviceCallback.showChecklist();
    }

    @OnClick(R.id.btn_plus)
    public void plusClicked() {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_factor_item);
        Window window = dialog.getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.show();

        //elements
        TextInputEditText factorItemName = dialog.findViewById(R.id.spn_item_name);
        TextInputEditText factorItemQty = dialog.findViewById(R.id.factor_item_qty);
        TextInputEditText factorItemUnitPrice = dialog.findViewById(R.id.factor_item_unit_price);
        TextInputEditText factorItemPrice = dialog.findViewById(R.id.factor_item_price);
        factorItemSave = dialog.findViewById(R.id.factor_item_save);
        factorItemSaveNew = dialog.findViewById(R.id.factor_item_save_new);

        currentFactorItem = new FactorItem();

        factorItemQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                currentFactorItem.quantity = Integer.parseInt(s.toString().length() > 0 ? s.toString() : "0");
                currentFactorItem.total = currentFactorItem.quantity * currentFactorItem.unitPrice;
                factorItemPrice.setText(String.valueOf(currentFactorItem.quantity * currentFactorItem.unitPrice) );
                handleDialogSaveButton();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        factorItemUnitPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                currentFactorItem.unitPrice = Integer.parseInt(s.toString().length() > 0 ? s.toString() : "0");
                currentFactorItem.total = currentFactorItem.quantity * currentFactorItem.unitPrice;
                factorItemPrice.setText(String.valueOf(currentFactorItem.quantity * currentFactorItem.unitPrice) );
                handleDialogSaveButton();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        factorItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                currentFactorItem.name = s.toString();
                handleDialogSaveButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        factorItemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElevatorApplication.factorItems.add(currentFactorItem);
                currentFactorItem = new FactorItem();
                ElevatorApplication.isFactorItemAdded = true;
                dialog.dismiss();
            }
        });

        factorItemSaveNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElevatorApplication.factorItems.add(currentFactorItem);
                currentFactorItem = new FactorItem();
                ElevatorApplication.isFactorItemAdded = true;
                factorItemName.setText("");
                factorItemQty.setText("");
                factorItemUnitPrice.setText("");
            }
        });

        ImageButton btnClose = dialog.findViewById(R.id.ic_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(ElevatorApplication.isFactorItemAdded) {
                    ElevatorApplication.isFactorItemAdded = false;
                    refreshFactorList();
                }
            }
        });
    }

    private void refreshFactorList() {
        Damage damage = ElevatorApplication.damage;
        Factor factors = new Factor();

        int sum = 0;
        for (int i = 0; i < ElevatorApplication.factorItems.size(); i++) {
            sum += ElevatorApplication.factorItems.get(i).total;
        }

        factors.factorItems = ElevatorApplication.factorItems;
        factors.sumPrice = sum;

        damage.factors = factors;
        ElevatorApplication.damage = damage;

        Gson g = new Gson();
        Log.e("xxx", ">" + g.toJson(ElevatorApplication.damage.factors));

        showDamageDetail(ElevatorApplication.damage);
    }

    public void handleDialogSaveButton() {
        if(currentFactorItem.name.equals("") ||
                String.valueOf(currentFactorItem.quantity).equals("")) {

            factorItemSave.setEnabled(false);
            factorItemSaveNew.setEnabled(false);

            return;
        }

        factorItemSave.setEnabled(true);
        factorItemSaveNew.setEnabled(true);
    }

    @Override
    public void showProgress(boolean show) {
        circularProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showError(String error) {

        serviceCallback.showMessage(error);
    }

    @Override
    public void showMessage(String message) {

        serviceCallback.showMessage(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ElevatorApplication.damage = new Damage();
        ElevatorApplication.isChecklistsEditable = true;
        ElevatorApplication.checklists = new ArrayList<>();
    }

    @OnClick(R.id.fab_button)
    public void onSaveClicked() {
        if(ElevatorApplication.damage.factors == null ||
                ElevatorApplication.damage.factors.factorItems == null ||
                ElevatorApplication.damage.factors.factorItems.size() == 0) {
            serviceCallback.showMessage("لطفا اقلام فاکتور را وارد کنید.");
            return;
        }

        if(ElevatorApplication.damage.reports == null ||
                ElevatorApplication.damage.reports.body.equals("")) {
            serviceCallback.showMessage("لطفا گزارش را وارد کنید.");
            return;
        }

        SaveReport saveReport = new SaveReport();
        saveReport.damageId = ElevatorApplication.damage.id;
        saveReport.factorItems = ElevatorApplication.damage.factors.factorItems;
        saveReport.report = ElevatorApplication.damage.reports.body;
        Gson gson = new Gson();
        saveReport.checkList = gson.toJson(ElevatorApplication.checklists);

        ElevatorApplication.checklists.clear();

        Gson g = new Gson();
        Log.e("xxx", g.toJson(saveReport));
        serviceDamageDetailPresenter.saveFactor(saveReport);

    }

    @Override
    public void showDamageDetail(Damage damage) {

        //user
        chipAddress.setText(damage.appUser.address);

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            Date jalaliDate = df1.parse(damage.createdAt);
            txtDamageDate.setText(PersianCalendar.getPersianDate(jalaliDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        txtDamageDescription.setText(damage.description);

        //factor
        if(damage.factors != null) {

            recyclerViewFactor.setVisibility(View.VISIBLE);

            adapter = new FactorAdapter(getActivity(), damage.factors.factorItems);
            recyclerViewFactor.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewFactor.setItemAnimator(new DefaultItemAnimator());
            recyclerViewFactor.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            txtFactorTotalPrice.setText(damage.factors.sumPrice + "");

        } else {
            recyclerViewFactor.setVisibility(View.GONE);
        }

        //report
        if(damage.reports != null && damage.reports.createdAt != null) {

            layoutReportDisable.setVisibility(View.VISIBLE);
            txtReportDisable.setText(damage.reports.body);

            try {
                if(damage.reports.createdAt != null) {
                    Date jalaliDate = df1.parse(damage.reports.createdAt);
                    txtReportDate.setText(PersianCalendar.getPersianDate(jalaliDate));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Gson g = new Gson();
            List<Checklist> checklists = g.fromJson(damage.reports.checkList, new TypeToken<List<Checklist>>(){}.getType());
            ElevatorApplication.checklists = checklists;
            ElevatorApplication.isChecklistsEditable = false;

            if(ElevatorApplication.checklists == null) {
                txtChecklist.setVisibility(View.GONE);
            }

        } else {
            txtReportDescription.requestFocus();
        }

        txtReportDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Report report = new Report();
                report.body = s.toString();
                ElevatorApplication.damage.reports = report;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void goBack() {
        serviceCallback.popStack();
    }

    @SuppressLint("RestrictedApi")
    public void disableAddFactor() {
        save.setVisibility(View.GONE);
        btnPlus.setVisibility(View.GONE);
        layoutReport.setVisibility(View.GONE);
        layoutReportDisable.setVisibility(View.VISIBLE);
    }

}
