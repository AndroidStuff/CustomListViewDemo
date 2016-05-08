package com.example.customlistviewdemo.task;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.customlistviewdemo.listener.OnDownloadImageListener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.util.Log;

/**
 * This class is a slightly modified version of one found at
 * https://github.com/krusevdespark/katwalk/blob/
 * 559971cc3b062c17e0b09c1bf335342acbe3543b/rggarb/src/net/shiftinpower/
 * asynctasks/DownloadImage.java
 **/
public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

	private Bitmap imageBitmap;
	private String imageUrl;
	private OnDownloadImageListener listener;
	private BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	private int maxImageWidth;
	private int maxImageHeight;

	public DownloadImageTask(OnDownloadImageListener listener, String imageUrl, int reqHeight, int reqWidth) {
		this.listener = listener;
		this.imageUrl = imageUrl;
		this.maxImageHeight = reqHeight;
		this.maxImageWidth = reqWidth;
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		Log.d(getClass().getSimpleName(), "Downloading image from " + imageUrl);
		computeImageSizeRatio(imageUrl);
		imageBitmap = processedImage(imageUrl);
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

	private void computeImageSizeRatio(String url) {
		InputStream in = null;
		BufferedInputStream bis = null;
		try {
			in = new java.net.URL(imageUrl).openStream();
			bis = new BufferedInputStream(in);
			bitmapOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(bis, null, bitmapOptions);
			bitmapOptions.inSampleSize = calculateSampleSize(bitmapOptions, maxImageHeight, maxImageWidth);
			bitmapOptions.inJustDecodeBounds = false;
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
	}

	/**
	 * Ideally, the downloaded image size should be small. But, if it were to be
	 * BIG, we'll resample it. And, if it's still bigger, we'll resample it
	 * harder again
	 * @return Resized image, if the image is bigger than usual
	 */
	private Bitmap processedImage(String imageUrl) {
		Bitmap image = null;
		InputStream in = null;
		BufferedInputStream bis = null;
		try {
			in = new java.net.URL(imageUrl).openStream();
			bis = new BufferedInputStream(in);
			bitmapOptions.inJustDecodeBounds = false;
			image = BitmapFactory.decodeStream(bis, null, bitmapOptions);
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
		return image;
	}

	private int calculateSampleSize(Options options, int reqHeight, int reqWidth) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		Log.d(getClass().getSimpleName(), "Size of image to download is " + height + "x" + width);
		int inSampleSize = 1;
		if (width > reqWidth || height > reqHeight) {
			if (width > height) {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			} else {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			}
		}
		return inSampleSize;
	}

	private void close(final InputStream is) {
		try {
			is.close();
		} catch (IOException e) {
		}
	}

}
