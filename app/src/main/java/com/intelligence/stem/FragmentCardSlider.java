package com.intelligence.stem;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class FragmentCardSlider extends Fragment implements View.OnClickListener {

    CustomInterface customInterface;
    private CardView two_hundred, five_hundred, one_thousand, two_thousand, three_thousand;
    private TextView swipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_slider, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipe = getActivity().findViewById(R.id.swipe_right);
        swipe.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));

        customInterface = (CustomInterface) getActivity();


        two_hundred = getActivity().findViewById(R.id.card_200);
        five_hundred = getActivity().findViewById(R.id.card_500);
        one_thousand = getActivity().findViewById(R.id.card_1000);
        two_thousand = getActivity().findViewById(R.id.card_2000);
        three_thousand = getActivity().findViewById(R.id.card_3000);

        two_hundred.setOnClickListener(this);
        five_hundred.setOnClickListener(this);
        one_thousand.setOnClickListener(this);
        two_thousand.setOnClickListener(this);
        three_thousand.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_200:
                customInterface.setAmount("200");
                break;
            case R.id.card_500:
                customInterface.setAmount("500");
                break;
            case R.id.card_1000:
                customInterface.setAmount("1000");
                break;
            case R.id.card_2000:
                customInterface.setAmount("2000");
                break;
            case R.id.card_3000:
                customInterface.setAmount("3000");
                break;
        }
    }
}
