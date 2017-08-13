package com.clarifai.clarapplai.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.clarifai.clarapplai.R;
import com.clarifai.clarapplai.TrainPhotoManager;

/**
 * Created by mikehhsu on 8/12/17.
 */

public class FaceTaggingFragment extends BaseFragment {

    public static FaceTaggingFragment newInstance() {

        Bundle args = new Bundle();

        FaceTaggingFragment fragment = new FaceTaggingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_action.setText(getString(R.string.btn_action_is_the_same));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        textView_result.setText(getString(R.string.text_notice_area_ready));
    }

    @Override
    protected void onActionBtnClick() {
        double[] currentEmdebbings = getCurrentEmbeddings();
        if(currentEmdebbings == null || currentEmdebbings.length == 0){
            return;
        }
        boolean isThisUser = TrainPhotoManager.getInstance().isTheSamePhoto(currentEmdebbings);
        textView_result.setText(isThisUser ? getString(R.string.text_result_match) : getString(R.string.text_result_match));
    }
}
