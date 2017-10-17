package com.example.admin.walmartapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.walmartapplication.data.RemoteDataSource;
import com.example.admin.walmartapplication.model.ImageEntity;
import com.example.admin.walmartapplication.model.Item;
import com.example.admin.walmartapplication.model.WalmartLookup;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyItemListAdapter extends RecyclerView.Adapter<MyItemListAdapter.ViewHolder> {

    private static final String TAG = "MyItemListAdapter";
    Context context;
    List<Item> items = new ArrayList<>();

    public MyItemListAdapter(List<Item> itemList) {
        this.items = itemList;
    }

    @Override
    public MyItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater
                .from( parent.getContext() )
                .inflate( R.layout.list_item, parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(MyItemListAdapter.ViewHolder holder, final int position) {
        Item i = items.get( position );

        holder.item = i;

        Glide.with( context )
                .load( i.getThumbnailImage() )
                .into( holder.image );

        Glide.with( context )
                .load( i.getCustomerRatingImage() )
                .into( holder.rating );

        holder.title.setText( i.getName() );

        holder.price.setText( String.format( "$%,.2f", i.getSalePrice()));
        holder.stock.setText( i.getStock() );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, rating;
        TextView title, price, stock;
        Item item;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: clicked");
                    //Mac's way
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("item", item);
                    context.startActivity(intent);
                }
            });

            image = itemView.findViewById( R.id.ivImage );
            rating = itemView.findViewById( R.id.ivRating );
            title = itemView.findViewById( R.id.tvTitle );
            price = itemView.findViewById( R.id.tvPrice );
            stock = itemView.findViewById( R.id.tvInStock );
        }
    }
}
