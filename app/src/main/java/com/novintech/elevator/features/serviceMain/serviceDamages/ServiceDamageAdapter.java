package com.novintech.elevator.features.serviceMain.serviceDamages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.util.PersianCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceDamageAdapter extends RecyclerView.Adapter<ServiceDamageAdapter.DamageViewHolder> {

    private List<Damage> damageList;
    private Context context;
    private ServiceDamagesFragment damagesFragment;

    ServiceDamageAdapter() {
        damageList = Collections.emptyList();
    }

    public void setDamage(List<Damage> damages) {
        this.damageList = damages;
        notifyDataSetChanged();
    }

    public ServiceDamageAdapter(Context context, ServiceDamagesFragment damagesFragment, List<Damage> damageList) {
        this.context = context;
        this.damagesFragment = damagesFragment;
        this.damageList = damageList;
    }

    @Override
    public DamageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_damage_item, parent, false);
        return new DamageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DamageViewHolder holder, int position) {
        Damage damage = this.damageList.get(position);
        holder.onBind(damage);
    }

    @Override
    public int getItemCount() {
        return damageList.size();
    }

    class DamageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.blank_layout)
        ConstraintLayout blank_layout;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.service_name)
        TextView serviceUserName;

        @BindView(R.id.date)
        TextView visitDate;

        private Damage damage;

        DamageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    damagesFragment.showDamageDetail(getAdapterPosition());
                }
            });
        }

        void onBind(Damage damage) {
            this.damage = damage;

            description.setText(damage.description);
            if (damage.serviceUser != null) {
                serviceUserName.setText(damage.serviceUser.getFullName());
            }

            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            try {
                if(damage.visitDate != null) {
                    Date jalaliDate = df1.parse(damage.visitDate);
                    visitDate.setText(PersianCalendar.getPersianDate(jalaliDate));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(damageList.get(damageList.size() - 1).id == damage.id) {
                blank_layout.setVisibility(View.VISIBLE);
            }
        }
    }
}
