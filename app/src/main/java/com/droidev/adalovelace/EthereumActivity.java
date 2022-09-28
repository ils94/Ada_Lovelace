package com.droidev.adalovelace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EthereumActivity extends AppCompatActivity {

    TextView price, change, lastPrice, lastChange, lastCheck, msgs;
    Button check;
    String priceText, changeText, error = "1", url = "https://www.google.com/search?q=ethereum+usd";
    TinyDB tinydb;
    Utils utils;
    LoadSaveInfos loadSaveInfos;
    WebScraping webScraping;
    DateFormat df;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethereum);

        tinydb = new TinyDB(getApplicationContext());
        utils = new Utils();
        loadSaveInfos = new LoadSaveInfos();
        webScraping = new WebScraping();
        df = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss");

        price = findViewById(R.id.ETHPrice);
        change = findViewById(R.id.ETHchange);
        lastPrice = findViewById(R.id.ETHLastPrice);
        lastChange = findViewById(R.id.ETHLastChange);
        lastCheck = findViewById(R.id.ETHLastCheck);
        msgs = findViewById(R.id.ETHmsgs);

        check = findViewById(R.id.ETHcheck);

        check.setOnClickListener(v -> updatePrice());

        loadSavedInfo();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        loadSavedInfo();
        price.setText("Current Price");
        change.setText("---");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ethereum, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.btc:

                bitcoin();
                finish();

                break;
            case R.id.bnb:

                binance();
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadSavedInfo() {

        String[] array = loadSaveInfos.loadTinySave(getApplicationContext(),
                "lastPriceETH",
                "lastChangeETH",
                "lastCheckETH");

        lastPrice.setText(array[0]);
        lastChange.setText(utils.changeColor(getApplicationContext(), array[1]));
        lastCheck.setText(array[2]);

    }

    public void saveInfo() {

        loadSaveInfos.tinySave(getApplicationContext(),
                "lastPriceETH", price.getText().toString(),
                "lastChangeETH", change.getText().toString(),
                "lastCheckETH", df.format(Calendar.getInstance().getTime()));
    }

    @SuppressLint("SetTextI18n")
    public void updatePrice() {

        msgs.setText("Updating...");

        check.setEnabled(false);

        new Thread(() -> {

            final StringBuilder sb = new StringBuilder();

            String[] result = webScraping.getPrice(url);

            priceText = result[0];
            changeText = result[1];
            sb.append(result[2]);
            error = result[3];

            runOnUiThread(() -> {

                if (!error.equals("1")) {

                    price.setText(priceText);

                    change.setText(utils.changeColor(getApplicationContext(), changeText), TextView.BufferType.SPANNABLE);

                    saveInfo();

                }

                utils.message(sb.toString(), msgs, check);
            });
        }).start();
    }

    public void bitcoin() {

        Intent myIntent = new Intent(EthereumActivity.this, BitcoinActivity.class);
        EthereumActivity.this.startActivity(myIntent);
    }

    public void binance() {

        Intent myIntent = new Intent(EthereumActivity.this, BinanceActivity.class);
        EthereumActivity.this.startActivity(myIntent);
    }
}