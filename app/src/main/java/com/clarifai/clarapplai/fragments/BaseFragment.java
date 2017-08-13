package com.clarifai.clarapplai.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clarifai.clarapplai.R;
import com.clarifai.clarapplai.api.ClarifaiAPI;
import com.clarifai.clarapplai.api.ClarifaiEmbeddingRequest;
import com.clarifai.clarapplai.api.ClarifaiEmbeddingResult;
import com.clarifai.clarapplai.util.FaceCropper;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.IOException;

import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mikehhsu on 8/11/17.
 */

abstract public class BaseFragment extends Fragment {

    private final int CAMERA_REQUEST_CODE = 1234;

    protected ImageView imageView_photo;
    protected TextView textView_result;

    protected Button btn_action;
    protected Button btn_cancel;

    private ClarifaiAPI clarifaiAPI;

    private double[] currentEmbeddings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clarifaiAPI = new ClarifaiAPI("iL5b9z5VHpLwF1hpfdmX92Vg5MFcvsydv3QpxUSx", "C2TEObsUvdKe1Mnf87Xv6CaSouW7VWoa6Ie9QWLD");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView_result = (TextView) view.findViewById(R.id.textview_result);
        imageView_photo = (ImageView) view.findViewById(R.id.imageview_photo);
        btn_action = (Button) view.findViewById(R.id.button_action);
        btn_cancel = (Button) view.findViewById(R.id.button_cancel);

        textView_result.setText(getString(R.string.text_notice_area_camera_hint));

        if (textView_result != null) {
            textView_result.setBackgroundColor(getResultAreaBackgroundColor());
        }

        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionBtnClick();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelBtnClick();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab == null) {
            return;
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view,
//                        "// TODO: Write an awesome implementation of Clarapplai!",
//                        Snackbar.LENGTH_INDEFINITE)
//                        .setAction("Action", null)
//                        .show();
                onCameraBtnClick();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        return rootView;
    }

    protected int getLayoutId() {
        return R.layout.fragment_face_tagging_base;
    }

    protected int getResultAreaBackgroundColor() {
        return getContext().getResources().getColor(R.color.colorAccent);
    }

    void onCameraBtnClick() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CAMERA_REQUEST_CODE) {
            return;
        }

        Log.d(this.getClass().getSimpleName(), "OnActivityResult with request code: " + requestCode);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            loadIntoPhotoImageView((Bitmap) bundle.get("data"));
        }
    }

    private void loadIntoPhotoImageView(final Bitmap bitmap) {
        if (imageView_photo == null || bitmap == null) {
            return;
        }
        imageView_photo.setImageBitmap(bitmap);
        Bitmap bitmapCopy = bitmap.copy(bitmap.getConfig(), true);
        makeGetEmbeddingsRequest(bitmapCopy);
        StandardDeviation d = new StandardDeviation();

    }

    private void makeGetEmbeddingsRequest(Bitmap bitmap) {
        if (clarifaiAPI == null) {
            return;
        }
        final ClarifaiEmbeddingRequest embeddingRequest = clarifaiAPI.requestEmbeddings(new FaceCropper().getCroppedImage(bitmap));
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    embeddingRequest.requestSync();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }.execute();
        embeddingRequest.requestAsync(new ClarifaiEmbeddingRequest.Callback() {
            @Override
            public void onSuccess(ClarifaiEmbeddingResult result) {
                currentEmbeddings = result.getEmbeddings();
                Log.d(this.getClass().getSimpleName(), "embeddings: " + result.getEmbeddings());
            }

            @Override
            public void onUnsuccessfulResponse(Response response) {
                Log.d(this.getClass().getSimpleName(), "Retrive Embedding Unsuccessfull! " + response.message());

            }

            @Override
            public void onNetworkFailure(IOException exception) {
                Log.d(this.getClass().getSimpleName(), "Retrieve Embedding Failed!" + exception.getMessage());
            }
        });
    }

    protected void onCancelBtnClick(){
        if(imageView_photo != null){
            imageView_photo.setImageBitmap(null);
        }
        //reset
        textView_result.setText(getString(R.string.text_notice_area_camera_hint));
        setCurrentEmbeddings(null);
    }

    protected abstract void onActionBtnClick();

    public double[] getCurrentEmbeddings() {
        return currentEmbeddings;
    }

    public void setCurrentEmbeddings(double[] currentEmbeddings) {
        this.currentEmbeddings = currentEmbeddings;
    }
}
