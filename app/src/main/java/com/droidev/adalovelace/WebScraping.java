package com.droidev.adalovelace;

import android.annotation.SuppressLint;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraping {

    private String price;
    private String change;
    private String msg;
    private String error;
    private Double total;

    public String[] getPrice(String url) {

        try {

            Document doc = Jsoup.connect(url).get();

            price = doc.select("span[class=pclqee]").text().replace(",", ".");


            if (!price.isEmpty()) {

                try {

                    change = doc.select("span[jsname=SwWl3d]").text().replace(",", ".").replace("−", "-") + " " + doc.select("span[jsname=rfaVEf]").text().replace("()", "").replace(",", ".");

                    if (change.contains("-0.00")) {

                        change = change.replace("-", "");
                    }

                    error = "0";

                    msg = "The price has been updated.";

                } catch (Exception e) {
                    e.printStackTrace();

                    msg = "No price found.";

                    error = "1";
                }

            } else {

                msg = "Something went wrong, please try again.";

                error = "1";
            }

        } catch (IOException e) {
            e.printStackTrace();

            msg = "Something went very wrong, please try again.";

            error = "1";
        }

        return new String[]{"$" + price, change, msg, error};
    }

    @SuppressLint("DefaultLocale")
    public String[] getPriceWithAmount(String amount, String url) {

        try {

            Document doc = Jsoup.connect(url).get();

            price = doc.select("span[class=pclqee]").text().replace(",", ".");


            if (!amount.isEmpty() && !price.isEmpty()) {

                try {

                    total = Double.parseDouble(price) * Double.parseDouble(amount);

                    change = doc.select("span[jsname=SwWl3d]").text().replace(",", ".").replace("−", "-") + " " + doc.select("span[jsname=rfaVEf]").text().replace("()", "").replace(",", ".");

                    if (change.contains("-0.00")) {

                        change = change.replace("-", "");
                    }

                    error = "0";

                    msg = "The price has been updated.";

                } catch (Exception e) {
                    e.printStackTrace();

                    msg = "Invalid Amount.";

                    error = "1";
                }

            } else {

                msg = "Something went wrong, please try again.";

                error = "1";
            }

        } catch (IOException e) {
            e.printStackTrace();

            msg = "Something went very wrong, please try again.";

            error = "1";
        }

        return new String[]{"$" + price, change, "$" + String.format("%.2f", total).replace(",", "."), msg, error};
    }

    public String[] getAmountFromAddress(String address) {

        String amount;

        try {

            Document doc = Jsoup.connect("https://cexplorer.io/stake/" + address).get();

            Elements elements = doc.select("#data > div > div > table > tbody > tr:nth-child(6) > td:nth-child(2) > div > span:nth-child(2)");

            String[] clearElement;

            clearElement = elements.get(0).toString().split("\"");

            amount = clearElement[3].replace(",", "");

            msg = "Success!";

        } catch (IOException e) {
            e.printStackTrace();

            msg = "Could not fetch amount.";
            amount = "";
        }

        return new String[]{amount.replace(",", ""), msg};
    }
}
