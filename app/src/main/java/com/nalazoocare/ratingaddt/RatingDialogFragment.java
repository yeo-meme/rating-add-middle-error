package com.nalazoocare.ratingaddt;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by nalazoo.yeomeme@gmail.com on 2020-05-29
 */
public class RatingDialogFragment extends DialogFragment implements View.OnClickListener {
 public static final String TAG = "RatingDialog";

 private MaterialRatingBar mRatingBar;
    private EditText mRatingText;
    private RatingListener mRatingListener;

    interface RatingListener {
        void onRating(Rating rating);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_rating, container, false);
        mRatingBar = v.findViewById(R.id.restaurant_form_rating);
        mRatingText = v.findViewById(R.id.restaurant_form_text);

        v.findViewById(R.id.restaurant_form_button).setOnClickListener(this);
        v.findViewById(R.id.restaurant_form_cancel).setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof RatingListener) {
            mRatingListener = (RatingListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restaurant_form_button:
                onSubmitClicked(v);
                break;
            case R.id.restaurant_form_cancel:
                onCancelClicked(v);
                break;
        }
    }


    public void onSubmitClicked(View view) {
        Rating rating = new Rating(
                "진우랑미미랑",
                0.6,
                "독스아웃 싸우지말자");

        if (mRatingListener != null) {
            mRatingListener.onRating(rating);
        }

        dismiss();
    }


    public void onCancelClicked(View view) {
        dismiss();
    }

}
