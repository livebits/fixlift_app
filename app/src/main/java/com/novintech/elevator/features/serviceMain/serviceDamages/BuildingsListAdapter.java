package com.novintech.elevator.features.serviceMain.serviceDamages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.User;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BuildingsListAdapter extends RecyclerView.Adapter<BuildingsListAdapter.DamageViewHolder> {

    private List<User> buildingsList;
    private Context context;

    BuildingsListAdapter() {
        buildingsList = Collections.emptyList();
    }

    public void setDamage(List<User> users) {
        this.buildingsList = users;
        notifyDataSetChanged();
    }

    public BuildingsListAdapter(Context context, List<User> buildingsList) {
        this.context = context;
        this.buildingsList = buildingsList;
    }

    @Override
    public DamageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_choose_building, parent, false);
        return new DamageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DamageViewHolder holder, int position) {
        User building = this.buildingsList.get(position);
        holder.onBind(building);
    }

    @Override
    public int getItemCount() {
        return buildingsList.size();
    }

    class DamageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.building_name)
        TextView buildingName;

        @BindView(R.id.building_address)
        TextView buildingAddress;

        private User building;

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

        void onBind(User building) {
            this.building = building;

            buildingName.setText(this.building.buildingName);
            buildingAddress.setText(this.building.address);
        }
    }
}
