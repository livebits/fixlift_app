package com.novintech.elevator;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.novintech.elevator.data.Connection;
import com.novintech.elevator.data.DataManager;
import com.novintech.elevator.data.model.response.Checklist;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.FactorItem;
import com.novintech.elevator.data.model.response.Filter;
import com.novintech.elevator.data.model.response.User;
import com.novintech.elevator.data.remote.ElevatorService;
import com.novintech.elevator.util.FontsOverride;
//import com.singhajit.sherlock.core.Sherlock;
import com.squareup.leakcanary.LeakCanary;
import com.tspoon.traceur.Traceur;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ElevatorApplication extends Application {


    public static DataManager dataManager;
    public static View snackBarView;
    public static ElevatorService elevatorService;
    public static Context ApplicationContext;
    public static User AppUser;
    public static String Token = "";
    public static Damage damage = new Damage();
    public static boolean handleNotif = false;
    public static Filter filter = new Filter();
    public static Boolean isFilterChanged = false;
    public static Boolean isFactorItemAdded = false;
    public static List<FactorItem> factorItems = new ArrayList<>();
    public static List<Checklist> checklists = new ArrayList<>();
    public static boolean isChecklistsEditable = true;

    public static ElevatorApplication get(Context context) {
        return (ElevatorApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//            Stetho.initializeWithDefaults(this);
//            LeakCanary.install(this);
//            Sherlock.init(this);
//            Traceur.enableLogging();
//        }

        FontsOverride.setDefaultFont(this, "DEFAULT", "IRANSansMobile(FaNum).ttf");
        FontsOverride.setDefaultFont(this, "monospace", "IRANSansMobile(FaNum).ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "IRANSansMobile(FaNum)_Bold.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "IRANSansMobile(FaNum).ttf");
        FontsOverride.setDefaultFont(this, "normal", "IRANSansMobile(FaNum).ttf");
        FontsOverride.setDefaultFont(this, "SANS", "IRANSansMobile(FaNum).ttf");

        ApplicationContext = getApplicationContext();

        elevatorService =
                Connection.loopbackAPI().create(ElevatorService.class);

        dataManager = new DataManager(elevatorService);


        User user = dataManager.getUserFromPref();
        if (user != null) {
            ElevatorApplication.AppUser = user;
            if (user.token != null) {
                ElevatorApplication.Token = user.token.id;
            }
        }
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) ApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

}
