package com.kun.cityguide.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kun.cityguide.CityGuideApp;
import com.kun.cityguide.MainActivity;
import com.kun.cityguide.R;
import com.kun.cityguide.adapter.AdapterPlaces;
import com.kun.cityguide.adapter.AdapterSearch;
import com.kun.cityguide.callbacks.CallbackNearbyPlaces;
import com.kun.cityguide.callbacks.CallbackSearchDone;
import com.kun.cityguide.db.DatabaseManager;
import com.kun.cityguide.db.IPreferenceManager;
import com.kun.cityguide.db.PreferenceManager;
import com.kun.cityguide.googleplaces.Constants;
import com.kun.cityguide.googleplaces.NearbyPlacesManager;
import com.kun.cityguide.googleplaces.models.Place;
import com.kun.cityguide.util.Common;
import com.kun.cityguide.util.Constant;
import com.kun.cityguide.util.UtilView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by dobrikostadinov on 6/6/15.
 */
public class FragmentListPlaces extends Fragment implements CallbackNearbyPlaces, CallbackSearchDone {

    public static final String TAG = FragmentListPlaces.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private AdapterPlaces mAdapter;
    private Constants.PLACE_TYPES mPlacesType;
    private View mAbContainer;
    private TextView mAbTitle;
    private View mAbArrow;
    private View mIconMap;
    private AutoCompleteTextView mAutoCompleteSearch;
    private ImageView mAbSearchIcon;
    private float mDensity;
    private List<String> currentTypeAsList;
    private IPreferenceManager manager;

    TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mAdapter = new AdapterPlaces(getActivity().getLayoutInflater(), new ArrayList<>(NearbyPlacesManager.getInstance(getActivity()).performSearchByName(mAutoCompleteSearch.getText().toString(), mPlacesType)), getActivity(), mPlacesType);
                mRecyclerView.setAdapter(mAdapter);

                return true;
            }
            return false;
        }
    };

    public static FragmentListPlaces newInstance(Constants.PLACE_TYPES placesType) {

        FragmentListPlaces fragmentListPlaces = new FragmentListPlaces();
        fragmentListPlaces.mPlacesType = placesType;

        return fragmentListPlaces;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fra_list_places, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDensity = getResources().getDisplayMetrics().density;
        manager = new PreferenceManager(getActivity());

        mAbContainer = getView().findViewById(R.id.fra_list_places_ab_container);
        UtilView.setNoLollipopPadding(mAbContainer, 0, 0);

        if (!UtilView.isLollipop()) {
            UtilView.setViewHeight(mAbContainer, 50);
        }

        mAbContainer.setBackgroundColor(getResources().getColor(mPlacesType.getColourId()));

        mAbArrow = getView().findViewById(R.id.fra_list_places_ab_back_arrow);
        mAbArrow.setOnClickListener(new OnBackClickListener());

        mAbTitle = (TextView) getView().findViewById(R.id.fra_list_places_ab_title);
        mAbTitle.setText(manager.getStringValue(Constant.TOWN));

        mAbSearchIcon = (ImageView) getView().findViewById(R.id.fra_list_places_ab_search);

        mAutoCompleteSearch = (AutoCompleteTextView) getView().findViewById(R.id.fra_list_places_ab_search_et);
        mAutoCompleteSearch.setFocusableInTouchMode(true);


        setSearchEt();

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.fra_places_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mIconMap = getView().findViewById(R.id.fra_list_places_ab_map);
        mIconMap.setOnClickListener(new OnMapClickListener());

        currentTypeAsList = new ArrayList<>();
        currentTypeAsList.add(mPlacesType.getType());

        if (mPlacesType == Constants.PLACE_TYPES.GOOGLE_PLACES_FAV) {
            mAdapter = new AdapterPlaces(getActivity().getLayoutInflater(), new ArrayList<>(DatabaseManager.getInstance().getRealFavPlace()), getActivity(), mPlacesType);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            List<Place> currentPlacesToShow = NearbyPlacesManager.getInstance(getActivity()).getPlacesByType(mPlacesType);

            if (currentPlacesToShow.size() > 0) {
                mAdapter = new AdapterPlaces(getActivity().getLayoutInflater(), currentPlacesToShow, getActivity(), mPlacesType);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                ((MainActivity) getActivity()).showLoadingDialog();

                NearbyPlacesManager.getInstance(getActivity()).addCallback(this);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CityGuideApp application = (CityGuideApp) getActivity().getApplication();
        final Tracker tracker = application.getTracker();
        if (tracker != null) {
            tracker.setScreenName(getClass().getSimpleName());
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    private void setSearchEt() {
        mAutoCompleteSearch.setBackgroundResource(Constants.PLACE_TYPES.getSearchAutoTextBg(mPlacesType));
        mAutoCompleteSearch.setPadding((int) (5 * mDensity), 0, 0, 0);
        IPreferenceManager manager = new PreferenceManager(getActivity());
        mAutoCompleteSearch.setHint(getString(R.string.search_in) + " " + manager.getStringValue(Constant.TOWN));

        mAbSearchIcon.setOnClickListener(new SearchEtClickListener());

        mAutoCompleteSearch.setOnEditorActionListener(mOnEditorActionListener);
    }


    @Override
    public void getSearchResult(final Set<Place> placesResult) {
        AdapterSearch adapterSearch = new AdapterSearch(getActivity(), placesResult);

        mAutoCompleteSearch.setAdapter(adapterSearch);
        adapterSearch.notifyDataSetChanged();

        mAutoCompleteSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List<Place> places = new ArrayList<>(placesResult);

                ((MainActivity) (getActivity())).showNearbyFragment(places.get(position).getPlaceType(), places.get(position), false);
            }
        });
    }

    @Override
    public void showSearchLoading() {

    }

    @Override
    public void hideSearchLoading() {

    }

    @Override
    public void onPlaceClicked(Place place) {

    }

    @Override
    public void onPlacesLoaded(Set<Place> places, Constants.PLACE_TYPES type) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                List<Place> currentPlacesToShow = NearbyPlacesManager.getInstance(getActivity()).getPlacesByType(mPlacesType);

                if (currentPlacesToShow.size() > 0) {
                    ((MainActivity) getActivity()).hideLoadingDialog();

                    mAdapter = new AdapterPlaces(getActivity().getLayoutInflater(), currentPlacesToShow, getActivity(), mPlacesType);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }


    private class OnBackClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            UtilView.hideSoftKeyboard(getActivity(), mAutoCompleteSearch);

            if (mAutoCompleteSearch.getVisibility() == View.VISIBLE) {
                mAutoCompleteSearch.setVisibility(View.GONE);
                mAbTitle.setVisibility(View.VISIBLE);
                mAbTitle.setText(manager.getStringValue(Constant.TOWN));
            } else {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    private class OnMapClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            ((MainActivity) getActivity()).showLoadingDialog();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Common.checkGooglePlayService(getActivity()))
                        ((MainActivity) getActivity()).showNearbyFragment(mPlacesType, null, false);
                    else
                        ((MainActivity) getActivity()).hideLoadingDialog();
                }
            }, 500);

        }
    }

    private class SearchEtClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mAutoCompleteSearch.getVisibility() != View.VISIBLE) {
                mAbTitle.setVisibility(View.GONE);
                mAutoCompleteSearch.setVisibility(View.VISIBLE);

                ((View) mAutoCompleteSearch.getParent()).clearFocus();
                mAutoCompleteSearch.clearFocus();
                mAutoCompleteSearch.requestFocus();
                UtilView.showSoftKeyboard(getActivity(), mAutoCompleteSearch);
            }
        }
    }
}
