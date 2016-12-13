package com.example.hypergaragesale;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Taral on 3/12/2016.
 */
public class BrowsePosts implements Cloneable ,Serializable {
    private static final long serialVersionUID = 7185630974835115584L;
    public String mID;
    public String mTitle;
    public String mPrice;
    public String mMoodRating;
    public String mDescription;
    public String mPictureContent;
    public String mAddress;
    public Bitmap mBitmap;
    public Integer checkBox_visibility;

    public BrowsePosts (String mID, String title, String price, String moodRating, String description, String address, String image_content, Integer ischeck) {
        this.mID = mID;
        this.mTitle = title;
        this.mPrice = price;
        this.mMoodRating = moodRating;
        this.mDescription = description;
        this.mPictureContent = image_content;
        this.mAddress = address;
        this.checkBox_visibility = ischeck;
    }

    public BrowsePosts clone()  {
        try {
            return (BrowsePosts) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
