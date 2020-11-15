package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.ravensu.gaitpodzialy.appdata.UsersData;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.Document;

import java.util.ArrayList;

public class DocumentsListViewModel extends AndroidViewModel {
    private final LiveData<ArrayList<Document>> documents;
    public DocumentsListViewModel(@NonNull Application application) {
        super(application);
        documents = Transformations.map(UsersLiveData.getCurrentlySelectedUserLiveData(), user -> user.Documents);
    }
    public LiveData<ArrayList<Document>> getDocuments(){
        return documents;
    }
}
