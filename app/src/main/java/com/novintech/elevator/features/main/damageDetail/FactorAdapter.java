package com.novintech.elevator.features.main.damageDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.FactorItem;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FactorAdapter extends RecyclerView.Adapter<FactorAdapter.DamageViewHolder> {

    private List<FactorItem> factorItems;
    private Context context;

    FactorAdapter() {
        factorItems = Collections.emptyList();
    }

    public void setDamage(List<FactorItem> factorItems) {
        this.factorItems = factorItems;
        notifyDataSetChanged();
    }

    public FactorAdapter(Context context, List<FactorItem> factorItems) {
        this.context = context;
        this.factorItems = factorItems;
    }

    @Override
    public DamageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_factor_item, parent, false);
        return new DamageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DamageViewHolder holder, int position) {
        FactorItem factorItem = this.factorItems.get(position);
        holder.onBind(factorItem);
    }

    @Override
    public int getItemCount() {
        return factorItems.size();
    }

    class DamageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.factor_name)
        TextView factorItemName;

        @BindView(R.id.factor_item_price)
        TextView factorItemPrice;

        @BindView(R.id.factor_item_number)
        TextView factorItemQuantity;

        @BindView(R.id.factor_item_unit_price)
        TextView factorItemUnitPrice;

        private FactorItem factorItem;

        DamageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        void onBind(FactorItem factorItem) {
            this.factorItem = factorItem;

            factorItemName.setText(factorItem.name);
            factorItemPrice.setText(factorItem.total + "");
            factorItemQuantity.setText(factorItem.quantity + "");
            factorItemUnitPrice.setText(factorItem.unitPrice + "");
        }
    }
}
