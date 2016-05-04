package com.example.customlistviewdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.customlistviewdemo.adapter.MovieListAdapter;
import com.example.customlistviewdemo.base.BaseActivity;
import com.example.customlistviewdemo.model.Movie;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends BaseActivity {

	private static final String MOVIES_SAMPLE_DATA_JSON = "movies-sample-data.json";
	private List<Movie> movieList = new ArrayList<Movie>();
	private ListView movieListView;
	private MovieListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		movieList = fetchSampleMoviesData();
		movieListView = (ListView) findViewById(R.id.list);
		adapter = new MovieListAdapter(this, movieList);
		movieListView.setAdapter(adapter);
	}

	private List<Movie> fetchSampleMoviesData() {
		final ArrayList<Movie> movies = new ArrayList<Movie>(0);
		String strMovies = readFileFromAssets(MOVIES_SAMPLE_DATA_JSON);
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(strMovies);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				movies.add(new Movie(jsonObject));
			}
		} catch (JSONException e) {
			Log.e(getClass().getSimpleName(), "Error converting JSON to Java Object. " + e.getMessage());
			throw new RuntimeException(e);
		}
		return movies;
	}

	private String readFileFromAssets(String fileName) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder("");
		String line;
		try {
			final InputStream fileStream = getAssets().open(fileName);
			reader = new BufferedReader(new InputStreamReader(fileStream));
			while (true) {
				line = reader.readLine();
				if (line == null)
					break;
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (IOException e) {
			Log.e(getClass().getSimpleName(), "Error loading image from disk. " + e.getMessage());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		return "";
	}
}
