package com.novintech.elevator.features.serviceMain.serviceDamageDetail.Checklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Checklist;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.DamageViewHolder> {

    private List<Checklist> checklists;
    private Context context;

    ChecklistAdapter() {
        checklists = Collections.emptyList();
    }

    public void setChecklists(List<Checklist> checklists) {
        this.checklists = checklists;
        notifyDataSetChanged();
    }

    public ChecklistAdapter(Context context, List<Checklist> checklists) {
        this.context = context;
        this.checklists = checklists;
    }

    @Override
    public DamageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_checklist, parent, false);
        return new DamageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DamageViewHolder holder, int position) {
        Checklist checklist = this.checklists.get(position);
        holder.onBind(checklist, position);
    }

    @Override
    public int getItemCount() {
        return checklists.size();
    }

    class DamageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_text)
        TextView item_text;

        @BindView(R.id.checkbox)
        CheckBox checkBox;

        private Checklist checklist;

        DamageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        void onBind(Checklist checklist, int position) {
            this.checklist = checklist;

            item_text.setText(checklist.title);
            if(checklist.isChecked){
                checkBox.setChecked(true);
            }
            else {
                checkBox.setChecked(false);

            }

            if(!ElevatorApplication.isChecklistsEditable) {
                checkBox.setEnabled(false);
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Checklist cl = ElevatorApplication.checklists.get(position);
                    if(checkBox.isChecked()){
                        checklist.isChecked = true;
                        cl.isChecked = true;
                        ElevatorApplication.checklists.set(position,cl);
                    }
                    else {
                        checklist.isChecked = false;
                        cl.isChecked = false;
                        ElevatorApplication.checklists.set(position,cl);
                    }
                }
            });
        }
    }
}
