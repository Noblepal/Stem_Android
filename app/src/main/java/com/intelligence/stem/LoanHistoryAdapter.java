package com.intelligence.stem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LoanHistoryAdapter extends RecyclerView.Adapter<LoanHistoryAdapter.ViewHolder> {
    private ArrayList<LoanHistoryItem> mArrayList;

    public LoanHistoryAdapter(ArrayList<LoanHistoryItem> loanHistoryItemArrayList) {
        mArrayList = loanHistoryItemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loan_history_items, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        LoanHistoryItem loanHistoryItem = mArrayList.get(i);

        viewHolder.mTitleDate.setText(loanHistoryItem.getmTitleDate());
        viewHolder.mStatus.setText(loanHistoryItem.getmStatus());
        viewHolder.mLoanAmount.setText(loanHistoryItem.getmLoanAmount());
        viewHolder.mLendDate.setText(loanHistoryItem.getmLendDate());
        viewHolder.mRepayDate.setText(loanHistoryItem.getmRepayDate());

        if (loanHistoryItem.getmStatus().contentEquals("Cleared")) {
            viewHolder.mImageView.setImageResource(R.drawable.check);
            viewHolder.mStatus.setBackgroundResource(R.drawable.ontime_payment_badge);

        } else if (loanHistoryItem.getmStatus().contentEquals("Active")) {
            viewHolder.mImageView.setImageResource(R.drawable.help);
            viewHolder.mStatus.setBackgroundResource(R.drawable.active_payment_badge);


        } else if (loanHistoryItem.getmStatus().contentEquals("null")) {
            viewHolder.mImageView.setImageResource(R.drawable.ic_history_black_24dp);
        } else {
            viewHolder.mImageView.setImageResource(R.drawable.alert);
            viewHolder.mStatus.setBackgroundResource(R.drawable.late_payment_badge);

        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleDate, mStatus, mLoanAmount, mLendDate    , mRepayDate;
        ImageView mImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleDate = itemView.findViewById(R.id.history_card_date);

            mImageView = itemView.findViewById(R.id.history_imageView);
            mStatus = itemView.findViewById(R.id.history_card_payment_status);
            mLoanAmount = itemView.findViewById(R.id.tv_history_loan_amount);
            mLendDate = itemView.findViewById(R.id.tv_history_lend_date);
            mRepayDate = itemView.findViewById(R.id.tv_history_repay_date);
        }
    }
}
