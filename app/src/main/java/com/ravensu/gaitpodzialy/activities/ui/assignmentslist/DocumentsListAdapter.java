package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.service.autofill.UserData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.activities.ui.MainViewPager;
import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.Document;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class DocumentsListAdapter extends RecyclerView.Adapter<DocumentsListAdapter.ViewHolder> {
    private MutableLiveData<User> currentlySelectedUser = new MutableLiveData<>(UsersData.getCurrentlySelectedUser());
    private ArrayList<Document> documents;
    private Fragment parentFragment;

    public DocumentsListAdapter(Fragment fragment){
        this.documents = currentlySelectedUser.getValue().Documents;
        Collections.reverse(this.documents);
        this.parentFragment = fragment;
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
        holder.mDocument = documents.get(position);
        String dateFormat = new SimpleDateFormat("dd-MM-yyyy").format(documents.get(position).Date);
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
                Intent intent = new Intent();
                intent.setDataAndType(Uri.parse("http://" + url), "application/pdf");
                Intent chooserIntent = Intent.createChooser(intent, "Open Document");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + url));
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                parentFragment.startActivity(chooserIntent);
//                WebView webView = new WebView(parentFragment.getContext());
//                webView.loadUrl("http://" + url);
            }
        });
    }
}
