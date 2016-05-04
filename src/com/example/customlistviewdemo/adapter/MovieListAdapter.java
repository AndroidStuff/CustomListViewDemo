package com.example.customlistviewdemo.adapter;

import java.io.IOException;
import java.util.List;

import com.example.customlistviewdemo.R;
import com.example.customlistviewdemo.model.Movie;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieListAdapter extends BaseAdapter {

	private List<Movie> movieList;
	private Activity activity;
	private LayoutInflater inflater;
	private Drawable defaultImage;
	private final String defaultImagePath = "default-image.png";

	public MovieListAdapter(Activity activity, List<Movie> movieList) {
		this.activity = activity;
		this.movieList = movieList;
		try {
			this.defaultImage = Drawable.createFromStream(
					activity.getApplicationContext().getAssets().open(defaultImagePath), defaultImagePath);
		} catch (IOException e) {
			Log.e(getClass().getSimpleName(), "Error setting default image : " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getCount() {
		return movieList.size();
	}

	@Override
	public Object getItem(int position) {
		return movieList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		Log.d(getClass().getSimpleName(), "getView(..) invoked...");
		if (inflater == null) {
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_row, null);
		}

		ImageView thumbnail = (ImageView) rowView.findViewById(R.id.thumbnail);
		TextView title = (TextView) rowView.findViewById(R.id.title);
		TextView rating = (TextView) rowView.findViewById(R.id.rating);
		TextView genre = (TextView) rowView.findViewById(R.id.genre);
		TextView year = (TextView) rowView.findViewById(R.id.releaseYear);
		Log.d(getClass().getSimpleName(), "" + thumbnail + title + rating + genre + year);

		Movie m = movieList.get(position);
		thumbnail.setImageDrawable(defaultImage);
		title.setText(m.getTitle());
		rating.setText(String.valueOf(m.getRating()));
		genre.setText(m.getStringifiedGenre());
		year.setText(m.getYear() + "");

		Log.d(getClass().getSimpleName(), "getView(..) exiting...");
		return rowView;
	}

}
