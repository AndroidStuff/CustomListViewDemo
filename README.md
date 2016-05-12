# CustomListViewDemo

Demo the use of ListView with List Row data having a mixed of text and image.

## Learning Objective
I want to do it the raw way without using any third party-libraries for image downloading to drive home some lessons on how ListView works. See `Lessons Learned` section for more on what I observed/learned.

## Tools Used

* [Picasso](http://square.github.io/picasso/). This is one heck of an uber cool tool that comes handy to the developer to relieve him of all ailments related to image download. You wouldn't want to miss this ;)
* You can set `picasso = Picasso.with(activity); picasso.setIndicatorsEnabled(true);` to know if an image is downloaded from Network/Disk/Memory by way of color code Red/Blue/Green respectively displayed in the image's top left corner as respective-color-filled-triangle.


## Lessons Learned 
* ListView adaptor's getView() like crazy. It's out-rightly unpredictable. Thank you google for making thedeveloper's life hell :(
* Writing a custom ListView adaptor. Then it's common to apply the ViewHolder Pattern to avoid calling findViewById() repetitively. For the number of times the adaptor's getView() getting called, it's a terrible idea to parse through the view hierarchy (albeit small) every single time.
* The `DownloadImageTask` class should serve as a good reference for me if I were to implement a simple image download class again.
* Here is the thing that makes life of a developer all the more miserable. There is no gurantee as to when getView() is invoked with null or non-null convertView/rowView parameter. This makes the current project with open bugs wherein not all images are displayed on scroll of ListView. Some images on new Rows in ListView don't show-up on scroll because getView() is invoked with not-null convertView/rowView parameter. This happens because of ListView adaptor's recycling views technique adopted by Google. But our code implementation downloads image only when the url of the ImageView matches with the url passed to it. For this technique to be adopted, getView() should be called without any convertView/rowView parameter.
* Life is smoother and easier to choose a 3rd party library to solve this problem that re-invent the wheel on our own. I'd however like to continue to try solving this in my own way to quench my intellectual curiosity ;-) 

NOTE: My above observations are based on running this project on GenyMotion's emulator.

## References
* [Android Custom ListView with Image and Text using Volley](http://www.androidhive.info/2014/07/android-custom-listview-with-image-and-text-using-volley/)
* [Android ListView Optimizations Part 1: The ViewHolder](https://dzone.com/articles/android-listview-optimizations)
* [Android ListView Optimizations Part 2: Displaying Images in Lists](https://dzone.com/articles/android-listview-optimizations-0)
* [Using lists in Android (ListView) - Tutorial](http://www.vogella.com/tutorials/AndroidListView/article.html)
* [An Introduction to Volley](http://code.tutsplus.com/tutorials/an-introduction-to-volley--cms-23800)
* [How To Create A Custom Listview - Android Example](http://androidexample.com/How_To_Create_A_Custom_Listview_-_Android_Example/index.php?view=article_discription&aid=67&aaid=92)
* [Multithreading For Performance](http://android-developers.blogspot.in/2010/07/multithreading-for-performance.html)
* [Android – Asynchronous image loading in ListView](http://www.technotalkative.com/android-asynchronous-image-loading-in-listview/)
* `DownloadImage.java` and `OnDownloadImageListener.java` classes are shamelessly copy-pasted from the project [krusevdespark/katwalk/rggarb](https://github.com/krusevdespark/katwalk/tree/559971cc3b062c17e0b09c1bf335342acbe3543b/rggarb)
* [SO's QnA - Scrolling a listview with dynamically loaded images mixes images order](http://stackoverflow.com/questions/21810821/scrolling-a-listview-with-dynamically-loaded-images-mixes-images-order)
* [JSON URL for Sample Data](http://api.androidhive.info/json/movies.json)
* [Android Adapter Good Practices](http://www.piwai.info/android-adapter-good-practices/)