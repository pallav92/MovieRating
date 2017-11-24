package com.android.movierating.application;

import android.app.Application;

import com.android.movierating.db.DbOpenHelper;
import com.android.movierating.db.model.DaoMaster;
import com.android.movierating.db.model.DaoSession;
import com.android.movierating.db.model.Movie;
import com.android.movierating.db.model.MovieDao;
import com.android.movierating.db.model.MovieList;
import com.android.movierating.util.AppUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pallav on 8/11/17.
 */

public class MovieApp extends Application {
    private DaoSession mDaoSession;
    MovieDao movieDao;

    @Override
    public void onCreate() {
        super.onCreate();
        mDaoSession = new DaoMaster(new DbOpenHelper(this, "movie_rating.db")
                .getWritableDb()).newSession();
        movieDao = getDaoSession().getMovieDao();
        if (movieDao.queryBuilder().count() < 10) {
            loadJSONFromAsset();
        }
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }


    private void loadJSONFromAsset() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = getApplicationContext().getAssets().open("movie_rating.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    String json = new String(buffer, "UTF-8");
                    MovieList movies = AppUtils.parseJson(json, MovieList.class);
                    for (Movie movie : movies.getMovies()) {
                        movieDao.insert(movie);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
}
