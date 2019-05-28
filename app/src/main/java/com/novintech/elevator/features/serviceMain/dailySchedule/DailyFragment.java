package com.novintech.elevator.features.serviceMain.dailySchedule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.features.base.BaseDialogFragment;
import com.novintech.elevator.features.main.damages.DamageAdapter;
import com.novintech.elevator.features.serviceMain.ServiceMainFragmentsCallback;
import com.novintech.elevator.util.PersianCalendar;
import com.novintech.elevator.util.RecyclerItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class DailyFragment extends BaseDialogFragment implements DailyView {

    DailyPresenter dailyPresenter;
    String date;
    Dialog dialog;
    List<Damage> damages;

    @BindView(R.id.recycler_daily_list)
    RecyclerView recyclerViewDamages;

    @BindView(R.id.chip_no_item)
    Chip noItem;

    @BindView(R.id.ic_close)
    ImageButton btnClose;

    @BindView(R.id.btn_search)
    Button btnSearch;

    @BindView(R.id.npYear)
    WheelYearPicker npYear;

    @BindView(R.id.npMonth)
    WheelPicker npMonth;

    @BindView(R.id.npDay)
    WheelYearPicker npDay;


    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;

    public static DailyFragment newInstance() {
        return new DailyFragment();
    }

    public static String TAG = "DailyFragment";

    public static final String TAG_FRAG_DATE_TIME = "fragDateTime";
    private static final String KEY_DIALOG_TITLE = "dialogTitle";
    private static final String KEY_INIT_DATE = "initDate";
    Typeface font;
    private Context context;
    private Bundle mArgument;
    String[] years;
    String[] monthes;
    int lastDay;

    DailyListAdapter adapter;

    private ServiceMainFragmentsCallback serviceCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        serviceCallback = (ServiceMainFragmentsCallback) activity;
        dailyPresenter = new DailyPresenter(ElevatorApplication.dataManager);
    }

    @Override
    public void onStart() {
        super.onStart();

        dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        font = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.font));

        setDate(PersianCalendar.getPersianDate(c));

