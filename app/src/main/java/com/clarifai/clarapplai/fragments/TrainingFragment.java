package com.clarifai.clarapplai.fragments;

import android.content.Intent;
import android.os.Bundle;

import com.clarifai.clarapplai.R;
import com.clarifai.clarapplai.TrainPhotoManager;

/**
 * Created by mikehhsu on 8/12/17.
 */

public class TrainingFragment extends BaseFragment {

    private final int CAMERA_TRAINING_CODE = 2345;

    public static TrainingFragment newInstance() {

        Bundle args = new Bundle();

        TrainingFragment fragment = new TrainingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getResultAreaBackgroundColor() {
        return getContext().getResources().getColor(R.color.colorPrimary);
    }

    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        textView_result.setText(getString(R.string.text_notice_area_add));
    }


    @Override
    protected void onActionBtnClick() {
        double[] currEmbeddings = getCurrentEmbeddings();
        if(currEmbeddings == null || currEmbeddings.length == 0) {
            return;
        }
        TrainPhotoManager.getInstance().addOneEmbedding(getCurrentEmbeddings());
        //reset
        textView_result.setText(getString(R.string.text_notice_area_camera_hint));
        setCurrentEmbeddings(null);
    }
}
