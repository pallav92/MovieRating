package com.android.movierating.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.android.movierating.activity.BaseActivity;

public class BaseFragment extends Fragment {

    protected BaseActivity activity;
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = (BaseActivity) activity;
    }

}