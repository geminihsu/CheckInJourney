package com.example.hypergaragesale;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Taral on 3/11/2016.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(BrowsePosts item);
    }

    private ArrayList<BrowsePosts> mDataset;
    private final OnItemClickListener listener;
   // private final PublishSubject<BrowsePosts> onClickSubject = PublishSubject.create();
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView number;
        public ImageView picture;
        public TextView mTitle;
        public TextView mPrice;
        public TextView mDetail;
        public ViewHolder(View view) {
            super(view);
            picture = (ImageView) itemView.findViewById(R.id.imageView);
            number = (TextView) itemView.findViewById(R.id.number);
            mTitle = (TextView) itemView.findViewById(R.id.titleView);
            mPrice = (TextView) itemView.findViewById(R.id.priceView);
            mDetail = (TextView) itemView.findViewById(R.id.detail);
            // Implement view click Listener when make each row of RecycleView clickable

        }
       public void bind(final BrowsePosts item, final OnItemClickListener listener) {

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override public void onClick(View v) {
                   listener.onItemClick(item);
               }
           });
       }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostsAdapter(ArrayList<BrowsePosts> myDataset, OnItemClickListener listener) {
        mDataset = myDataset;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get elements from your dataset at this position
        // - replace the contents of the views with that elements
        holder.bind(mDataset.get(position), listener);
        //DisplayImage function from ImageLoader Class
        //imageLoader.DisplayImage(mDataset.get(position).mBitmap, holder.picture);
        holder.picture.setImageBitmap(mDataset.get(position).mBitmap);
        holder.number.setText(mDataset.get(position).mNumber);
        holder.mTitle.setText(mDataset.get(position).mTitle);
        holder.mPrice.setText(mDataset.get(position).mPrice);
        holder.mDetail.setText(mDataset.get(position).mDescription);

      /*  final BrowsePosts element = mDataset.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(element);
            }
        });*/
}

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

  /*  public Observable<BrowsePosts> getPositionClicks(){
        return onClickSubject.asObservable();
    }*/




}


