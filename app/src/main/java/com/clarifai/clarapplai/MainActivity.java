package com.clarifai.clarapplai;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.clarifai.clarapplai.fragments.FaceTaggingFragment;
import com.clarifai.clarapplai.fragments.TrainingFragment;

/**
 * Feel free to make more Activities, if you need more,
 * delete this one if you want to approach this differently, etc.
 */
public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (viewPager != null)
        {
            viewPager.setAdapter(new PhotoTaggingPagerAdapter(getSupportFragmentManager()));
        }
        // NOTE: We will provide you with an app ID and secret, which you can use to make an API instance like this:
        // ClarifaiAPI clarifaiAPI = new ClarifaiAPI(APP_ID, APP_SECRET);

        // Use the Clarifai API now!
    }

    class PhotoTaggingPagerAdapter extends FragmentPagerAdapter{
        final int PAGE_COUNT = 2;

        public PhotoTaggingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return FaceTaggingFragment.newInstance();
                case 1:
                    return TrainingFragment.newInstance();
            }
            return FaceTaggingFragment.newInstance();
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}
