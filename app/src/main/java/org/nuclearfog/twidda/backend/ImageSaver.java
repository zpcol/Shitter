package org.nuclearfog.twidda.backend;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import org.nuclearfog.twidda.activity.MediaViewer;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * this class is for saving images into storage
 */
public class ImageSaver extends AsyncTask<Void, Void, ImageSaver.ImageStat> {

    public enum ImageStat {
        IMAGE_SAVE_SUCCESS,
        IMAGE_SAVE_FAILED,
        IMAGE_DUPLICATE
    }

    private final Bitmap image;
    private final String link;
    private final WeakReference<MediaViewer> callback;
    private File imageFile;


    public ImageSaver(MediaViewer activity, Bitmap image, String link) {
        this.callback = new WeakReference<>(activity);
        this.image = image;
        this.link = link;
        // path where images to save
        imageFile = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
    }


    @Override
    protected ImageStat doInBackground(Void... v) {
        try {
            String name = "shitter_" + link.substring(link.lastIndexOf('/') + 1);
            imageFile = new File(imageFile, name);
            if (imageFile.exists())
                return ImageStat.IMAGE_DUPLICATE;
            FileOutputStream imageWrite = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, imageWrite);
            imageWrite.flush();
            imageWrite.close();
            return ImageStat.IMAGE_SAVE_SUCCESS;
        } catch (Exception err) {
            err.printStackTrace();
        }
        return ImageStat.IMAGE_SAVE_FAILED;
    }


    @Override
    protected void onPostExecute(ImageStat status) {
        if (callback.get() != null) {
            callback.get().onImageSaved(status, imageFile.toString());
        }
    }
}