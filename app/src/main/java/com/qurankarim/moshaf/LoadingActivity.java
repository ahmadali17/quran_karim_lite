package com.qurankarim.moshaf;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingActivity extends AppCompatDialogFragment {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingActivity(Activity myactivity) {
        activity = myactivity;
    }

    public void startLoadingActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_loading, null));

        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

    }

    public void dismissDailog() {
        dialog.dismiss();
    }
}