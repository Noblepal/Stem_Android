package com.intelligence.stem;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

public class CurrencyFormatter {
    public CurrencyFormatter() {
    }

    public String FormatCurrencyFromString(String data) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        numberFormat.setCurrency(Currency.getInstance("KES"));
        numberFormat.setMinimumFractionDigits(2);
        String currency = null;
        try {
            currency = String.valueOf(numberFormat.format(numberFormat.parse(data)));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Ksh. " + currency;
    }
}
