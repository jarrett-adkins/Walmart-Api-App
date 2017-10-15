package com.example.admin.walmartapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.walmartapplication.model.ImageEntity;
import com.example.admin.walmartapplication.model.Item;

import java.util.ArrayList;
import java.util.List;

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
    public void onBindViewHolder(MyItemListAdapter.ViewHolder holder, int position) {
        Item i = items.get( position );

        Glide.with( context )
                .load( i.getThumbnailImage() )
                .into( holder.image );

        Glide.with( context )
                .load( i.getCustomerRatingImage() )
                .into( holder.rating );

        holder.title.setText( i.getName() );
        holder.price.setText( "$" + i.getSalePrice() );
        // TODO: 10/15/2017 String format to always be $00.00
        holder.stock.setText( i.getStock() );

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, rating;
        TextView title, price, stock;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: clicked");
//                    Intent intent = new Intent(context, ImageActivity.class);
//                    intent.putExtra("start", item);
//                    context.startActivity(intent);
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