//        setDate("1397/10/21");
        dailyPresenter.getDamages(formattedDate);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_daily;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    protected void attachView() {
        dailyPresenter.attachView(this);
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

    @Override
    public void showMessage(String message) {
        serviceCallback.showMessage(message);
    }

    @Override
    public void showDailyList(List<Damage> damages) {

        this.damages = damages;

        if(damages.size() == 0) {
            noItem.setVisibility(View.VISIBLE);
            recyclerViewDamages.setVisibility(View.INVISIBLE);
        } else {
            noItem.setVisibility(View.GONE);
            recyclerViewDamages.setVisibility(View.VISIBLE);

            adapter = new DailyListAdapter(getActivity(), damages);
            recyclerViewDamages.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewDamages.setItemAnimator(new DefaultItemAnimator());
            recyclerViewDamages.setAdapter(adapter);

            recyclerViewDamages.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            dismiss();
                            showDamageDetail(position);
                        }
                    })
            );

            adapter.notifyDataSetChanged();
        }

    }

    @OnClick(R.id.ic_close)
    public void OnDailyClose() {

        dialog.dismiss();
    }

    public void showDamageDetail(int position) {

        ElevatorApplication.damage = damages.get(position);
        serviceCallback.showDamageDetail();
    }


    //Date
    public void setDate(String initDate) {

        String[] ymd = initDate.split("/");

        ArrayList<String> monthes = new ArrayList<String>();
        monthes.add("فروردین");
        monthes.add("اردیبهشت");
        monthes.add("خرداد");
        monthes.add("تیر");
        monthes.add("مرداد");
        monthes.add("شهریور");
        monthes.add("مهر");
        monthes.add("آبان");
        monthes.add("آذر");
        monthes.add("دی");
        monthes.add("بهمن");
        monthes.add("اسفند");
        npYear.setYearStart(1376);
        npYear.setYearEnd(1416);
        npYear.setTypeface(font);
        npMonth.setTypeface(font);
        npMonth.setData(monthes);
        npDay.setYearStart(1);
        npDay.setYearEnd(31);
        npDay.setTypeface(font);

        //check if year is not in rang sent error to user
        if (Integer.parseInt(ymd[0]) >= 1376 && Integer.parseInt(ymd[0]) <= 1416)
            npYear.setSelectedYear(Integer.parseInt(ymd[0]));
        else {
//            mOnDialogResultSetListener.ResultSet("Err:Year( "+ymd[0]+" )is not in range");
//            dismiss();
        }
        //check if month is not in range sent error to user
        if (Integer.parseInt(ymd[0]) >= 1 && Integer.parseInt(ymd[1]) <= 12)
            npMonth.setSelectedItemPosition(Integer.parseInt(ymd[1]) - 1);
        else {
//            mOnDialogResultSetListener.ResultSet("Err:Month( "+ymd[1]+" )is not in range");
//            dismiss();
        }
        //check if day is not in range sent error to user
        if (Integer.parseInt(ymd[0]) >= 1 && Integer.parseInt(ymd[2]) <= 31)
            npDay.setSelectedYear(Integer.parseInt(ymd[2]));
        else {
//            mOnDialogResultSetListener.ResultSet("Err:day( "+ymd[2]+" )is not in range");
//            dismiss();
        }
        //for check day in range
        setDayOfMonth(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]), Integer.parseInt(ymd[2]));

        npYear.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
                checkAndSetDay(npYear.getCurrentYear(), npMonth.getCurrentItemPosition() + 1, npDay.getCurrentItemPosition() + 1);
            }
        });
        npMonth.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
                checkAndSetDay(npYear.getCurrentYear(), npMonth.getCurrentItemPosition() + 1, npDay.getCurrentItemPosition() + 1);
            }
        });
    }

    @OnClick(R.id.btn_search)
    public void onSearchClicked() {

        String y;
        y = npYear.getCurrentYear() + "";

        String m = String.valueOf(npMonth.getCurrentItemPosition() + 1);
        if (npMonth.getCurrentItemPosition() + 1 < 10)
            m = "0" + m;

        String d = String.valueOf(npDay.getCurrentItemPosition() + 1);
        if (npDay.getCurrentItemPosition() + 1 < 10)
            d = "0" + d;

        date = y + "-" + m + "-" + d;

        dailyPresenter.getDamages(convertDate(date));
    }

    public void setDayOfMonth(int year, int month, int day) {
        if (day < 1) {
//            mOnDialogResultSetListener.ResultSet("Err:day( "+day+" )is not in range");
//            dismiss();
        }

        if (month <= 6 && day > 31) {
//            mOnDialogResultSetListener.ResultSet("Err:day( "+day+" )is not in range");
//            dismiss();
        }

        if (month > 6 && month <= 12 && day > 30) {
//            mOnDialogResultSetListener.ResultSet("Err:day( "+day+" )is not in range");
//            dismiss();
        }

        if (isLeapYear(year) && month == 12 && day > 30) {
//            mOnDialogResultSetListener.ResultSet("Err:day( "+day+" )is not in range");
//            dismiss();
        }
        if ((!isLeapYear(year)) && month == 12 && day > 29) {
//            mOnDialogResultSetListener.ResultSet("Err:day( "+day+" )is not in range");
//            dismiss();
        }
    }

    public boolean isLeapYear(int year) {
        int y;
        if (year > 0)
            y = year - 474;
        else
            y = 473;
        return (((((y % 2820) + 474) + 38) * 682) % 2816) < 682;
    }

    public void checkAndSetDay(int year, int month, int day) {
        if (month <= 5) {
            npDay.setYearStart(1);
            npDay.setYearEnd(31);
        }
        if (month > 5) {
            npDay.setYearStart(1);
            npDay.setYearEnd(30);
        }
        if (isLeapYear(year) && month == 12) {
            npDay.setYearStart(1);
            npDay.setYearEnd(30);
        }
        if ((!isLeapYear(year)) && month == 12) {
            npDay.setYearStart(1);
            npDay.setYearEnd(29);
        }
    }

    public String convertDate(String date)
    {
        PersianCalendar pdate = new PersianCalendar(date);
        Calendar c = pdate.getGregorianCalendar();
        Date gDate = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(gDate);
    }
}
