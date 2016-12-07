package com.example.hypergaragesale;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.util.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class PostInfoActivity extends AppCompatActivity {
    private String TAG = PostInfoActivity.class.toString();

    public final static String ARG_POST_TITLE = "title";
    public final static String ARG_POST_PRICE = "price";
    public final static String ARG_POST_DESCRIPTION  = "description";
    public final static String ARG_POST_IMAGE_PATH  = "image_path";
    public final static String ARG_POST_LOCATION  = "location";
    public final static String ARG_POST_DATA  = "post";

    String title,price,description,picture_path,location;
    private TextView titleText;
    private TextView descText;
    private TextView priceText;

    private LinearLayout linearLayout;

    private int scaleWidth;
    private int scaleHeight;


    private ImageView image;
    private String imageContentURI;

    protected MapView mMapView;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);
        //mSavedInstanceState = savedInstanceState;
        mMapView = (MapView) findViewById(R.id.fragment_embedded_map_view_mapview);
        mMapView.onCreate(savedInstanceState);
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

        location = bundle.getString(ARG_POST_LOCATION);


    }


    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        setLister();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    private void findViews()
    {
        titleText = (TextView)findViewById(R.id.textView_title);
        titleText.setText(title);
        descText = (TextView)findViewById(R.id.textView_desc);
        descText.setText(description);
        priceText = (TextView)findViewById(R.id.textView_price);
        priceText.setText(price);
        linearLayout = (LinearLayout) findViewById(R.id.linearMain);
        // Gets to GoogleMap from the MapView and does initialization stuff
        googleMap = mMapView.getMap();
        if(!runtime_permissions())
        {
            Geocoder fwdGeocoder = new Geocoder(this, Locale.US);

            String streetAddress = location;
            List<Address> locations = null;
            try {
                locations = fwdGeocoder.getFromLocationName(streetAddress, 10);
            } catch (IOException e) {}


            setMapView(locations.get(0).getLongitude(),locations.get(0).getLatitude());
        }

        /*image = (ImageView) findViewById(R.id.image);
        try {
            Bitmap   bitmap = Utils.decodeSampledBitmapFromResource(picture_path,300,400);
            // Log.d(TAG, String.valueOf(bitmap));

            //ImageView imageView = (ImageView) findViewById(R.id.imageView);
           // Log.e(TAG,uri.toString());
            image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String[] images = picture_path.split(",");
        if(images.length>0)
        {
            for (String path : images)
            {
                Bitmap   bitmap = Utils.decodeSampledBitmapFromResource(path,300,400);
                setUploadImageView(bitmap);
            }

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

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            try {
                mMapView.onDestroy();
            } catch (NullPointerException e) {
                Log.e(TAG, "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }


    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }
    public void setMapView(double longitude,double latitude) {
        if (mMapView != null) {

            googleMap = mMapView.getMap();

            //Creating a LatLng Object to store Coordinates
            LatLng latLng = new LatLng(latitude, longitude);

            //Adding marker to map
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng) //setting position
                    .draggable(true) //Making the marker draggable
                    .title("Current Location")); //Adding a title
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(this);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(latitude, longitude));

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
           // if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Geocoder fwdGeocoder = new Geocoder(this, Locale.US);

                String streetAddress = location;
                List<Address> locations = null;
                try {
                    locations = fwdGeocoder.getFromLocationName(streetAddress, 10);
                } catch (IOException e) {}


                setMapView(locations.get(0).getLongitude(),locations.get(0).getLatitude());

            }else {
                runtime_permissions();
           // }
        }
    }

    private void setUploadImageView(Bitmap bitmap)
    {
        image= new ImageView(getApplicationContext());
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        image.setLayoutParams(layoutParams);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setPadding(0, 0, 0, 10);
        image.setAdjustViewBounds(true);
        image.setImageBitmap(bitmap);

        linearLayout.addView(image);
    }
}
