package com.ravensu.gaitpodzialy.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.appdata.SavedAppMainLogin;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

public class ConfirmGeneralDialogFragment extends BaseDialogFragment<ConfirmGeneralDialogFragment.onDialogFragmentClickListener> {
    public interface onDialogFragmentClickListener{
        public void onPositiveClicked(ConfirmGeneralDialogFragment dialogFragment);
        public void onNegativeClicked(ConfirmGeneralDialogFragment dialogFragment);
    }

    public static ConfirmGeneralDialogFragment newInstance(String title, String message) {
        ConfirmGeneralDialogFragment fragment = new ConfirmGeneralDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        fragment.setArguments(args);
        return fragment;
    }

    public static ConfirmGeneralDialogFragment newInstance(String title, String message, Bundle args) {
        ConfirmGeneralDialogFragment fragment = new ConfirmGeneralDialogFragment();
        args.putString("title", title);
        args.putString("message", message);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivityInstance().onPositiveClicked(ConfirmGeneralDialogFragment.this);
                    }
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> getActivityInstance().onNegativeClicked(ConfirmGeneralDialogFragment.this))
                .create();
    }
}
