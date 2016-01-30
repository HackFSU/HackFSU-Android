package com.hackfsu.android.hackfsu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

import com.parse.ParseFile;
import com.parse.ParseImageView;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by andrewsosa on 12/23/15.
 */
public class CustomParseImageView extends ParseImageView {

    ParseFile file;

    public CustomParseImageView(Context context) {
        super(context);
    }

    public CustomParseImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomParseImageView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    public void setParseFile(ParseFile file) {
        this.file = file;
        super.setParseFile(this.file);
    }

    @Override
    public Task<byte[]> loadInBackground() {

        if (file == null) {
            return Task.forResult(null);
        }

        final ParseFile loadingFile = file;
        return file.getDataInBackground().onSuccessTask(new Continuation<byte[], Task<byte[]>>() {
            @Override
            public Task<byte[]> then(Task<byte[]> task) throws Exception {
                byte[] data = task.getResult();
                if (file != loadingFile) {
                    // This prevents the very slim chance of the file's download finishing and the callback
                    // triggering just before this ImageView is reused for another ParseObject.
                    return Task.cancelled();
                }
                if (data != null) {
                    //Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Bitmap bitmap = decodeSampledBitmapFromResource(data, 0, data.length, 512, 512);
                    if (bitmap != null) {
                        setImageBitmap(bitmap);
                    }
                }
                return task;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(byte[] data, int offset, int length,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, offset, length, options);
    }
}


