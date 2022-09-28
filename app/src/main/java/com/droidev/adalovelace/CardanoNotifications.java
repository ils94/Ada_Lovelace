package com.droidev.adalovelace;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CardanoNotifications {

    String url = "https://www.google.com/search?q=cardano+usd";

    public void ShowNotification(Context context) {

        new Thread(() -> {

            String text;

            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss");

            TinyDB tinydb = new TinyDB(context);

            WebScraping webScraping = new WebScraping();

            Notifications notifications = new Notifications();

            String price = webScraping.getPrice(url)[0];
            String change = webScraping.getPrice(url)[1];

            String lastPrice = tinydb.getString("lastPriceADA");
            String lastChange = tinydb.getString("lastChangeADA");

            if (lastPrice.equals("")) {

                text = "Price: " + price + " | 24h Change: " + change;

            } else {

                text = "Price: " + price + " | 24h Change: " + change + "\nLast Price: " + lastPrice + " | Last 24h Change: " + lastChange;
            }

            if (tinydb.getString("lastTotalADA").equals("")) {

                tinydb.remove("lastTotalADA");
                tinydb.putString("lastTotalADA", "0");
            }

            notifications.NotificationBuilder("ADA", text, context);

            tinydb.remove("lastPriceADA");
            tinydb.remove("lastChangeADA");
            tinydb.remove("lastCheckADA");

            tinydb.putString("lastPriceADA", price);
            tinydb.putString("lastChangeADA", change);
            tinydb.putString("lastCheckADA", df.format(Calendar.getInstance().getTime()));
        }).start();
    }
}
