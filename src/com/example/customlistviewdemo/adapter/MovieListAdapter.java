package com.example.customlistviewdemo.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.customlistviewdemo.R;
import com.example.customlistviewdemo.app.AppGovernment;
import com.example.customlistviewdemo.model.Movie;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MovieListAdapter extends BaseAdapter {

	private List<Movie> movieList;
	private Activity activity;
	private LayoutInflater inflater;
	private ImageLoader imageLoader = AppGovernment.getInstance().getImageLoader();

	static class ViewHolder { // ViewHolder Pattern
		TextView title;
		TextView rating;
		TextView genre;
		TextView year;
		NetworkImageView thumbnail;
	}

	public MovieListAdapter(Activity activity, List<Movie> movieList) {
		this.activity = activity;
		this.movieList = movieList;
		debugLog("Movie list of size " + movieList.size() + " is set in the MovieListAdapter");
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
		debugLog("getView() called for position " + position);
		ViewHolder viewHolder = null;

		if (inflater == null) {
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		if (imageLoader == null) {
			imageLoader = AppGovernment.getInstance().getImageLoader();
		}

		Movie m = movieList.get(position);
		if (rowView == null) {
			debugLog("rowView is null for View position " + position);
			rowView = inflater.inflate(R.layout.list_row, null);
			viewHolder = createViewHolder(rowView);
			viewHolder.thumbnail.setTag(m.getThumbnailUrl());
			rowView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) rowView.getTag();

		viewHolder.title.setText(m.getTitle());
		viewHolder.rating.setText(String.valueOf(m.getRating()));
		viewHolder.genre.setText(m.getStringifiedGenre());
		viewHolder.year.setText(m.getYear() + "");
		viewHolder.thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);

		return rowView;
	}

	private ViewHolder createViewHolder(View rowView) {
		ViewHolder viewHolder;
		viewHolder = new ViewHolder();
		viewHolder.thumbnail = (NetworkImageView) rowView.findViewById(R.id.thumbnail);
		viewHolder.title = (TextView) rowView.findViewById(R.id.title);
		viewHolder.rating = (TextView) rowView.findViewById(R.id.rating);
		viewHolder.genre = (TextView) rowView.findViewById(R.id.genre);
		viewHolder.year = (TextView) rowView.findViewById(R.id.releaseYear);
		return viewHolder;
	}

	private void debugLog(String msg) {
		Log.d(getClass().getSimpleName(), msg);
	}
}
