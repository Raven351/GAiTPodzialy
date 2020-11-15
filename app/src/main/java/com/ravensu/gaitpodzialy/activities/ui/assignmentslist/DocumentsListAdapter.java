package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.appdata.GaitWebsiteUrlFinder;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.Document;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DocumentsListAdapter extends RecyclerView.Adapter<DocumentsListAdapter.ViewHolder> {
    private ArrayList<Document> documents;
    private final Fragment parentFragment;
    private final String gaitSiteURL;

    public DocumentsListAdapter(Fragment fragment){
        sortDocumentsByDate();
        this.parentFragment = fragment;
        gaitSiteURL = new GaitWebsiteUrlFinder(UsersLiveData.getCurrentlySelectedUserLiveData().getValue().UserId).getSiteUrl();
    }

    private void sortDocumentsByDate() {
        if (documents != null){
            Collections.sort(documents, (o1, o2) -> o2.Date.compareTo(o1.Date));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_documents_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position % 2 == 1) holder.mView.setBackgroundColor(Color.parseColor("#e8e8e8"));
        else holder.mView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        holder.mDocument = documents.get(position);
        String dateFormat = documents.get(position).Date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        holder.mDate.setText(dateFormat);
        holder.mName.setText(documents.get(position).Name);
        holder.mNumber.setText(documents.get(position).Number);
        holder.mView.setClickable(true);
        setOnClickListItem(holder.mView, documents.get(position).URL);

    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public void setDocuments(ArrayList<Document> documents){
        this.documents = documents;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mDate;
        public final TextView mName;
        public final TextView mNumber;
        public Document mDocument;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mDate = itemView.findViewById(R.id.documentDateLabel);
            mName = itemView.findViewById(R.id.documentTitle);
            mNumber = itemView.findViewById(R.id.documentNumber);
        }
    }

    private void setOnClickListItem(View view, final String url){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                        Intent intent = new Intent();
                        intent.setDataAndType(Uri.parse(gaitSiteURL + url), "application/pdf");
                        Intent chooserIntent = Intent.createChooser(intent, "Open Document");
                        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        parentFragment.startActivity(chooserIntent);
                        }
                    }).start();
            }
        });
    }
}
