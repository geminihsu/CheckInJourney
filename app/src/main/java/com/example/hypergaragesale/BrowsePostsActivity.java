package com.example.hypergaragesale;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static com.example.util.Utils.decodeSampledBitmapFromResource;


public class BrowsePostsActivity extends AppCompatActivity implements PostsAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<BrowsePosts> data = new ArrayList<BrowsePosts>();
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffFEBB31"));
        toolbar.setBackgroundDrawable(colorDrawable);



        mRecyclerView = (RecyclerView) findViewById(R.id.posts_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        PostsDbHelper mDbHelper = new PostsDbHelper(this);
        db = mDbHelper.getReadableDatabase();
        data = getDataSet();
        mAdapter = new PostsAdapter(data,this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
               // startActivity(new Intent(getApplicationContext(), NewPostActivity.class));
                Intent question = new Intent(BrowsePostsActivity.this, NewPostActivity.class);
                startActivity(question);
                finish();
            }
        });

        mRecyclerView.setAdapter(new PostsAdapter(getDataSet(), new PostsAdapter.OnItemClickListener() {
            @Override public void onItemClick(BrowsePosts item) {
                //zToast.makeText(BrowsePostsActivity.this, item.mTitle, Toast.LENGTH_LONG).show();

                Intent question = new Intent(BrowsePostsActivity.this, PostInfoActivity.class);
                Bundle b = new Bundle();
                //b.putSerializable(PostInfoActivity.ARG_POST_DATA,item);
                b.putString(PostInfoActivity.ARG_POST_TITLE,item.mTitle);
                b.putString(PostInfoActivity.ARG_POST_PRICE,item.mPrice);
                b.putString(PostInfoActivity.ARG_POST_DESCRIPTION,item.mDescription);
                b.putString(PostInfoActivity.ARG_POST_IMAGE_PATH,item.mPictureContent);
                b.putString(PostInfoActivity.ARG_POST_LOCATION,item.mAddress);
                question.putExtras(b);
                startActivity(question);

            }
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();

        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<BrowsePosts> getDataSet() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Posts.PostEntry.COLUMN_NAME_TITLE,
                Posts.PostEntry.COLUMN_NAME_PRICE,
                Posts.PostEntry.COLUMN_NAME_DESCRIPTION,
                Posts.PostEntry.COLUMN_NAME_PICTURE_CONTENT,
                Posts.PostEntry.COLUMN_NAME_LOCATION
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Posts.PostEntry.COLUMN_NAME_PRICE + " DESC";

        Cursor cursor = db.query(
                Posts.PostEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        Bitmap pic = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        ArrayList<BrowsePosts> browsePosts = new ArrayList<BrowsePosts>();
        int i=1;
        if (cursor.moveToFirst()) {
            do {
                BrowsePosts post = new BrowsePosts(String.valueOf(i+"."),
                        cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_TITLE)),
                        cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_PICTURE_CONTENT)),View.GONE);
                String path= cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_PICTURE_CONTENT));

                pic=decodeSampledBitmapFromResource(path,100, 100);
                post.mBitmap = pic;
                browsePosts.add(post);
                i++;
            } while (cursor.moveToNext());
        }

        return browsePosts;
    }


    @Override
    public void onItemClick(BrowsePosts item) {

    }
}
