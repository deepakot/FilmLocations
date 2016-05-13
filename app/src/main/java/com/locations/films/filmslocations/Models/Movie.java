package com.locations.films.filmslocations.Models;

/**
 * Created by Deepak on 5/13/2016.
 */
public class Movie {
    private String title, genre, year;

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    private Boolean like;

    public Movie() {
    }

    public Movie(String title, String genre, String year, Boolean like) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.like = like;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

