package com.example.camera2raw;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureResult;
import android.media.Image;
import android.media.ImageReader;

import java.io.File;

/**
 * Builder class for constructing {@link ImageSaver}s.
 * <p/>
 * This class is thread safe.
 */
public class ImageSaverBuilder {
    private Image mImage;
    private File mFile;
    private CaptureResult mCaptureResult;
    private CameraCharacteristics mCharacteristics;
    private Context mContext;
    private RefCountedAutoCloseable<ImageReader> mReader;

    /**
     * Construct a new ImageSaverBuilder using the given {@link Context}.
     *
     * @param context a {@link Context} to for accessing the
     *                {@link android.provider.MediaStore}.
     */
    public ImageSaverBuilder(final Context context) {
        mContext = context;
    }

    public synchronized ImageSaverBuilder setRefCountedReader(
            RefCountedAutoCloseable<ImageReader> reader) {
        if (reader == null) throw new NullPointerException();

        mReader = reader;
        return this;
    }

    public synchronized ImageSaverBuilder setImage(final Image image) {
        if (image == null) throw new NullPointerException();
        mImage = image;
        return this;
    }

    public synchronized ImageSaverBuilder setFile(final File file) {
        if (file == null) throw new NullPointerException();
        mFile = file;
        return this;
    }

    public synchronized ImageSaverBuilder setResult(final CaptureResult result) {
        if (result == null) throw new NullPointerException();
        mCaptureResult = result;
        return this;
    }

    public synchronized ImageSaverBuilder setCharacteristics(
            final CameraCharacteristics characteristics) {
        if (characteristics == null) throw new NullPointerException();
        mCharacteristics = characteristics;
        return this;
    }

    public synchronized ImageSaver buildIfComplete() {
        if (!isComplete()) {
            return null;
        }
        return new ImageSaver(mImage, mFile, mCaptureResult, mCharacteristics, mContext,
                mReader);
    }

    public synchronized String getSaveLocation() {
        return (mFile == null) ? "Unknown" : mFile.toString();
    }

    private boolean isComplete() {
        return mImage != null && mFile != null && mCaptureResult != null
                && mCharacteristics != null;
    }
}