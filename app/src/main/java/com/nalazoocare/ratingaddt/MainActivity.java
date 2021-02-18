package com.nalazoocare.ratingaddt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        EventListener<DocumentSnapshot>,
        RatingDialogFragment.RatingListener {
    private static final String TAG = "RestaurantDetail";

    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";
    private RatingDialogFragment mRatingDialog;

    private ImageView mImageView;
    private TextView mNameView;
    private MaterialRatingBar mRatingIndicator;
    private TextView mNumRatingsView;
    private TextView mCityView;
    private TextView mCategoryView;
    private TextView mPriceView;
    private ViewGroup mEmptyView;
    private RecyclerView mRatingsRecycler;
    private Button button;
    private DocumentReference mRestaurantRef;
    private Query mQuery;

    private Button meme;


    //    interface RatingListener {
//
//        void onRating(Rating rating);
//    }
    private FirestoreAdapter mRatingAdapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.restaurant_image);
        mNameView = findViewById(R.id.restaurant_name);
        mRatingIndicator = findViewById(R.id.restaurant_rating);
        mNumRatingsView = findViewById(R.id.restaurant_num_ratings);
        mCityView = findViewById(R.id.restaurant_city);
        mCategoryView = findViewById(R.id.restaurant_category);
        mPriceView = findViewById(R.id.restaurant_price);
        mEmptyView = findViewById(R.id.view_empty_ratings);
        mRatingsRecycler = findViewById(R.id.recycler_ratings);

        meme = findViewById(R.id.memebutton);

        findViewById(R.id.memebutton).setOnClickListener(this);
        findViewById(R.id.restaurant_button_back).setOnClickListener(this);
        findViewById(R.id.fab_show_rating_dialog).setOnClickListener(this);

        mFirestore = FirebaseFirestore.getInstance();
        mRatingDialog = new RatingDialogFragment();

    }



    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

        if (e != null) {
            Log.w(TAG, "restaurant:onEvent", e);
            return;
        }

//        onRestaurantLoaded()
    }

    @Override
    public void onRating(Rating rating) {

        // In a transaction, add the new rating and update the aggregate totals
        addRating(mRestaurantRef, rating)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Rating added");

                        // Hide keyboard and scroll to top
                        mRatingsRecycler.smoothScrollToPosition(0);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Add rating failed", e);

                        // Show failure message and hide keyboard
                        Snackbar.make(findViewById(android.R.id.content), "Failed to add rating",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });

    }


    private Task<Void> addRating (final DocumentReference restaurantRef, final Rating rating) {
        final DocumentReference ratingRef = restaurantRef.collection("ratings")
                .document();

        return mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                Restaurant restaurant = transaction.get(restaurantRef)
                        .toObject(Restaurant.class);


                int newNumRatings = restaurant.getNumRatings() + 1;
                double oldRatingTotal = restaurant.getAvgRating() * restaurant.getNumRatings();
                double newAvgRating = (oldRatingTotal + rating.getRating()) / newNumRatings;
                restaurant.setNumRatings(newNumRatings);
                restaurant.setAvgRating(newAvgRating);
                transaction.set(restaurantRef, restaurant);
                transaction.set(ratingRef, rating);

                return null;
            }
        });
    }
    public void onBackArrowClicked(View view) {
        onBackPressed();
    }

    public void onAddRatingClicked(View view) {
        mRatingDialog.show(getSupportFragmentManager(), RatingDialogFragment.TAG);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
                case R.id.restaurant_button_back:
                    onBackArrowClicked(v);
                    break;
                case R.id.fab_show_rating_dialog:
                    onAddRatingClicked(v);
                    break;

            case R.id.memebutton :
                Log.d(TAG, "in button");

                mFirestore = FirebaseFirestore.getInstance();
                mQuery = mFirestore.collection("restaurants")
                        .orderBy("avgRating", Query.Direction.DESCENDING)
                        .limit(30);
                CollectionReference restaurants = mFirestore.collection("restaurants");
                // Add a new document with a generated id.
                Restaurant restaurant = new Restaurant();
                restaurant.setName("meme");
                restaurant.setCity("sec");
                restaurant.setCategory("호호");
                restaurant.setPhoto("키키키");
                restaurant.setPrice(3230);
                restaurant.setAvgRating(656565);
                restaurant.setNumRatings(1654);


                restaurants.add(restaurant);
                Log.d(TAG, "in after:" +restaurant.getName());

                break;
        }
    }


}
