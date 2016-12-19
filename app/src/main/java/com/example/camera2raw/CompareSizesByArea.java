package com.example.camera2raw;

// Utility classes and methods:
// *********************************************************************************************

import android.util.Size;

import java.util.Comparator;

/**
 * Comparator based on area of the given {@link Size} objects.
 */
 class CompareSizesByArea implements Comparator<Size> {

    @Override
    public int compare(Size lhs, Size rhs) {
        // We cast here to ensure the multiplications won't overflow
        return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                (long) rhs.getWidth() * rhs.getHeight());
    }

}

