package com.android.movierating.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.android.movierating.R;
import com.android.movierating.fragment.MovieListFragment;
import com.android.movierating.util.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pallav on 8/11/17.
 */

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.app_name));
        if (savedInstanceState == null) {
            replaceFragment(R.id.rl_container, new MovieListFragment(), false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, AppConstant.CurrentFragmentKey, getSupportFragmentManager().findFragmentById(R.id.rl_container));
    }
}
