package com.kun.cityguide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kun.cityguide.MainActivity;
import com.kun.cityguide.R;
import com.kun.cityguide.googleplaces.Constants;
import com.kun.cityguide.googleplaces.models.Place;
import com.kun.cityguide.util.ImageOptionsBuilder;
import com.kun.cityguide.util.UtilView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dobrikostadinov on 4/21/15.
 */
public class AdapterPlaces extends RecyclerView.Adapter<AdapterPlaces.ViewHolder> {

    private final DisplayImageOptions mDisplayImageOptions;
    private final ImageLoader mImageLoader;
    private final Context mContext;
    private final Constants.PLACE_TYPES mType;
    private LayoutInflater mLayoutInflater;
    private List<Place> mAdapterData;

    public AdapterPlaces(LayoutInflater layoutInflater, List<Place> adapterData, Context context, Constants.PLACE_TYPES type) {
        mLayoutInflater = layoutInflater;
        mAdapterData = adapterData;

        this.mDisplayImageOptions = ImageOptionsBuilder.buildGeneralImageOptions(true, Constants.PLACE_TYPES.getEmptyImagePlaceHolder(type), false);
        this.mImageLoader = ImageLoader.getInstance();
        this.mContext = context;
        this.mType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater
                .inflate(R.layout.list_item_places, null);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final Place currentPlace = new ArrayList<Place>(mAdapterData).get(i);

//        viewHolder.mImageView.setImageBitmap(null);

//        if (currentPlace.getPhotosUrls().size() > 0) {
            mImageLoader.displayImage(currentPlace.getPhotosUrls().get(0), viewHolder.mImageView, mDisplayImageOptions);
//        } else {
//            viewHolder.mImageView.setBackgroundResource(Constants.PLACE_TYPES.getEmptyImagePlaceHolder(mType));
//        }

        viewHolder.mTextViewTitle.setText(currentPlace.getName());
        viewHolder.mTextViewPriceLevelValue.setText(currentPlace.getPriceLevel(mContext));

        viewHolder.mPriceLevelStarsContainer.removeAllViews();
        if (currentPlace.getPriceLevelValue() > 0) {
            UtilView.getStarsByValue(currentPlace.getPriceLevelValue(), viewHolder.mPriceLevelStarsContainer, getLayoutInflater(), mType);

        }

        viewHolder.mTextViewRatingLevelValue.setText("" + currentPlace.getRatingValue());

        viewHolder.mRatingLevelStarsContainer.removeAllViews();
        if (currentPlace.getRatingValue() > 0) {
            UtilView.getStarsByValue(currentPlace.getRatingValue(), viewHolder.mRatingLevelStarsContainer, getLayoutInflater(), mType);
        }

        viewHolder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).showPlaceDetailFragment(currentPlace, mType);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAdapterData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View mParentView;

        private LinearLayout mPriceLevelStarsContainer;
        private LinearLayout mRatingLevelStarsContainer;
        private ImageView mImageView;
        private TextView mTextViewTitle;
        private TextView mTextViewPriceLevelValue;

        private TextView mTextViewRatingLevelValue;

        public ViewHolder(View itemView) {
            super(itemView);

            mParentView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.list_item_places_image);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.list_item_places_title);
            mTextViewPriceLevelValue = (TextView) itemView.findViewById(R.id.list_item_places_price_level_value);
            mPriceLevelStarsContainer = (LinearLayout) itemView.findViewById(R.id.list_item_places_price_level_stars);
            mRatingLevelStarsContainer = (LinearLayout) itemView.findViewById(R.id.list_item_places_rating_level_stars);
            mTextViewRatingLevelValue = (TextView) itemView.findViewById(R.id.list_item_places_rating_value);
        }
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }


}
