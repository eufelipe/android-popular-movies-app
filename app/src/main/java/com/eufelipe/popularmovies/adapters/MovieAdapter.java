package com.eufelipe.popularmovies.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.viewholders.MovieViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private List<Movie> movieList = new ArrayList<>();
    private Context mContext;

    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.movieList = movieList;
        this.mContext = mContext;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View layout = layoutInflater.inflate(R.layout.fragment_movie_list_item, null);
        MovieViewHolder movieViewHolder = new MovieViewHolder(layout);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return this.movieList.size();
    }


    /**
     * @param position
     * @param movie
     * @desription : Método para add itens
     */
    public void addItem(int position, Movie movie) {
        movieList.add(position, movie);
        Integer index = movieList.indexOf(movie);
        notifyItemInserted(index);

    }


}
