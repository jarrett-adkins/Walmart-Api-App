package com.example.admin.walmartapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.walmartapplication.data.RemoteDataSource;
import com.example.admin.walmartapplication.model.Item;
import com.example.admin.walmartapplication.model.WalmartLookup;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText queryView;
    Button searchButton;
    TextView status;
    RecyclerView queryList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;
    MyItemListAdapter myItemListAdapter;
    List<Item> items = new ArrayList<>();
    int start = 1;
    String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queryView = findViewById( R.id.etQuery );
        searchButton = findViewById( R.id.btnSearch );
        status = findViewById( R.id.tvStatus );
        queryList = findViewById( R.id.rvQueryList );

        layoutManager = new LinearLayoutManager(this);
        itemAnimator = new DefaultItemAnimator();
        queryList.setLayoutManager(layoutManager);
        queryList.setItemAnimator(itemAnimator);
        queryList.addOnScrollListener( new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                Toast.makeText(MainActivity.this, "onLoadMore", Toast.LENGTH_SHORT).show();
                start+=25;
                nextPage( totalItemsCount );
            }
        });
    }

    public void search(View view) {
        closeKeyboard();
//        String query = queryView.getText().toString();
        query = queryView.getText().toString();

        //reset everything
        items = new ArrayList<>();
        queryList.setAdapter( null );
        start = 1;

        RemoteDataSource.getWalmartLookup( query, start )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribeOn(  Schedulers.io() )
                .subscribe(new Observer<WalmartLookup>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        status.setText( "Searching...");
                    }

                    @Override
                    public void onNext(@NonNull WalmartLookup walmartLookup) {
                        Log.d(TAG, "onNext: ");
                        Log.d(TAG, "onNext: query: " + walmartLookup.getQuery() );
                        Log.d(TAG, "onNext: total results: " + walmartLookup.getTotalResults() );
                        Log.d(TAG, "onNext: get start: " + walmartLookup.getStart());

                        if( walmartLookup.getTotalResults() != 0 ) {
                            Log.d(TAG, "onNext: items count: " + walmartLookup.getItems().size());
                            items = walmartLookup.getItems();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        status.setText( "No Results.");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");

                        myItemListAdapter = new MyItemListAdapter( items );
                        queryList.setAdapter( myItemListAdapter );

                        String s = (items.size() > 0) ? "" : "No Results";
                        status.setText( s );
                    }
                });
    }

    public void nextPage( final int totalItemsCount ) {
        RemoteDataSource.getWalmartLookup( query, start )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribeOn(  Schedulers.io() )
                .subscribe(new Observer<WalmartLookup>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        status.setText( "Searching...");
                    }

                    @Override
                    public void onNext(@NonNull WalmartLookup walmartLookup) {
                        for( Item i : walmartLookup.getItems() )
                            items.add( i );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "nextPage: onError: " + e.toString());
                        Log.d(TAG, "nextPage: onError: " + query + " " + start);
                        status.setText( "No Results.");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");

                        myItemListAdapter.notifyItemRangeChanged( start, totalItemsCount);
                    }
                });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //keeps the form form getting wiped when onDestroy gets called, such as when the phone is rotated.
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
//        outState.putString("query", queryView.getText().toString());
        outState.putString("query", query);
        outState.putInt("start", start);

        ArrayList<Item> itemArrayList = (ArrayList<Item>) items;
        outState.putParcelableArrayList( "itemArray", itemArrayList);

//        Log.d(TAG, "onSaveInstanceState: query=" + query);
//        Log.d(TAG, "onSaveInstanceState: start=" + start);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: ");

//        closeKeyboard();
        queryView.setText( savedInstanceState.getString( "query" ));
        query = savedInstanceState.getString( "query" );
        start = savedInstanceState.getInt( "start" );

        ArrayList<Item> itemArrayList = savedInstanceState.getParcelableArrayList( "itemArray" );

        //if we got something back from the instance
        if( !itemArrayList.isEmpty() ) {
            items = itemArrayList;
            myItemListAdapter = new MyItemListAdapter(items);
            queryList.setAdapter(myItemListAdapter);
        }

//        Log.d(TAG, "onRestoreInstanceState: query=" + query);
//        Log.d(TAG, "onRestoreInstanceState: start=" + start);
    }

    public void closeKeyboard() {
        View v = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}

/*
X 1. First screen should contain a List of all the products returned by the Service call with images.
X 2. The list should support Lazy Loading. When scrolled to the bottom of the list, start lazy loading
     next page of products and append it to the list.
X 3. When a product is clicked, it should go to the second screen.
4. Second screen should display details of the product.
5. Ability to swipe to view next/previous item ( Eg: Gmail App)
X BONUS : Handling orientation changes efficiently will be a plus.
 */
