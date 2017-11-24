package com.android.movierating.fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.movierating.R;
import com.android.movierating.adapter.MovieAdapter;
import com.android.movierating.application.MovieApp;
import com.android.movierating.db.model.DaoSession;
import com.android.movierating.db.model.Movie;
import com.android.movierating.db.model.MovieDao;
import com.android.movierating.listner.RecyclerClickListner;
import com.android.movierating.util.AppConstant;
import com.android.movierating.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pallav on 14/11/17.
 */

public class MovieListFragment extends BaseFragment implements SearchView.OnQueryTextListener, RecyclerClickListner {

    private static final String TAG = MovieListFragment.class.getSimpleName();

    @BindView(R.id.rv_movie_list)
    RecyclerView rvMovieList;

    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>();
    private MovieDao movieDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        DaoSession daoSession = ((MovieApp) getActivity().getApplication()).getDaoSession();
        movieDao = daoSession.getMovieDao();
        new AsyncDataLoader().execute();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        movieAdapter.getFilter().filter(s);
        return true;
    }


    @Override
    public void recyclerViewListClicked(View v, int position, Movie movie) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            Bundle bundle = new Bundle();
            bundle.putLong(AppConstant.MOVIEID, movie.getId());
            WebViewFragment webViewFragment = new WebViewFragment();
            webViewFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.rl_container, webViewFragment).addToBackStack(null).commit();
        }
    }


    private class AsyncDataLoader extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            movieList = movieDao.loadAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG,"Movie list Size" + movieList.size());
            movieAdapter = new MovieAdapter(getActivity(),movieList,MovieListFragment.this);
            rvMovieList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvMovieList.setItemAnimator(new DefaultItemAnimator());
            rvMovieList.setAdapter(movieAdapter);
            pdLoading.dismiss();
        }

    }
}



