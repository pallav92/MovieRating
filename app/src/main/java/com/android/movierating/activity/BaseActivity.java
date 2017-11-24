package com.android.movierating.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;

import com.android.movierating.fragment.BaseFragment;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

    }

    /*
     Replace fragment with fragment instance as well as store in back stack = true
     */
    public void replaceFragment(int containerId, BaseFragment fragment, boolean isBackStack) {
        try {
            if (getFragmentManager() != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (fragment.isAdded()) {
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                    getSupportFragmentManager().executePendingTransactions();
                }
                if (isBackStack) {
                    fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                }
                fragmentTransaction.replace(containerId, fragment);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public void replaceFragment(int containerId, BaseFragment fragment) {
        replaceFragment(containerId, fragment, false);
    }

}