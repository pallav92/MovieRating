package com.android.movierating.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.movierating.R;
import com.android.movierating.application.MovieApp;
import com.android.movierating.db.model.DaoSession;
import com.android.movierating.db.model.Movie;
import com.android.movierating.db.model.MovieDao;
import com.android.movierating.util.AppConstant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pallav on 14/11/17.
 */

public class WebViewFragment extends BaseFragment {

    private static final String TAG = WebViewFragment.class.getSimpleName();
    private MovieDao movieDao;
    private long id;
    private List<Movie> movie;
    private boolean isMenuEnabled = false;
    private boolean isOrientationChange = false;

    @BindView(R.id.webview)
    WebView myWebView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        isOrientationChange = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            id = savedInstanceState.getLong(AppConstant.MOVIEID);
            isOrientationChange = true;
            Log.e(TAG, "id recieved : " + id);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        DaoSession daoSession = ((MovieApp) getActivity().getApplication()).getDaoSession();
        movieDao = daoSession.getMovieDao();

        Bundle bundle = this.getArguments();
        if (!isOrientationChange && bundle != null && bundle.getLong(AppConstant.MOVIEID) > 0) {
            id = bundle.getLong(AppConstant.MOVIEID);
            Log.e(TAG, "id bundle : " + id);
        }


        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        loadURL(id);
    }

    private void loadURL(long id) {
        myWebView.stopLoading();
        myWebView.loadUrl("about:blank");
        new AsyncDataLoader().execute(id);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(AppConstant.MOVIEID, id);
        Log.e(TAG, "id : " + id);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.web_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.prev:
                Log.d(TAG, "onOptionsItemSelected() called with: item = prev");
                if (id > 1 && isMenuEnabled) {
                    isMenuEnabled = false;
                    loadURL(--id);
                } else
                    Toast.makeText(getActivity(), R.string.no_more_results, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.next:
                Log.d(TAG, "onOptionsItemSelected() called with: item = next");
                if (id < 10 && isMenuEnabled) {
                    loadURL(++id);
                    isMenuEnabled = false;
                } else
                    Toast.makeText(getActivity(), R.string.no_more_results, Toast.LENGTH_SHORT).show();
                return false;
            default:
                break;
        }
        return false;
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading() called with: view = [" + view + "], url = [" + url + "]");
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "onPageStarted() called with: view = [" + view + "], url = [" + url + "], favicon = [" + favicon + "]");
        }

        public void onPageFinished(WebView view, String url) {
            isMenuEnabled = true;
            progressBar.setVisibility(View.GONE);
            Log.d(TAG, "onPageFinished() called with: view = [" + view + "], url = [" + url + "]");
        }

    }

    private class AsyncDataLoader extends AsyncTask<Long, Void, List<Movie>> {
        ProgressDialog pdLoading = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }

        @Override
        protected List<Movie> doInBackground(Long... params) {
            movie = movieDao.queryBuilder().where(MovieDao.Properties.Id.eq(params[0])).list();
            return movie;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            super.onPostExecute(result);
            String url = "";
            if (movie.get(0).getImdbURL() != null)
                url = movie.get(0).getImdbURL();
            myWebView.setWebViewClient(new MyWebViewClient());
            myWebView.loadUrl(url);
            Log.e(TAG, "loadUrl: " + url);
            pdLoading.dismiss();
        }

    }
}
