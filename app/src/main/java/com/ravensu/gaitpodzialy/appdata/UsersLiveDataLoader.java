package com.ravensu.gaitpodzialy.appdata;

import android.content.Context;

import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UsersLiveDataLoader {
    private final Context context;

    public UsersLiveDataLoader(Context context) {
        this.context = context;
    }

    public boolean loadUsersData(){
        if (SavedAppLogins.existsAny(context)){
            final Map<String, ?> savedUsersCredentials = SavedAppLogins.getAllCredentials(context);
            ExecutorService executorService = Executors.newFixedThreadPool(savedUsersCredentials.size());
            List<Future<User>> userFutureList = new ArrayList<Future<User>>();
            for (Map.Entry<String, ?> entry : savedUsersCredentials.entrySet()){
                ScrapUserDataTask scrapUserDataTask = new ScrapUserDataTask(entry.getKey(), entry.getValue().toString());
                Future<User> userFuture = executorService.submit(scrapUserDataTask);
                userFutureList.add(userFuture);
            }
            for (Future<User> userFuture : userFutureList){
                try {
                    UsersLiveData.addUserData(userFuture.get());
                }
                catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }
            }
        }
        if (UsersLiveData.getUsersLiveData().getValue() != null){
            User mainUser = UsersLiveData.getUsersLiveData().getValue().get(SavedAppMainLogin.GetMainLoginUserId(context));
            UsersLiveData.setMainUser(mainUser);
            UsersLiveData.setCurrentlySelectedUser(mainUser);
        }
        return UsersLiveData.getCurrentlySelectedUserLiveData().getValue() != null;
    }

}
