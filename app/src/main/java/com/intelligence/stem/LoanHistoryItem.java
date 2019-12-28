package com.intelligence.stem;

import android.widget.ImageView;

public class LoanHistoryItem {
    private String mTitleDate, mStatus, mLoanAmount, mLendDate, mRepayDate;
    private ImageView imageView;

    public LoanHistoryItem() {
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getmTitleDate() {
        return mTitleDate;
    }

    public void setmTitleDate(String mTitleDate) {
        this.mTitleDate = mTitleDate;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmLoanAmount() {
        return mLoanAmount;
    }

    public void setmLoanAmount(String mLoanAmount) {
        this.mLoanAmount = mLoanAmount;
    }

    public String getmLendDate() {
        return mLendDate;
    }

    public void setmLendDate(String mLendDate) {
        this.mLendDate = mLendDate;
    }

    public String getmRepayDate() {
        return mRepayDate;
    }

    public void setmRepayDate(String mRepayDate) {
        this.mRepayDate = mRepayDate;
    }
}
