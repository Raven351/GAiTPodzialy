package com.ravensu.gaitpodzialy.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public abstract class BaseDialogFragment<T> extends DialogFragment {
    private T mContext;

    public final T getActivityInstance(){
        return mContext;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = (T) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}
