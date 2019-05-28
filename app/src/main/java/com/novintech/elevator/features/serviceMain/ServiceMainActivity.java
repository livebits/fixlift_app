package com.novintech.elevator.features.serviceMain;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BaseActivity;
import com.novintech.elevator.features.base.BaseFragment;
import com.novintech.elevator.features.serviceMain.dailySchedule.DailyFragment;
import com.novintech.elevator.features.serviceMain.serviceDamageDetail.Checklist.ChecklistFragment;
import com.novintech.elevator.features.serviceMain.serviceDamageDetail.ServiceDamageDetailFragment;
import com.novintech.elevator.features.serviceMain.serviceDamages.ServiceDamagesFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.OnClick;

public class ServiceMainActivity extends BaseActivity implements ServiceMainFragmentsCallback {


    @BindView(R.id.bottom_bar)
    BottomAppBar bottomAppBar;

    @BindView(R.id.fab_button)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.layout_collapse)
    ConstraintLayout fadeLayout;

    @BindView(R.id.parent_layout)
    CoordinatorLayout containerLayout;

    public List<String> currentFragmentTAG = new ArrayList<>();

    public DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseFragment fragment = ServiceDamagesFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
        currentFragmentTAG.add(fragment.getTAG());
        bottomAppBar.setNavigationIcon(R.drawable.wrench_yellow);

        bottomAppBar.replaceMenu(R.menu.service_main);
        bottomAppBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFragment(ServiceDamagesFragment.newInstance());
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_logout:
                        exitClick();
                        break;
                }
                return true;
            }
        });

        ElevatorApplication.snackBarView = findViewById(R.id.container);

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            Gson g = new Gson();
            if(getIntent().getExtras() != null && getIntent().getExtras().getString("fcmData") != null && getIntent().getExtras().getString("fcmData") != "") {
                ElevatorApplication.damage = g.fromJson((String) getIntent().getExtras().getString("fcmData"), Damage.class);
            }

            ElevatorApplication.handleNotif = true;

            showDamageDetail();
            return;
        }
    }


    public void exitClick() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.show();

        ImageButton closeBtn = dialog.findViewById(R.id.ic_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        CheckBox checkBox = dialog.findViewById(R.id.checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView txtCheckBox = dialog.findViewById(R.id.txt_checkbox);
        txtCheckBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                checkBox.performClick();
                return false;
            }
        });

        MaterialButton yesBtn = dialog.findViewById(R.id.btn_yes);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {

                    User blankUser = new User();
                    PrefUtil.putString(ElevatorApplication.ApplicationContext, "user", "");
                    ElevatorApplication.Token ="";
                    ElevatorApplication.AppUser = null;
                    dialog.dismiss();
                    finish();

                }
                else {
                    finish();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_service_main;
    }

    @Override
    protected void attachView() {

    }

    @Override
    protected void detachPresenter() {

    }

    @OnClick(R.id.fab_button)
    public void fabClick() {

//        showFragment(DailyFragment.newInstance());
        DailyFragment dialog = new DailyFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialog.show(ft, DailyFragment.TAG);

    }

    public void showFragment(Fragment toShowFragment) {


        if (((BaseFragment) toShowFragment).getTAG().equalsIgnoreCase(currentFragmentTAG.get(currentFragmentTAG.size() - 1))) {
            return;
        }

        if (((BaseFragment) toShowFragment).getTAG().equalsIgnoreCase("ServiceDamagesFragment")) {
            bottomAppBar.setNavigationIcon(R.drawable.wrench_yellow);
        } else {
            bottomAppBar.setNavigationIcon(R.drawable.wrench_white);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out)
                .replace(R.id.container, toShowFragment)
                .commit();

        currentFragmentTAG.add(((BaseFragment) toShowFragment).getTAG());

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        currentFragmentTAG.remove(currentFragmentTAG.size() - 1);

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showMessage(String message) {

        Snackbar snackbar = Snackbar.make(findViewById(R.id.container), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            sbView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                snackbar.getView().getLayoutParams();
        params.setMargins(0, 0, 0, bottomAppBar.getHeight() + 10);
        snackbar.getView().setLayoutParams(params);

        snackbar.show();
    }

    @Override
    public void showDamageDetail() {
        showFragment(ServiceDamageDetailFragment.newInstance());
    }

    @Override
    public void showDamagesList() {
        showFragment(ServiceDamagesFragment.newInstance());
    }

    @Override
    public void showDailySchedule() {

        showFragment(DailyFragment.newInstance());
    }

    @Override
    public void popStack() {
        onBackPressed();
    }

    @Override
    public void showBuildingDialog() {


    }

    @Override
    public void showChecklist() {
        showFragment(ChecklistFragment.newInstance());
    }

}
