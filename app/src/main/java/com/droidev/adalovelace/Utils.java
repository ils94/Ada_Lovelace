package com.droidev.adalovelace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class Utils {

    public SpannableStringBuilder changeColor(Context context, String s) {

        SpannableStringBuilder builder = new SpannableStringBuilder();

        if (s.contains("-")) {

            SpannableString redSpannable = new SpannableString(s);
            redSpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.red)), 0, s.length(), 0);
            builder.append(redSpannable);

        } else if (s.contains("+")) {

            SpannableString greenSpannable = new SpannableString(s);
            greenSpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.darkgreen)), 0, s.length(), 0);
            builder.append(greenSpannable);
        } else {

            SpannableString graySpannable = new SpannableString(s);
            graySpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.black)), 0, s.length(), 0);
            builder.append(graySpannable);
        }

        return builder;
    }

    @SuppressLint("DefaultLocale")
    public String gainLoss(String total, String lastTotal) {

        Double num1 = Double.parseDouble(total.replace("$", ""));
        Double num2 = Double.parseDouble(lastTotal.replace("$", ""));
        Double num3 = num1 - num2;

        return "$" + String.format("%.2f", num3).replace(",", ".");
    }

    public void message(String s, TextView textView, Button button) {

        textView.setText(s);

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            textView.setText("");

            button.setEnabled(true);
        }, 3000);
    }
}