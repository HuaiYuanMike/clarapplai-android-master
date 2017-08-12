package com.clarifai.clarapplai.fragments;

import android.os.Bundle;

/**
 * Created by mikehhsu on 8/12/17.
 */

public class FaceTaggingFragment extends FaceTaggingBaseFragment {
    public static FaceTaggingFragment newInstance() {

        Bundle args = new Bundle();

        FaceTaggingFragment fragment = new FaceTaggingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
