package com.android.movierating.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.movierating.R;
import com.android.movierating.db.model.Movie;
import com.android.movierating.listner.RecyclerClickListner;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pallav on 8/11/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> implements Filterable {

    private List<Movie> moviesList;
    private List<Movie> filteredMovieList;
    private int lastPosition = -1;
    private Context context;
    private RecyclerClickListner mClickListner;
    private static final long FADE_DURATION = 300;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    filteredMovieList = moviesList;
                } else {

                    ArrayList<Movie> filteredList = new ArrayList<>();

                    for (Movie movie : moviesList) {

                        if (movie.getName().toLowerCase().contains(charString)) {

                            filteredList.add(movie);
                        }
                    }

                    filteredMovieList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredMovieList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredMovieList = (ArrayList<Movie>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_name)
        public TextView name;
        @BindView(R.id.tv_year)
        public TextView year;
        @BindView(R.id.tv_rating)
        public TextView rating;
        @BindView(R.id.iv_poster_preview)
        public ImageView poster;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListner.recyclerViewListClicked(view, this.getAdapterPosition(), filteredMovieList.get(getAdapterPosition()));
        }
    }

    public MovieAdapter(Context context, List<Movie> moviesList, RecyclerClickListner listener) {
        this.moviesList = moviesList;
        this.filteredMovieList = moviesList;
        this.context = context;
        mClickListner = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = filteredMovieList.get(position);
        holder.name.setText(movie.getName());
        holder.year.setText(movie.getYearOfRelease());
        holder.rating.setText(movie.getRating());
        Glide.with(context).load(movie.getImage()).into(holder.poster);
        setScaleAnimation(holder.itemView, position);
    }


    private void setScaleAnimation(View view, int position) {
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
    }

    @Override
    public int getItemCount() {
        return filteredMovieList.size();
    }
}
