package com.example.hypergaragesale;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.ItemProvider;
import com.example.util.RealPathUtil;
import com.example.util.Utils;

import java.io.File;
import java.io.IOException;

import static com.example.util.Utils.decodeSampledBitmapFromResource;


public class PostInfoActivity extends AppCompatActivity {
    private String TAG = PostInfoActivity.class.toString();

    public final static String ARG_POST_TITLE = "title";
    public final static String ARG_POST_PRICE = "price";
    public final static String ARG_POST_DESCRIPTION  = "description";
    public final static String ARG_POST_IMAGE_PATH  = "image_path";
    public final static String ARG_POST_DATA  = "post";

    String title,price,description,picture_path;
    private TextView titleText;
    private TextView descText;
    private TextView priceText;

    private int scaleWidth;
    private int scaleHeight;


    private ImageView image;
    private String imageContentURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffFEBB31"));
        myToolbar.setBackgroundDrawable(colorDrawable);
        myToolbar.setTitleTextColor(Color.WHITE);
        Bundle bundle = this.getIntent().getExtras();
        title = bundle.getString(ARG_POST_TITLE);
        price = bundle.getString(ARG_POST_PRICE);

        description = bundle.getString(ARG_POST_DESCRIPTION);

        picture_path = bundle.getString(ARG_POST_IMAGE_PATH);



    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        findViews();
        setLister();


    }



    private void findViews()
    {
        titleText = (TextView)findViewById(R.id.textView_title);
        titleText.setText("The Item title:"+title);
        descText = (TextView)findViewById(R.id.textView_desc);
        descText.setText("The Item price:"+price);
        priceText = (TextView)findViewById(R.id.textView_price);
        priceText.setText("The Item description:"+description);
        image = (ImageView) findViewById(R.id.image);
        try {
            Bitmap   bitmap = Utils.decodeSampledBitmapFromResource(picture_path,300,400);
            // Log.d(TAG, String.valueOf(bitmap));

            //ImageView imageView = (ImageView) findViewById(R.id.imageView);
           // Log.e(TAG,uri.toString());
            image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void setLister()
    {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //Get the size of the Image view after the
    //Activity has completely loaded
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        scaleWidth=image.getWidth();
        scaleHeight=image.getHeight();
    }

}
