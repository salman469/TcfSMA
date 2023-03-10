package com.tcf.sma.Adapters.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.R;

import java.util.List;
import java.util.Map;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private Map<Integer, CalendarsModel> listItems;
    private Context context;
    private List<CalendarsModel> cmList;

    public EventsAdapter(Map<Integer, CalendarsModel> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public EventsAdapter(List<CalendarsModel> cmList, Context context) {
        this.cmList = cmList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(getColorForHolidays(String.valueOf(cmList.get(position).getC_Holiday_Type_Id()))));
        viewHolder.textViewHead.setText(cmList.get(position).getC_Activity_Name());
        viewHolder.textViewDesc.setText(cmList.get(position).getC_Holiday_Type_Name());

    }

    private int getColorForHolidays(String holidayTypeId) {
        int color = R.color.light_red;  //Default Holiday for all

        switch (holidayTypeId) {
            case "1": //Working day for all
                color = R.color.light_green;
                break;
            case "2": //Holiday for students only
                color = R.color.dark_yellow;
                break;
            case "3": //Holiday for all except principal
                color = R.color.orange;
                break;
            case "4": //Holiday for all
                color = R.color.light_red;
                break;
        }

        return color;
    }

    @Override
    public int getItemCount() {
        return cmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead, textViewDesc;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewHead = itemView.findViewById(R.id.textViewHeading);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}

