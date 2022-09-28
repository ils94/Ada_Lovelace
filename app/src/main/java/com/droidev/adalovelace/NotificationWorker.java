package com.droidev.adalovelace;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        CardanoNotifications cardanoNotifications = new CardanoNotifications();

        cardanoNotifications.ShowNotification(getApplicationContext());

        return Result.success();
    }
}
