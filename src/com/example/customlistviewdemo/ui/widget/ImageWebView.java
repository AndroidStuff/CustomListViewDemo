package com.example.customlistviewdemo.ui.widget;

import com.example.customlistviewdemo.listener.OnDownloadImageListener;
import com.example.customlistviewdemo.task.DownloadImageTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * The ListView is recycling views, which means that once you scroll down,
 * the download you triggered for a list item might not apply anymore,
 * as that same list item view has been used to display an item at the bottom of the list, which should have a different image.
 *
 * What you need to do, is set the URL of the image as a tag to your ImageWebView in your setImageUrl method,
 * and then in onImageDownloaded, check if the Url in the tag is still the same as the one you just downloaded.
 * If it's not, it means that your ImageWebView is already being used for a new list item, and you shouldn't set the image.
 * And for this you should also add the downloaded image Url as a parameter to your onImageDownloaded method.
 *
 * Ref.: http://stackoverflow.com/questions/21810821/scrolling-a-listview-with-dynamically-loaded-images-mixes-images-order
 */
public class ImageWebView extends ImageView implements OnDownloadImageListener {

	private Bitmap cachedBitmap;
	private boolean imageBitmapCached = false;

	public ImageWebView(Context context) {
		this(context, null);
	}

	public ImageWebView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ImageWebView(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
	}

	public void setImageUrl(String url, int placeholderResId) {
		String oldUrl = (String) getTag();
		setTag(url);
		if (!url.equals(oldUrl)) {
			setImageResource(placeholderResId);
			new DownloadImageTask(this, url).execute();
		}
	}

	@Override
	public void onDownloadImageFailure(String imageUrl) {
		Log.e(getClass().getSimpleName(), "Failed to download image from the URL " + imageUrl);
	}

	@Override
	public void onDownloadImageSuccess(Bitmap image, String imageUrl) {
		String tag = (String) getTag();
		if (imageUrl.equals(tag)) {
			setImageBitmap(image);
			cachedBitmap = image;
			imageBitmapCached = true;
		}
	};

}
