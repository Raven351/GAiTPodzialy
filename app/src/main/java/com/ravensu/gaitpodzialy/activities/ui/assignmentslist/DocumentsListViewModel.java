package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.Document;

import java.util.ArrayList;

public class DocumentsListViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Document>> documents;
    public DocumentsListViewModel(@NonNull Application application) {
        super(application);
        documents = new MutableLiveData<>(UsersData.getCurrentlySelectedUser().Documents);
    }
    public LiveData<ArrayList<Document>> getDocuments(){
        if (documents == null){
            documents = new MutableLiveData<ArrayList<Document>>();
            loadDocuments();
        }
        return documents;
    }

    private void loadDocuments(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                documents = new MutableLiveData<>(UsersData.getCurrentlySelectedUser().Documents);
            }
        }).start();
    }
}
