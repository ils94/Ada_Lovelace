package com.droidev.adalovelace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CardanoActivity extends AppCompatActivity {

    TextView price, total, gainLoss, change, lastPrice, lastTotal, lastChange, lastCheck, msgs;
    EditText amount;
    Button check;
    String priceText, changeText, totalValue, error = "1", url = "https://www.google.com/search?q=cardano+usd";
    TinyDB tinydb;
    Utils utils;
    LoadSaveInfos loadSaveInfos;
    WebScraping webScraping;
    AlertDialogs alertDialogs;
    DateFormat df;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardano);

        Notifications adaNotifications = new Notifications();

        adaNotifications.createNotificationChannel(getApplicationContext());

        tinydb = new TinyDB(getApplicationContext());
        utils = new Utils();
        loadSaveInfos = new LoadSaveInfos();
        webScraping = new WebScraping();
        alertDialogs = new AlertDialogs();
        df = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss");

        price = findViewById(R.id.ADAPrice);
        amount = findViewById(R.id.ADAAmount);
        total = findViewById(R.id.ADAtotal);
        gainLoss = findViewById(R.id.ADAgainloss);
        change = findViewById(R.id.ADAchange);
        lastPrice = findViewById(R.id.ADAlastPrice);
        lastTotal = findViewById(R.id.ADAlastTotal);
        lastChange = findViewById(R.id.ADAlastChange);
        lastCheck = findViewById(R.id.ADAlastCheck);
        msgs = findViewById(R.id.ADAmsgs);

        check = findViewById(R.id.ADAcheck);

        loadSavedInfo();

        check.setOnClickListener(v -> updatePrice());

        gainLoss.setOnClickListener(v -> Toast.makeText(CardanoActivity.this, "Gain/Loss = Total - Last Total", Toast.LENGTH_LONG).show());
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        loadSavedInfo();
        price.setText("Current Price");
        total.setText("---");
        gainLoss.setText("---");
        change.setText("---");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cardano, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.about:

                alertDialogs.about(CardanoActivity.this);

                break;

            case R.id.pp:

                privacyPolicy();

                break;

            case R.id.wallet:

                alertDialogs.stakeKeyAddress(CardanoActivity.this, amount, msgs, check);

                break;

            case R.id.notificationsON:

                alertDialogs.turnOnNotifications(CardanoActivity.this);

                break;

            case R.id.notificationsOFF:

                alertDialogs.turnOffNotifications(CardanoActivity.this);

                break;

            case R.id.btc:

                bitcoin();

                break;
            case R.id.eth:

                ethereum();

                break;
            case R.id.bnb:

                binance();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    public void updatePrice() {

        amount.clearFocus();

        msgs.setText("Updating...");

        check.setEnabled(false);

        new Thread(() -> {

            final StringBuilder sb = new StringBuilder();

            String[] result = webScraping.getPriceWithAmount(amount.getText().toString(), url);

            priceText = result[0];
            changeText = result[1];
            totalValue = result[2];
            sb.append(result[3]);
            error = result[4];

            runOnUiThread(() -> {

                if (!error.equals("1")) {

                    price.setText(priceText);

                    total.setText(totalValue);

                    gainLoss.setText(utils.gainLoss(total.getText().toString(), lastTotal.getText().toString()));

                    change.setText(utils.changeColor(getApplicationContext(), changeText), TextView.BufferType.SPANNABLE);

                    saveInfo();

                }

                utils.message(sb.toString(), msgs, check);
            });
        }).start();
    }

    public void loadSavedInfo() {

        String[] array = loadSaveInfos.loadBigSave(getApplicationContext(),
                "lastPriceADA",
                "lastTotalADA",
                "lastChangeADA",
                "lastCheckADA",
                "amountADA");

        lastPrice.setText(array[0]);
        lastTotal.setText(array[1]);
        lastChange.setText(utils.changeColor(getApplicationContext(), array[2]));
        lastCheck.setText(array[3]);
        amount.setText(array[4]);
    }

    public void saveInfo() {

        loadSaveInfos.saveBigInfo(getApplicationContext(), "amountADA", amount.getText().toString(),
                "lastPriceADA", price.getText().toString(),
                "lastTotalADA", total.getText().toString(),
                "lastChangeADA", change.getText().toString(),
                "lastCheckADA", df.format(Calendar.getInstance().getTime()));
    }

    public void privacyPolicy() {

        Intent myIntent = new Intent(CardanoActivity.this, PrivacyPolicyActivity.class);
        CardanoActivity.this.startActivity(myIntent);
    }

    public void bitcoin() {

        Intent myIntent = new Intent(CardanoActivity.this, BitcoinActivity.class);
        CardanoActivity.this.startActivity(myIntent);
    }

    public void ethereum() {

        Intent myIntent = new Intent(CardanoActivity.this, EthereumActivity.class);
        CardanoActivity.this.startActivity(myIntent);
    }

    public void binance() {

        Intent myIntent = new Intent(CardanoActivity.this, BinanceActivity.class);
        CardanoActivity.this.startActivity(myIntent);
    }
}