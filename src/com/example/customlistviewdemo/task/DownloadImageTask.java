package com.example.customlistviewdemo.task;

import java.io.InputStream;

import com.example.customlistviewdemo.listener.OnDownloadImageListener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * This class is a slightly modified version of one found at
 * https://github.com/krusevdespark/katwalk/blob/
 * 559971cc3b062c17e0b09c1bf335342acbe3543b/rggarb/src/net/shiftinpower/
 * asynctasks/DownloadImage.java
 **/
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

	private static final int INITIAL_BITMAP_RESAMPLE_SIZE = 2;
	private static final int HARDER_BITMAP_RESAMPLE_SIZE = 4;
	private Bitmap imageBitmap;
	private String imageUrl;
	private OnDownloadImageListener listener;
	private BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

	public DownloadImageTask(OnDownloadImageListener listener, String imageUrl) {
		this.listener = listener;
		this.imageUrl = imageUrl;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {

		try {

			InputStream in = new java.net.URL(imageUrl).openStream();

			// ideally, the downloaded image size should be small.
			// But, if it were to be BIG, we'll resample it
			// If it's still bigger, we'll resample it harder again
			try {
				imageBitmap = BitmapFactory.decodeStream(in);
			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
				try {
					bitmapOptions.inSampleSize = INITIAL_BITMAP_RESAMPLE_SIZE;
					imageBitmap = BitmapFactory.decodeStream(in, null, bitmapOptions);
				} catch (OutOfMemoryError ex2) {
					ex2.printStackTrace();
					bitmapOptions.inSampleSize = HARDER_BITMAP_RESAMPLE_SIZE;
					imageBitmap = BitmapFactory.decodeStream(in, null, bitmapOptions);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return imageBitmap;
	} // End of doInBackground

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if (listener == null) {
			return;
		}
		if (result != null) {
			listener.onDownloadImageSuccess(result, imageUrl);
		} else {
			listener.onDownloadImageFailure();
		}
	}

}
