package com.example.customlistviewdemo.task;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.customlistviewdemo.listener.OnDownloadImageListener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

/**
 * This class is a slightly modified version of one found at
 * https://github.com/krusevdespark/katwalk/blob/
 * 559971cc3b062c17e0b09c1bf335342acbe3543b/rggarb/src/net/shiftinpower/
 * asynctasks/DownloadImage.java
 **/
public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

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
	protected Bitmap doInBackground(Void... params) {
		Log.d(getClass().getSimpleName(), "Downloading image from " + imageUrl);
		InputStream in = null;
		BufferedInputStream bis = null;
		try {
			in = new java.net.URL(imageUrl).openStream();
			bis = new BufferedInputStream(in);
			imageBitmap = processedImage(bis);
			if (in.markSupported() && bis.markSupported()) {
				in.reset();
				bis.reset();
			}
		} catch (Exception ex) {
			Log.e(getClass().getSimpleName(), "Something has gone wrong: " + ex.getMessage());
		} finally {
			close(bis);
			close(in);
		}
		return imageBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if (listener == null) {
			return;
		}
		if (result != null) {
			listener.onDownloadImageSuccess(result, imageUrl);
		} else {
			listener.onDownloadImageFailure(imageUrl);
		}
	}

	/**
	 * Ideally, the downloaded image size should be small. But, if it were to be
	 * BIG, we'll resample it. And, if it's still bigger, we'll resample it
	 * harder again
	 * @return Resized image, if the image is bigger than usual
	 */
	private Bitmap processedImage(BufferedInputStream bis) {
		Bitmap image = null;
		try {
			image = BitmapFactory.decodeStream(bis, null, bitmapOptions);
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
			try {
				bitmapOptions.inSampleSize = INITIAL_BITMAP_RESAMPLE_SIZE;
				image = BitmapFactory.decodeStream(bis, null, bitmapOptions);
			} catch (OutOfMemoryError ex2) {
				ex2.printStackTrace();
				bitmapOptions.inSampleSize = HARDER_BITMAP_RESAMPLE_SIZE;
				image = BitmapFactory.decodeStream(bis, null, bitmapOptions);
			}
		}
		return image;
	}

	private void close(final InputStream is) {
		try {
			is.close();
		} catch (IOException e) {
		}
	}

}
