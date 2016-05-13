package com.locations.films.filmslocations.Adapters;

/**
 * Created by Deepak on 5/13/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.locations.films.filmslocations.Activities.MapShowActivity;
import com.locations.films.filmslocations.Constants.AppConstants;
import com.locations.films.filmslocations.Models.Movie;
import com.locations.films.filmslocations.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    private Context context;
    private HashMap<String,ArrayList<String>> placesMap = new HashMap<String,ArrayList<String>>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        public ImageView like;
        public RelativeLayout movie_list;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            like = (ImageView) view.findViewById(R.id.like);
            movie_list = (RelativeLayout) view.findViewById(R.id.movie_list);
        }
    }


    public MoviesAdapter(List<Movie> moviesList,HashMap<String,ArrayList<String>> placesMap,Context ctx) {
        this.moviesList = moviesList;
        this.context = ctx;
        this.placesMap = placesMap;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Movie movie = moviesList.get(position);
        final int p=position;
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
        holder.like.setImageResource(R.drawable.heart_off);
        if(!moviesList.get(p).getLike())
        {
            holder.like.setImageResource(R.drawable.heart_off);
        }
        else
        {
            holder.like.setImageResource(R.drawable.heart_on);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!moviesList.get(p).getLike())
                {
                    moviesList.get(p).setLike(true);
                    holder.like.setImageResource(R.drawable.heart_on);
                }
                else
                {
                    moviesList.get(p).setLike(false);
                    holder.like.setImageResource(R.drawable.heart_off);
                }
            }
        });
        holder.movie_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(placesMap!=null&&placesMap.size()>0)
                {
                    Intent intent = new Intent(context,MapShowActivity.class);
                    intent.putStringArrayListExtra(AppConstants.PLACES,placesMap.get(moviesList.get(p).getTitle()));
                    context.startActivity(intent);
                }
                else
                {
                    Toast.makeText(context,AppConstants.PLEASE_WAIT,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setFilter(List<Movie> countryModels){
        moviesList = new ArrayList<>();
        moviesList.addAll(countryModels);
        notifyDataSetChanged();
    }
}


