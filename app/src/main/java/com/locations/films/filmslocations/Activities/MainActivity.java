package com.locations.films.filmslocations.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.locations.films.filmslocations.Adapters.MoviesAdapter;
import com.locations.films.filmslocations.Constants.AppConstants;
import com.locations.films.filmslocations.Models.Movie;
import com.locations.films.filmslocations.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{
    private RecyclerView recyclerview;
    private List<Movie> movieList = new ArrayList<>();
    private HashMap<String,ArrayList<String>> placesMap = new HashMap<String,ArrayList<String>>();
    private ArrayList<String> tempList = new ArrayList<String>();
    private MoviesAdapter mAdapter;
    private Boolean isListLoaded = false;
    //Strings
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerview.setLayoutManager(layoutManager);
        sharedPreferences = getSharedPreferences(AppConstants.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        movieList = loadFavorites();
        mAdapter = new MoviesAdapter(movieList,placesMap,MainActivity.this);
        recyclerview.setAdapter(mAdapter);
        sendRequest();
    }
    private void sendRequest(){
        pDialog = new ProgressDialog(MainActivity.this);
        if(movieList==null||movieList.size()==0) {
            pDialog.setMessage(AppConstants.LOADING);
            pDialog.show();
            pDialog.setCancelable(false);
        }
        else{
            isListLoaded = true;
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AppConstants.url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        showJSON(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(AppConstants.HTLog, AppConstants.ERROR + error.getMessage());
                pDialog.hide();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjReq);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //To prevent window leaks
        if(pDialog!=null)
            pDialog.cancel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showJSON(JSONObject json){
        if (json != null) {
            try {
                //Get JSON response by converting JSONArray into String
                JSONArray array = json.getJSONArray(AppConstants.DATA);
                Movie movie;
                String lastLocation="",title,genre,year,location;
                for(int i = 0,j=0; i < array.length(); i++) {
                    JSONArray obj = array.getJSONArray(i);
                    title=obj.get(8).toString();
                    genre=obj.get(12).toString();
                    year=obj.get(9).toString();
                    location=obj.get(10).toString();
                    movie = new Movie(title, genre, year,false);
                    if(!title.equals(lastLocation))
                    {
                        placesMap.put(lastLocation,tempList);
                        tempList = new ArrayList<String>();
                    }
                    tempList.add(location);
                    if((!isListLoaded)&&(j==0||(!movieList.get(j-1).getTitle().equals(movie.getTitle()))))
                    {
                        movieList.add(movie);
                        j++;
                    }
                    lastLocation=title;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(MainActivity.this, AppConstants.ERROR_PARSING, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, AppConstants.SOMETHING_WENT_WRONG, Toast.LENGTH_LONG).show();
            }
            //Setting adapter with movieList. Only notifyDataSetChanged required.
            mAdapter.notifyDataSetChanged();
            storeFavorites(movieList);
        }
        //When JSON is null
        else {
            Toast.makeText(MainActivity.this,AppConstants.UNEXPECTED_ERROR,Toast.LENGTH_SHORT).show();
        }
    }
    public void storeFavorites(List<Movie> favorites) {
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(AppConstants.FAVORITES, jsonFavorites);
        editor.commit();
    }
    public ArrayList loadFavorites() {
        List<Movie> favorites=new ArrayList<>();
        if (sharedPreferences.contains(AppConstants.FAVORITES)) {
            String jsonFavorites = sharedPreferences.getString(AppConstants.FAVORITES, null);
            Gson gson = new Gson();
            Movie[] favoriteItems = gson.fromJson(jsonFavorites,Movie[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList(favorites);
        }
        return (ArrayList) favorites;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        mAdapter.setFilter(movieList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        final List<Movie> filteredModelList = filter(movieList, newText);
        mAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private List<Movie> filter(List<Movie> models, String query) {
        query = query.toLowerCase();

        final List<Movie> filteredModelList = new ArrayList<>();
        for (Movie model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
