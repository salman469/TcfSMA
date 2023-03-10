package com.tcf.sma.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.StudentErrorLogAdapter;
import com.tcf.sma.Helpers.DbTables.FeesCollection.ErrorLog;
import com.tcf.sma.R;

public class StudentErrorFragment extends Fragment {

    RecyclerView rv_studentErrorLog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_student_error_log, null);
        rv_studentErrorLog = (RecyclerView) view.findViewById(R.id.rv_studentErrorLog);
        rv_studentErrorLog.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv_studentErrorLog.setAdapter(new StudentErrorLogAdapter(ErrorLog.getInstance(getActivity()).getFailedRecords()
                , getActivity()));

        return view;

    }
}
