package com.tcf.sma.Managers.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tcf.sma.Adapters.Calendar.EventsAdapter;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class EventBottomSheetDialog extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private EventsAdapter recyclerViewAdapter;
    private List<CalendarsModel> listItems = new ArrayList<>();
    private TextView tv_date;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_bottom_sheet_layout, container, false);
        return view;
    }


    public void setList(List<CalendarsModel> cmList) {
        this.listItems.clear();
        this.listItems.addAll(cmList);
        if (recyclerViewAdapter != null)
            recyclerViewAdapter.notify();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_date = view.findViewById(R.id.tv_date);
        if (listItems.size() > 0)
            tv_date.setText(AppModel.getInstance()
                    .convertDatetoFormat(listItems.get(0).Activity_Start_Date, "yyyy-MM-dd'T'hh:mm:ss", "E dd MMMM yyyy"));

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recyclerViewAdapter = new EventsAdapter(listItems, getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}

