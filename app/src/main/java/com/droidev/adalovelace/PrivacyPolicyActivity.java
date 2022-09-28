package com.droidev.adalovelace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class PrivacyPolicyActivity extends AppCompatActivity {

    TextView privacyPolicy;
    String texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        this.setTitle("Privacy Policy");

        texto = "Ada Lovelace Privacy Police\n" +
                "\n" +
                "• Does this app collects any personal information from the user?\n" +
                "\n" +
                "This app does not collect any personal information.\n" +
                "\n" +
                "• Where does this app saves all information?\n" +
                "\n" +
                "This app saves all information locally. The information will be deleted once you clear this app's cache/data or uninstall.\n" +
                "\n" +
                "• Does this app requires special permissions to run on my Android phone?\n" +
                "\n" +
                "No. This app only permission on Android is to have access to the internet so it can grab information about the prices of the cryptocurrencies and display them.";

        privacyPolicy = findViewById(R.id.pptext);
        privacyPolicy.setText(texto);
        privacyPolicy.setMovementMethod(new ScrollingMovementMethod());
    }
}