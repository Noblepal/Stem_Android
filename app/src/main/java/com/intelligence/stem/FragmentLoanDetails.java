package com.intelligence.stem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentLoanDetails extends Fragment {

    private TextView first, second, third, fourth, total;
    private TextView date_one, date_two, date_three, date_four;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loan_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        first = getActivity().findViewById(R.id.amt_first);
        second = getActivity().findViewById(R.id.amt_second);
        third = getActivity().findViewById(R.id.amt_third);
        fourth = getActivity().findViewById(R.id.amt_fourth);
        total = getActivity().findViewById(R.id.amt_total);

        date_one = getActivity().findViewById(R.id.tv_date_one);
        date_two = getActivity().findViewById(R.id.tv_date_two);
        date_three = getActivity().findViewById(R.id.tv_date_three);
        date_four = getActivity().findViewById(R.id.tv_date_four);

        getCurrentDate();

    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        addWeekToCalendar(calendar);
        String strDate = dateFormat.format(calendar.getTime());

        addWeekToCalendar(calendar);
        String strDate2 = dateFormat.format(calendar.getTime());

        addWeekToCalendar(calendar);
        String strDate3 = dateFormat.format(calendar.getTime());

        addWeekToCalendar(calendar);
        String strDate4 = dateFormat.format(calendar.getTime());

        date_one.setText(strDate);
        date_two.setText(strDate2);
        date_three.setText(strDate3);
        date_four.setText(strDate4);
    }

    private void addWeekToCalendar(Calendar calendar) {
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
    }

    public void initData(String data) {
        CurrencyFormatter currencyFormatter = new CurrencyFormatter();
        //currencyFormatter.FormatCurrencyFromString(data);

        float amount = Float.parseFloat(data);
        String mTotal = String.valueOf(calculateInterestAndTotal(amount));

        total.setText(currencyFormatter.FormatCurrencyFromString(mTotal));
        String subTotal = String.valueOf(calculateCumulativeTotals(Float.parseFloat(mTotal)));
        String formattedSub = currencyFormatter.FormatCurrencyFromString(subTotal);
        first.setText(formattedSub);
        second.setText(formattedSub);
        third.setText(formattedSub);
        fourth.setText(formattedSub);
    }

    public float calculateInterestAndTotal(float amount) {
        float i;
        float r = (float) (12.00 / 100.00);
        float total;

        i = r * amount;
        total = i + amount;
        return total;
    }

    public float calculateCumulativeTotals(float total) {
        return total / 4;
    }
}
