package com.example.customlistviewdemo.adapter;

import java.io.IOException;
import java.util.List;

import com.example.customlistviewdemo.R;
import com.example.customlistviewdemo.model.Movie;
import com.example.customlistviewdemo.task.ImageDownloaderTask;

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

	static class ViewHolder { // ViewHolder Pattern
		TextView title;
		TextView rating;
		TextView genre;
		TextView year;
		ImageView thumbnail;
	}

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
		ViewHolder viewHolder = null;

		if (inflater == null) {
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_row, null);
			viewHolder = createViewHolder(rowView);
			rowView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) rowView.getTag();

		Movie m = movieList.get(position);
		//getBackground() for the background attribute - meaning setBackground()
		viewHolder.thumbnail.setBackground(defaultImage);
		//getDrawable() for the image you set on the src attribute - meaning setImageResource/Bitmap
		if (viewHolder.thumbnail.getDrawable() == null) {
			new ImageDownloaderTask(viewHolder.thumbnail).execute(m.getThumbnailUrl());
		}
		viewHolder.title.setText(m.getTitle());
		viewHolder.rating.setText(String.valueOf(m.getRating()));
		viewHolder.genre.setText(m.getStringifiedGenre());
		viewHolder.year.setText(m.getYear() + "");

		return rowView;
	}

	private ViewHolder createViewHolder(View rowView) {
		ViewHolder viewHolder;
		viewHolder = new ViewHolder();
		viewHolder.thumbnail = (ImageView) rowView.findViewById(R.id.thumbnail);
		viewHolder.title = (TextView) rowView.findViewById(R.id.title);
		viewHolder.rating = (TextView) rowView.findViewById(R.id.rating);
		viewHolder.genre = (TextView) rowView.findViewById(R.id.genre);
		viewHolder.year = (TextView) rowView.findViewById(R.id.releaseYear);
		return viewHolder;
	}

}
