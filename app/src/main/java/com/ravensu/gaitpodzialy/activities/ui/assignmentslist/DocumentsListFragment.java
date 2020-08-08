package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.webscrapper.models.Document;

import java.util.ArrayList;

public class DocumentsListFragment extends Fragment {
    private static final String ARG_COlUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private DocumentsListViewModel documentsListViewModel;

    public DocumentsListFragment(){}

    public static AssignmentsListFragment newInstance(int columnCount){
        AssignmentsListFragment fragment = new AssignmentsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COlUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mColumnCount = getArguments().getInt(ARG_COlUMN_COUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documents_list, container, false);
        if (view instanceof RecyclerView){
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount<=1){
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
            else{
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            final DocumentsListAdapter adapter = new DocumentsListAdapter(this);
            recyclerView.setAdapter(adapter);
            documentsListViewModel = new ViewModelProvider(this).get(DocumentsListViewModel.class);
            documentsListViewModel.getDocuments().observe(getViewLifecycleOwner(), new Observer<ArrayList<Document>>() {
                @Override
                public void onChanged(ArrayList<Document> documents) {
                    adapter.notifyDataSetChanged();
                    adapter.setDocuments(documents);
                }
            });
        }
        return view;
    }
}
