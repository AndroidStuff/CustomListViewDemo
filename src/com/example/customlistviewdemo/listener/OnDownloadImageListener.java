package com.example.customlistviewdemo.listener;

import android.graphics.Bitmap;

public interface OnDownloadImageListener {
	void onDownloadImageSuccess(Bitmap image, String imageUrl);

	void onDownloadImageFailure(String imageUrl);
}
