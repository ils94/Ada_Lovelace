package com.droidev.adalovelace;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class AlertDialogs {

    WebScraping webScraping = new WebScraping();
    LoadSaveInfos loadSaveInfos = new LoadSaveInfos();
    Utils utils = new Utils();

    public void turnOnNotifications(Context context) {

        EditText interval = new EditText(context);
        interval.setHint("Interval must be 15 or higher");
        interval.setInputType(InputType.TYPE_CLASS_NUMBER);
        interval.setMaxLines(1);

        LinearLayout lay = new LinearLayout(context);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(interval);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("Turn on notifications")
                .setMessage("This will enable notifications that will show current ADA price in a set interval (in minutes) defined by the user.")
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .setView(lay)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(v -> {

            if (interval.getText().toString().isEmpty()) {

                Toast.makeText(context, "The interval field cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(interval.getText().toString()) < 15) {

                Toast.makeText(context, "The interval must be 15 or higher", Toast.LENGTH_SHORT).show();

            } else {

                int intervalInt = Integer.parseInt(interval.getText().toString());

                WorkManager.getInstance(context).cancelAllWorkByTag("ADANotification");

                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                WorkRequest notification = new PeriodicWorkRequest.Builder(NotificationWorker.class, intervalInt, TimeUnit.MINUTES)
                        .addTag("ADANotification")
                        .setConstraints(constraints)
                        .build();

                WorkManager.getInstance(context).enqueue(notification);

                Toast.makeText(context, "Notifications will appear every " + intervalInt + " minutes", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
    }

    public void turnOffNotifications(Context context) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("Turn off notifications")
                .setMessage("You will not receive price notifications for ADA unless you turn on the notifications.")
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(v -> {

            WorkManager.getInstance(context).cancelAllWorkByTag("ADANotification");

            Toast.makeText(context, "The notifications are now turned off", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        });
    }

    public void stakeKeyAddress(Activity activity, EditText editText, TextView textView, Button button) {

        EditText address = new EditText(activity);
        address.setHint("Insert stake address here");
        address.setInputType(InputType.TYPE_CLASS_TEXT);
        address.setMaxLines(1);

        LinearLayout lay = new LinearLayout(activity);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(address);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("Stake Address")
                .setMessage("Insert your stake address to fetch the ADA amount.")
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Clear", null)
                .setView(lay)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        address.setText(loadSaveInfos.loadAddress(activity, "addressADA"));

        positiveButton.setOnClickListener(v -> {

            if (address.getText().toString().isEmpty()) {

                Toast.makeText(activity, "The address field cannot be empty", Toast.LENGTH_SHORT).show();
            } else {

                address.setText(address.getText().toString().replace(" ", ""));

                dialog.setMessage("Fetching...");

                positiveButton.setEnabled(false);
                negativeButton.setEnabled(false);
                neutralButton.setEnabled(false);

                new Thread(() -> {

                    final StringBuilder sb = new StringBuilder();

                    String[] result = webScraping.getAmountFromAddress(address.getText().toString());

                    String addressAmount = result[0];

                    sb.append(result[1]);

                    activity.runOnUiThread(() -> {
                        editText.setText(addressAmount);

                        loadSaveInfos.saveAddress(activity, "addressADA", address.getText().toString());

                        dialog.dismiss();

                        utils.message(sb.toString(), textView, button);
                    });
                }).start();
            }
        });

        neutralButton.setOnClickListener(v -> {

            address.setText("");
            loadSaveInfos.saveAddress(activity, "addressADA", address.getText().toString());
        });
    }

    public void about(Context context) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("About this app")
                .setMessage("The purpose of this small project is to track your ADA portfolio." +
                        "\n\nAll information is saved locally and is not collected. " +
                        "\n\nThis app uses Google Finance data to display prices." +
                        "\n\nThis app uses cardanoscan.io to fetch ADA amount from stake address" +
                        "\n\nAll token logos are property of their owners.")
                .setPositiveButton("Ok", null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(v -> dialog.dismiss());
    }
}
