package com.droidev.adalovelace;

import android.annotation.SuppressLint;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

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

        Double temp;

        try {

            Document doc = Jsoup.connect("https://cexplorer.io/stake/" + address).get();

            Elements elements = doc.select("span[data-bs-toggle=tooltip]");

            Element element = elements.get(3);

            amount = element.text().replace(" ", "").replace("₳", "");

            if (amount.contains("k")) {

                amount = amount.replace("k", "");

                temp = Double.parseDouble(amount) * 1000;

                amount = String.valueOf(temp);
            } else if (amount.contains("M")) {

                amount = amount.replace("M", "");

                temp = Double.parseDouble(amount) * 1000000;

                amount = String.valueOf(temp);
            } else if (amount.contains("B")) {

                amount = amount.replace("B", "");

                temp = Double.parseDouble(amount) * 1000000000;

                amount = String.valueOf(temp);
            }

            msg = "Success!";

        } catch (IOException e) {
            e.printStackTrace();

            msg = "Could not fetch amount.";
            amount = "";
        }

        return new String[]{amount.replace(",", ""), msg};
    }
}
