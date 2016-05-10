package com.example.customlistviewdemo.task;

import com.example.customlistviewdemo.listener.OnDownloadImageListener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
	private int maxImageWidth;
	private int maxImageHeight;

	public DownloadImageTask(OnDownloadImageListener listener, String imageUrl, int reqHeight, int reqWidth) {
		this.listener = listener;
		this.imageUrl = imageUrl;
		this.maxImageHeight = reqHeight;
		this.maxImageWidth = reqWidth;
	}

	@Override
	protected void onCancelled(Bitmap result) {
		super.onCancelled(result);
		debugLog("Task cancelled for image download from url : " + imageUrl);
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		debugLog("Start downloading image from " + imageUrl);
		if (isCancelled()) return null;
		int imageSizeRatio = computeImageSizeRatio(imageUrl);
		if (isCancelled()) return null;
		imageBitmap = downloadAndProcessImage(imageUrl, imageSizeRatio);
		debugLog("Done downloading image from " + imageUrl);
		return imageBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if (isCancelled()) return;
		if (listener == null) return;

		if (result != null) {
			listener.onDownloadImageSuccess(result, imageUrl);
		} else {
			listener.onDownloadImageFailure(imageUrl);
		}
	}

	private int computeImageSizeRatio(String url) {
		final OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(imageUrl).build();
		Response response = null;
		try {
			if (isCancelled()) return 1;
			response = client.newCall(request).execute();
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(response.body().byteStream(), null, bitmapOptions);
			response.body().close();
			return calculateSampleSize(bitmapOptions, maxImageHeight, maxImageWidth);
		} catch (Exception ex) {
			errorLog("Something has gone wrong: " + ex.getMessage());
			return 1;
		}
	}

	/**
	 * Ideally, the downloaded image size should be small. But, if it were to be
	 * BIG, we'll resample it. And, if it's still bigger, we'll resample it
	 * harder again
	 * @return Resized image, if the image is bigger than usual
	 */
	private Bitmap downloadAndProcessImage(String imageUrl, int sizeRatio) {
		final OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(imageUrl).build();
		Response response = null;
		Bitmap image = null;
		try {
			response = client.newCall(request).execute();
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize = sizeRatio;
			bitmapOptions.inJustDecodeBounds = false;
			image = BitmapFactory.decodeStream(response.body().byteStream(), null, bitmapOptions);
			response.body().close();
		} catch (Exception ex) {
			errorLog("Something has gone wrong: " + ex.getMessage());
		}
		return image;
	}

	private int calculateSampleSize(Options options, int reqHeight, int reqWidth) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		debugLog("Size of image to download is " + height + "x" + width);
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

	private void debugLog(String msg) {
		Log.d(getClass().getSimpleName(), msg);
	}

	private void errorLog(String msg) {
		Log.e(getClass().getSimpleName(), msg);
	}

}
