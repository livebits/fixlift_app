package com.novintech.elevator.features.main.payments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Damage;
import com.novintech.elevator.data.model.response.Factor;
import com.novintech.elevator.data.model.response.FactorPayment;
import com.novintech.elevator.util.PersianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<Damage> factors;
    private Context context;
    PaymentsFragment paymentsFragment;

    PaymentAdapter() {
        factors = Collections.emptyList();
    }

    public void setDamage(List<Damage> damages) {
        this.factors = damages;
        notifyDataSetChanged();
    }

    public PaymentAdapter(Context context, PaymentsFragment paymentsFragment, List<Damage> factors) {
        this.context = context;
        this.factors = factors;
        this.paymentsFragment = paymentsFragment;
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_payment_item, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder holder, int position) {

        holder.onBind(factors.get(position).factors);
    }

    @Override
    public int getItemCount() {
        return factors.size();
    }

    class PaymentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_factor_number)
        TextView factorNumber;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.txt_factor_price)
        TextView factorPrice;

        private Factor factor;

        PaymentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paymentsFragment.showFactorDetail(getAdapterPosition());
                }
            });
        }

        void onBind(Factor factor) {

            this.factor = factor;

            factorNumber.setText(factor.id + "");

            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            try {
                Date jalaliDate = df1.parse(factor.createdAt);
                date.setText(PersianCalendar.getPersianDate(jalaliDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            factorPrice.setText(factor.sumPrice + "");
        }
    }
}
