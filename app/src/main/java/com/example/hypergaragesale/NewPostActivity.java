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
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.database.ItemProvider;
import com.example.util.RealPathUtil;
import com.example.util.Utils;

import java.io.File;
import java.io.IOException;


public class NewPostActivity extends AppCompatActivity {
    private String TAG = NewPostActivity.class.toString();
    private static final int CAMERA=1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private SQLiteDatabase db;
    private ContentValues values;

    private EditText titleText;
    private EditText descText;
    private EditText priceText;

    private Button upload;
    private ImageView image;
    private String imageContentURI;

    private int scaleWidth;
    private int scaleHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffFEBB31"));
        myToolbar.setBackgroundDrawable(colorDrawable);
        myToolbar.setTitleTextColor(Color.WHITE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        findViews();
        setLister();

        // Gets the data repository in write mode
        PostsDbHelper mDbHelper = new PostsDbHelper(this);
        db = mDbHelper.getWritableDatabase();
    }

    private void showSnackBar(View v) {
        if (v == null) {
            Snackbar.make(findViewById(R.id.myCoordinatorLayout), R.string.new_post_snackbar,
                    Snackbar.LENGTH_SHORT).show();
        }
        else {
            Snackbar.make(v, R.string.new_post_snackbar,
                    Snackbar.LENGTH_SHORT).show();
        }
    }

//    public void newPostAdded(View v) {
//        addPost();
//    }

    private void findViews()
    {
        titleText = (EditText)findViewById(R.id.textView_title);
        descText = (EditText)findViewById(R.id.textView_desc);
        priceText = (EditText)findViewById(R.id.textView_price);
        upload = (Button) findViewById(R.id.upload);

        image = (ImageView) findViewById(R.id.image);


    }
    private void setLister()
    {
        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(NewPostActivity.this);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        NewPostActivity.this,
                        android.R.layout.select_dialog_item);
                arrayAdapter.add(getString(R.string.pick_picture_camera));
                arrayAdapter.add(getString(R.string.pick_picture_gallery));
                //arrayAdapter.add("YouTube");

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                switch (which){
                                    case 0:
                                        Intent intent = new Intent(NewPostActivity.this, CameraActivity.class);
                                        startActivityForResult(intent, CAMERA);
                                        break;
                                    case 1:
                                        Intent gallery = new Intent();
                                        // Show only images, no videos or anything else
                                        gallery.setType("image/*");
                                        gallery.setAction(Intent.ACTION_GET_CONTENT);
                                        // Always show the chooser (if there are multiple options available)
                                        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE_REQUEST);
                                        break;
                                 /*   case 2:
                                        Intent question = new Intent(NewPostActivity.this, YoutubeActivity.class);
                                        startActivity(question);
                                        break;*/
                                }
                            }
                        });
                builderSingle.show();
            }
        });
    }
    private void addPost() {
        // Create a new map of values, where column names are the keys
        values = new ContentValues();
        values.put(Posts.PostEntry.COLUMN_NAME_TITLE, titleText.getText().toString());
        values.put(Posts.PostEntry.COLUMN_NAME_DESCRIPTION, descText.getText().toString());
        values.put(Posts.PostEntry.COLUMN_NAME_PRICE, priceText.getText().toString());
        values.put(Posts.PostEntry.COLUMN_NAME_PICTURE_CONTENT, imageContentURI);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                Posts.PostEntry.TABLE_NAME,
                null,
                values);

        /*ContentValues values = new ContentValues();
        values.put(ItemProvider.TITLE, titleText.getText().toString());

        values.put(ItemProvider.PRICE, descText.getText().toString());
        values.put(ItemProvider.DESCRIPTION, priceText.getText().toString());
        values.put(ItemProvider.IMAGE_CONTENT, imageContentURI);


        Uri uri = getContentResolver().insert(
                ItemProvider.CONTENT_URI, values);
*/
        //startActivity(new Intent(this, BrowsePostsActivity.class));
        Intent question = new Intent(NewPostActivity.this, BrowsePostsActivity.class);
        startActivity(question);
        finish();
    }

    @Override
    //Get the size of the Image view after the
    //Activity has completely loaded
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        scaleWidth=image.getWidth();
        scaleHeight=image.getHeight();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new_post) {
            showSnackBar(null);
            addPost();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case CAMERA:
                String path=data.getStringExtra("image");
                File imgFile = new File(path);
                if(imgFile.exists()){
                    Bitmap myBitmap = Utils.decodeSampledBitmapFromResource(path,scaleWidth,scaleHeight);
                    //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                    imageContentURI =path;
                    image.setImageBitmap(myBitmap);
                }
                break;
            case PICK_IMAGE_REQUEST:
                String realPath;
                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    bitmap = Utils.decodeSampledBitmapFromResource(realPath,scaleWidth,scaleHeight);
                    // Log.d(TAG, String.valueOf(bitmap));

                    //ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    Log.e(TAG,uri.toString());
                    imageContentURI =realPath;
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }


    }


}
