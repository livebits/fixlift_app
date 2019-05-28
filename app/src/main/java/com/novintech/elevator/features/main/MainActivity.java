package com.novintech.elevator.features.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.local.PrefUtil;
import com.novintech.elevator.data.model.response.CompanyInfo;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.features.base.BaseActivity;
import com.novintech.elevator.features.base.BaseFragment;
import com.novintech.elevator.features.main.damageDetail.DamageDetailFragment;
import com.novintech.elevator.features.main.damages.DamagesFragment;
import com.novintech.elevator.features.main.newDamage.NewDamageFragment;
import com.novintech.elevator.features.main.payments.PaymentsFragment;

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

public class MainActivity extends BaseActivity implements MainFragmentsCallback, MainView {


    @BindView(R.id.bottom_bar)
    BottomAppBar bottomAppBar;

    @BindView(R.id.bottom_bar_menu)
    ConstraintLayout bottomBarMenu;

    @BindView(R.id.layout_collapse)
    ConstraintLayout fadeLayout;

    @BindView(R.id.fab_button)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.layout_damages)
    ConstraintLayout layoutDamages;

    @BindView(R.id.ic_damages)
    ImageView damageIcon;

    @BindView(R.id.txt_damages)
    TextView damageTxt;

    @BindView(R.id.ic_payment)
    ImageView paymentIcon;

    @BindView(R.id.txt_payment)
    TextView paymentTxt;

    @BindView(R.id.ic_about)
    ImageView aboutIcon;

    @BindView(R.id.txt_about)
    TextView aboutTxt;

    @BindView(R.id.layout_payment)
    ConstraintLayout layoutPayments;

    @BindView(R.id.layout_about)
    ConstraintLayout layoutAbout;

    @BindView(R.id.img_btn_close_menu)
    ImageButton closeMenuButton;

    @BindView(R.id.img_btn_exit)
    ImageButton exitButton;

    private BottomSheetBehavior mBottomSheetBehavior;
    private MainPresenter mainPresenter;
    private CompanyInfo companyInfo;
    BaseFragment fragment;

    public List<String> currentFragmentTAG = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment = DamagesFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
        currentFragmentTAG.add(fragment.getTAG());
        changeColor("damage");

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomBarMenu);
        bottomAppBar.replaceMenu(R.menu.main);
        bottomAppBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fadeLayout.animate()
                        .alpha(1.0f)
                        .setListener(null);
                fadeLayout.setVisibility(View.VISIBLE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING) {

            fadeLayout.setVisibility(View.GONE);
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void attachView() {
        mainPresenter = new MainPresenter(ElevatorApplication.dataManager);
        companyInfo = new CompanyInfo();

        mainPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {

    }

    @OnClick(R.id.img_btn_exit)
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
                    ElevatorApplication.Token = "";
                    ElevatorApplication.AppUser = null;
                    dialog.dismiss();
                    finish();

                } else {
                    finish();
                }
            }
        });


    }

    @OnClick(R.id.fab_button)
    public void fabClick() {

        NewDamageFragment dialog = new NewDamageFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialog.show(ft, NewDamageFragment.TAG);
        CloseMenuClick();
    }

    @OnClick(R.id.layout_collapse)
    public void LayoutCollapsedClicked() {


        fadeLayout.animate()
                .alpha(0f)
                .setListener(null);
        fadeLayout.setVisibility(View.GONE);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.img_btn_close_menu)
    public void CloseMenuClick() {

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        LayoutCollapsedClicked();
    }

    @OnClick(R.id.layout_damages)
    public void onDamagesClicked() {
        showFragment(DamagesFragment.newInstance());
        changeColor("damage");
        CloseMenuClick();
    }


    @OnClick(R.id.layout_payment)
    public void onTransactionsClicked() {
        showFragment(PaymentsFragment.newInstance());
        changeColor("payment");
        CloseMenuClick();
    }

    @OnClick(R.id.layout_about)
    public void onAboutClicked() {
        showAboutDialog();
        changeColor("about");
        CloseMenuClick();

    }

    private void showAboutDialog() {

        mainPresenter.getCompanyInfo();


    }

    public void changeColor(String type) {
        changeColorToWhite();
        switch (type) {

            case "damage":
                damageIcon.setColorFilter(getResources().getColor(R.color.colorSecondary));
                damageTxt.setTextColor(getResources().getColor(R.color.colorSecondary));
                break;

            case "payment":
                paymentIcon.setColorFilter(getResources().getColor(R.color.colorSecondary));
                paymentTxt.setTextColor(getResources().getColor(R.color.colorSecondary));
                break;

            case "about":
                aboutIcon.setColorFilter(getResources().getColor(R.color.colorSecondary));
                aboutTxt.setTextColor(getResources().getColor(R.color.colorSecondary));
                break;
        }
    }

    private void changeColorToWhite() {

        damageIcon.setColorFilter(getResources().getColor(R.color.colorOnPrimary));
        damageTxt.setTextColor(getResources().getColor(R.color.colorOnPrimary));
        paymentIcon.setColorFilter(getResources().getColor(R.color.colorOnPrimary));
        paymentTxt.setTextColor(getResources().getColor(R.color.colorOnPrimary));
        aboutIcon.setColorFilter(getResources().getColor(R.color.colorOnPrimary));
        aboutTxt.setTextColor(getResources().getColor(R.color.colorOnPrimary));
    }

    public void showFragment(Fragment toShowFragment) {

        if (((BaseFragment) toShowFragment).getTAG().equalsIgnoreCase(currentFragmentTAG.get(currentFragmentTAG.size() - 1))) {
            return;
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
    public void showProgress(boolean show) {
    }

    @Override
    public void showError(String error) {

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
    public void showCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        TextView company_name = dialog.findViewById(R.id.company_name);
        company_name.setText(companyInfo.companyName);
        TextView address = dialog.findViewById(R.id.address);
        address.setText(companyInfo.address);

        dialog.show();
        ImageButton backBtn = dialog.findViewById(R.id.ic_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        MaterialButton callBtn = dialog.findViewById(R.id.btn_call);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + companyInfo.phone));
                startActivity(intent);

            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                String tag = currentFragmentTAG.get(currentFragmentTAG.size() - 1);

                if(tag.equals("ServiceDamagesFragment")){
                    changeColor("damage");

                }else if(tag.equals("ServiceDamageDetailFragment")){
                    changeColor("damage");

                }else if(tag.equals("PaymentsFragment")){
                    changeColor("payment");
                }
            }
        });
    }

    @Override
    public void showDamageDetail() {

        showFragment(DamageDetailFragment.newInstance());
    }

    @Override
    public void showDamagesList() {
        if (((BaseFragment) fragment).getTAG().equalsIgnoreCase(currentFragmentTAG.get(currentFragmentTAG.size() - 1))) {
            ((DamagesFragment) fragment).refreshClicked();
            return;
        }
        showFragment(DamagesFragment.newInstance());
    }

    @Override
    public void popStack() {
        onBackPressed();
    }

}
