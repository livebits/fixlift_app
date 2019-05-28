package com.novintech.elevator.features.serviceMain.serviceDamageDetail.Checklist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.novintech.elevator.ElevatorApplication;
import com.novintech.elevator.R;
import com.novintech.elevator.data.model.response.Checklist;
import com.novintech.elevator.features.base.BaseFragment;
import com.novintech.elevator.features.serviceMain.ServiceMainFragmentsCallback;
import com.novintech.elevator.features.serviceMain.serviceDamageDetail.FactorAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class ChecklistFragment extends BaseFragment implements ChecklistView {


    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;

    @BindView(R.id.ic_back)
    ImageButton btnBack;

    @BindView(R.id.ic_save)
    ImageButton btnSave;

    @BindView(R.id.recycler_view_checklist)
    RecyclerView recyclerViewCheckLists;

    public static String TAG = "ChecklistFragment";
    ChecklistAdapter adapter;
    private ServiceMainFragmentsCallback serviceCallback;
    ChecklistPresenter checklistPresenter;
    List<Checklist> checklists = new ArrayList<>();


    public static ChecklistFragment newInstance() {
        return new ChecklistFragment();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        serviceCallback = (ServiceMainFragmentsCallback) activity;
        checklistPresenter = new ChecklistPresenter(ElevatorApplication.dataManager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(ElevatorApplication.checklists.size() == 0){
            checklistPresenter.getChecklistItems();
        }
        else {
            showCheckLists(ElevatorApplication.checklists);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_checklist;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    protected void attachView() {
        checklistPresenter.attachView(this);
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
    public void showCheckLists(List<Checklist> myCheckLists) {
        this.checklists = myCheckLists;

        adapter = new ChecklistAdapter(getActivity(), myCheckLists);
        recyclerViewCheckLists.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewCheckLists.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCheckLists.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void goBack() {
        serviceCallback.popStack();
    }

    @OnClick(R.id.ic_back)
    public void backClicked() {

        serviceCallback.popStack();
    }

    @OnClick(R.id.ic_save)
    public void saveClicked() {

       goBack();
    }
}
