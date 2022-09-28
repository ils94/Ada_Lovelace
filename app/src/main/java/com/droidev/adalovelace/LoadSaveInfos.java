package com.droidev.adalovelace;

import android.content.Context;

public class LoadSaveInfos {

    public String[] loadBigSave(Context context, String lastPriceKey, String lastTotalKey, String lastChangeKey, String lastCheckKey, String amountKey) {

        TinyDB tinydb = new TinyDB(context);

        String tinyLastPrice = tinydb.getString(lastPriceKey);
        String tinyLastTotal = tinydb.getString(lastTotalKey);
        String tinyLastChange = tinydb.getString(lastChangeKey);
        String tinyLastCheck = tinydb.getString(lastCheckKey);

        if (tinyLastPrice.isEmpty() || tinyLastTotal.isEmpty() || tinyLastChange.isEmpty() || tinyLastCheck.isEmpty()) {

            return new String[]{"---", "0", "---", "---", ""};
        } else {

            return new String[]{tinydb.getString(lastPriceKey), tinydb.getString(lastTotalKey), tinydb.getString(lastChangeKey), tinydb.getString(lastCheckKey), tinydb.getString(amountKey)};
        }
    }

    public void saveBigInfo(Context context, String amountKey, String amount, String lastPriceKey, String lastPrice, String lastTotalKey, String lastTotal, String lastChangeKey, String lastChange, String lastCheckKey, String lastCheck) {

        TinyDB tinydb = new TinyDB(context);

        tinydb.remove(amountKey);
        tinydb.remove(lastPriceKey);
        tinydb.remove(lastTotalKey);
        tinydb.remove(lastChangeKey);
        tinydb.remove(lastCheckKey);

        tinydb.putString(amountKey, amount);
        tinydb.putString(lastPriceKey, lastPrice);
        tinydb.putString(lastTotalKey, lastTotal);
        tinydb.putString(lastChangeKey, lastChange);
        tinydb.putString(lastCheckKey, lastCheck);
    }

    public void tinySave(Context context, String lastPriceKey, String lastPrice, String lastChangeKey, String lastChange, String lastCheckKey, String lastCheck) {

        TinyDB tinydb = new TinyDB(context);

        tinydb.remove(lastPriceKey);
        tinydb.remove(lastChangeKey);
        tinydb.remove(lastCheckKey);

        tinydb.putString(lastPriceKey, lastPrice);
        tinydb.putString(lastChangeKey, lastChange);
        tinydb.putString(lastCheckKey, lastCheck);
    }

    public String[] loadTinySave(Context context, String lastPriceKey, String lastChangeKey, String lastCheckKey) {

        TinyDB tinydb = new TinyDB(context);

        String tinyLastPrice = tinydb.getString(lastPriceKey);
        String tinyLastChange = tinydb.getString(lastChangeKey);
        String tinyLastCheck = tinydb.getString(lastCheckKey);

        if (tinyLastPrice.isEmpty() || tinyLastChange.isEmpty() || tinyLastCheck.isEmpty()) {

            return new String[]{"---", "---", "---"};
        } else {

            return new String[]{tinydb.getString(lastPriceKey), tinydb.getString(lastChangeKey), tinydb.getString(lastCheckKey)};
        }
    }

    public void saveAddress(Context context, String addressKey, String address) {

        TinyDB tinydb = new TinyDB(context);

        tinydb.remove(addressKey);
        tinydb.putString(addressKey, address);
    }

    public String loadAddress(Context context, String addressKey) {

        TinyDB tinydb = new TinyDB(context);

        return tinydb.getString(addressKey);
    }
}
