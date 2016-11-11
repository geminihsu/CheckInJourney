package com.example.hypergaragesale;

import android.graphics.Bitmap;

/**
 * Created by Taral on 3/12/2016.
 */
public class BrowsePosts {
    public String mNumber;
    public String mTitle;
    public String mPrice;
    public String mDescription;
    public String mPictureContent;
    public Bitmap mBitmap;

    public BrowsePosts (String mNumber,String title, String price, String description, String image_content) {
        this.mNumber = mNumber;
        this.mTitle = title;
        this.mPrice = price;
        this.mDescription = description;
        this.mPictureContent = image_content;
    }
}
