package com.ravensu.gaitpodzialy.receivers;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.ravensu.gaitpodzialy.activities.data.Result;
import com.ravensu.gaitpodzialy.activities.data.model.LoggedInUser;

public class LoginResultReceiver extends ResultReceiver {
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    private Receiver receiver;
    public LoginResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver){
        this.receiver = receiver;
    }

    public interface Receiver{
        public Result<LoggedInUser> onReceiverResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null){
            receiver.onReceiverResult(resultCode, resultData);
        }
    }
}
