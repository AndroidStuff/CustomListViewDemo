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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.customlistviewdemo.adapter.MovieListAdapter;
import com.example.customlistviewdemo.app.AppGovernment;
import com.example.customlistviewdemo.base.BaseActivity;
import com.example.customlistviewdemo.model.Movie;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends BaseActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String MOVIES_JSON_URL = "http://api.androidhive.info/json/movies.json";
	private static final String MOVIES_SAMPLE_DATA_JSON = "movies-sample-data.json";
	private List<Movie> movieList = new ArrayList<Movie>();
	private ListView movieListView;
	private MovieListAdapter adapter;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		movieList = fetchSampleMoviesData();
		movieListView = (ListView) findViewById(R.id.list);

		progressDialog = new ProgressDialog(this);
		// Showing progress dialog before making HTTP request
		progressDialog.setMessage("Loading...");
		progressDialog.show();

		// Creating volley request obj
		JsonArrayRequest movieReq = makeVolleyRequestObjectForMovies();
		AppGovernment.getInstance().addToRequestQueue(movieReq);
	}

	private JsonArrayRequest makeVolleyRequestObjectForMovies() {
		JsonArrayRequest movieReq = new JsonArrayRequest(MOVIES_JSON_URL, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				Log.d(TAG, response.toString());
				hideProgressDialog();

				// Parsing JSON
				for (int i = 0; i < response.length(); i++) {
					try {
						JSONObject obj = response.getJSONObject(i);
						JSONArray genreArry = obj.getJSONArray("genre");
						ArrayList<String> genre = new ArrayList<String>();
						for (int j = 0; j < genreArry.length(); j++) {
							genre.add((String) genreArry.get(j));
						}
						Movie movie = new Movie(obj.getString("title"),
								obj.getString("image"),
								obj.getInt("releaseYear"),
								((Number) obj.get("rating")).doubleValue(),
								genre);
						movieList.add(movie);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				//Notify list adapter about data changes to refresh ListView with updated data
				adapter.notifyDataSetChanged();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());
				hideProgressDialog();

			}
		});
		;

		return movieReq;
	}

	@Override
	protected void onResume() {
		super.onResume();
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		hideProgressDialog();
	}

	private void hideProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

}
