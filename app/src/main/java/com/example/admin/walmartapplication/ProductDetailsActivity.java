package com.example.admin.walmartapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.walmartapplication.model.Item;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnTouchListener {

    Item item;
    TextView name, price, modelNumber, rating, stock, shortDescription;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //bind views
        image = findViewById( R.id.ivImage );
        name = findViewById( R.id.tvName );
        price = findViewById( R.id.tvPrice );
        modelNumber = findViewById( R.id.tvModelNumber );
        rating = findViewById( R.id.tvRating );
        stock = findViewById( R.id.tvStock );
        shortDescription = findViewById( R.id.tvShortDescription );

        //set values
        item = getIntent().getParcelableExtra("item");

        name.setText( item.getName() );
        price.setText( String.format( "$%,.2f", item.getSalePrice()));
        modelNumber.setText( item.getModelNumber() );
        rating.setText( String.format( "%.1f", Double.valueOf( item.getCustomerRating() )) + "/5");
        stock.setText( item.getStock() );
        shortDescription.setText( item.getShortDescription() );

        Glide.with( this )
                .load( item.getLargeImage() )
                .into( image );
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
