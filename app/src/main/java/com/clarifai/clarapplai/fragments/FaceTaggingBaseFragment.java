package com.clarifai.clarapplai.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clarifai.clarapplai.R;

/**
 * Created by mikehhsu on 8/11/17.
 */

abstract public class FaceTaggingBaseFragment extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView resultText = (TextView)view.findViewById(R.id.textview_result);
        if(resultText != null){
            resultText.setBackgroundColor(getResultAreaBackgroundColor());
        }

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab == null) {
            return;
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,
                        "// TODO: Write an awesome implementation of Clarapplai!",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null)
                        .show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        return rootView;
    }

    int getLayoutId(){
        return R.layout.fragment_face_tagging_base;
    }

    int getResultAreaBackgroundColor(){
        return getContext().getResources().getColor(R.color.colorAccent);
    }
}
