package com.gyaanguru.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gyaanguru.Activities.DailyQuizActivity;
import com.gyaanguru.R;

public class QuizzesFragment extends Fragment {

    ImageView dailyQuiz, mathKids, googly;

    public QuizzesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quizzes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ImageViews here
        dailyQuiz = (ImageView)view.findViewById(R.id.dailyQuiz);
        mathKids = (ImageView)view.findViewById(R.id.mathKids);
        googly = (ImageView)view.findViewById(R.id.googly);

        dailyQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DailyQuizActivity.class);
            //    intent.putParcelableArrayListExtra("list", ArrayList(questionsList()));
                startActivity(intent);
            }
        });
        mathKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "MathKids will come soon", Toast.LENGTH_SHORT).show();
            }
        });
        googly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Googly will come soon", Toast.LENGTH_SHORT).show();
            }
        });

    }

}