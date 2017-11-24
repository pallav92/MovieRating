package com.android.movierating.listner;

import android.view.View;

import com.android.movierating.db.model.Movie;

/**
 * Created by pallav on 14/11/17.
 */

public interface RecyclerClickListner {
    public void recyclerViewListClicked(View v, int position, Movie movie);
}
