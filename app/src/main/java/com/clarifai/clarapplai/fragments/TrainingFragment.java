package com.clarifai.clarapplai.fragments;

import android.os.Bundle;

import com.clarifai.clarapplai.R;

/**
 * Created by mikehhsu on 8/12/17.
 */

public class TrainingFragment extends FaceTaggingBaseFragment {
    public static TrainingFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TrainingFragment fragment = new TrainingFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    int getResultAreaBackgroundColor() {
        return getContext().getResources().getColor(R.color.colorPrimary);
    }
}
