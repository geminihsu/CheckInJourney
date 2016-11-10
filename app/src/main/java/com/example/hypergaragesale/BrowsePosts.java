package com.example.hypergaragesale;

/**
 * Created by Taral on 3/12/2016.
 */
public class BrowsePosts {
    public String mTitle;
    public String mPrice;
    public String mDescription;
    public String mPictureContent;

    public BrowsePosts (String title, String price, String description, String image_content) {
        this.mTitle = title;
        this.mPrice = price;
        this.mDescription = description;
        this.mPictureContent = image_content;
    }
}
