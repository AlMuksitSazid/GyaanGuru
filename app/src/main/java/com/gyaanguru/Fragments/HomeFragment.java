package com.gyaanguru.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyaanguru.Activities.NewsActivity;
import com.gyaanguru.Adapter.SliderAdapter;
import com.gyaanguru.Models.SliderData;
import com.gyaanguru.R;

import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private SliderAdapter adapter;
    private ArrayList<SliderData> sliderDataArrayList;
    private SliderView sliderView;

    private ImageView prothomAlo, karatoa;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sliderDataArrayList = new ArrayList<>();
        sliderView = (SliderView) view.findViewById(R.id.slider);
        loadImages();

        prothomAlo = (ImageView) view.findViewById(R.id.prothomAlo);
        karatoa = (ImageView) view.findViewById(R.id.karatoa);

        prothomAlo.setOnClickListener(this);
        karatoa.setOnClickListener(this);
    }

    private void loadImages() {
        sliderDataArrayList.add(new SliderData(R.drawable.journalist_holding_newspaper));
        sliderDataArrayList.add(new SliderData(R.drawable.slider_job));
        sliderDataArrayList.add(new SliderData(R.drawable.slider_quiz));
        sliderDataArrayList.add(new SliderData(R.drawable.slider_latest));
        sliderDataArrayList.add(new SliderData(R.drawable.slider_tutorials));

        adapter = new SliderAdapter(getContext(), sliderDataArrayList);
        sliderView.setSliderAdapter(adapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

    @Override
    public void onClick(View view) {
        Intent newsIntent = new Intent(getActivity(), NewsActivity.class);
        if (view.getId() == R.id.prothomAlo) {
            newsIntent.putExtra("NewsURL", "https://www.prothomalo.com/");
        }
        else if (view.getId() == R.id.karatoa) {
            newsIntent.putExtra("NewsURL", "https://www.dailykaratoa.com/");
        }
        startActivity(newsIntent);
    }

}