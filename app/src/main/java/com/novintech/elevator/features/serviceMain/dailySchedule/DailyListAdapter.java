package com.novintech.elevator.features.serviceMain.dailySchedule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.util.PersianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DailyListAdapter extends RecyclerView.Adapter<DailyListAdapter.DailyViewHolder> {

    private Context context;
    List<Damage> damages;

    public DailyListAdapter(Context context, List<Damage> damages) {
        this.context = context;
        this.damages = damages;
    }

    public void setDamage(List<Damage> damages) {
        this.damages = damages;
        notifyDataSetChanged();
    }

    @Override
    public DailyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_daily, parent, false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DailyViewHolder holder, int position) {

        Damage damage = this.damages.get(position);
        Log.e("xxx",damage.id);
        holder.onBind(damage);
    }

    @Override
    public int getItemCount() {
        return damages.size();
    }

    class DailyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.building_name)
        TextView buildingName;

        @BindView(R.id.building_address)
        TextView address;

        @BindView(R.id.date)
        TextView time;

        Damage damage;

        DailyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(Damage damage) {

            Log.e("yyy", damages.size()+"");
            this.damage = damage;

            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            try {
                Date jalaliDate = df1.parse(damage.visitDate);
                time.setText(PersianCalendar.getPersianDate(jalaliDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            address.setText(damage.appUser.address);
            buildingName.setText(damage.appUser.buildingName);
        }
    }
}