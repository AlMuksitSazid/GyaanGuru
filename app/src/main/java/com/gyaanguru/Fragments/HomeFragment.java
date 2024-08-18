package com.gyaanguru.Fragments;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyaanguru.Adapter.SliderAdapter;
import com.gyaanguru.Models.SliderData;
import com.gyaanguru.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private SliderAdapter adapter;
    private ArrayList<SliderData> sliderDataArrayList;
    FirebaseFirestore firebaseFirestore;
    private SliderView sliderView;

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
        firebaseFirestore = FirebaseFirestore.getInstance();
        loadImages();
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

}