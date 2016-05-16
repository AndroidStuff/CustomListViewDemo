package com.example.customlistviewdemo.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
	private String title, thumbnailUrl;
	private int year;
	private double rating;
	private ArrayList<String> genre;

	public Movie() {
	}

	public Movie(String name, String thumbnailUrl, int year, double rating, ArrayList<String> genre) {
		this.title = name;
		this.thumbnailUrl = thumbnailUrl;
		this.year = year;
		this.rating = rating;
		this.genre = genre;
	}

	public Movie(JSONObject obj) {
		try {
			this.title = obj.getString("title");
			this.thumbnailUrl = obj.getString("image");
			this.year = obj.getInt("releaseYear");
			this.rating = obj.getDouble("rating");
			this.genre = new ArrayList<String>();
			final JSONArray jsonArray = obj.getJSONArray("genre");
			for (int j = 0; j < jsonArray.length(); j++) {
				genre.add((String) jsonArray.get(j));
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public ArrayList<String> getGenre() {
		return genre;
	}

	public void setGenre(ArrayList<String> genre) {
		this.genre = genre;
	}

	public String getStringifiedGenre() {
		String genreStr = "";
		for (String str : this.genre) {
			genreStr += str + ", ";
		}
		genreStr = genreStr.length() > 0 ? genreStr.substring(0, genreStr.length() - 2) : genreStr;
		return genreStr;
	}

	@Override
	public String toString() {
		return String.format("{title: %s, thumbnailUrl: %s, year: %d, rating: %2f, genre: %s}", title, thumbnailUrl,
				year, rating, getStringifiedGenre());
	}

}
