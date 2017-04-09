package com.einstens3.ironchef.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.einstens3.ironchef.R;
import com.einstens3.ironchef.RecipeRecyclerAdapter;
import com.einstens3.ironchef.Utilities.EndlessRecyclerViewScrollListener;
import com.einstens3.ironchef.Utilities.NetworkClient;
import com.einstens3.ironchef.models.EdamamRecipe;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class HomeActivityFragment extends Fragment {
    @BindView(R.id.rvRecipies)
    RecyclerView rvRecipies;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    protected ArrayList<EdamamRecipe> mArrayList;
    protected RecipeRecyclerAdapter mArrayAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private GridLayoutManager gridLayoutManager;

    public HomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        rvRecipies.setAdapter(mArrayAdapter);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvRecipies.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvRecipies.addItemDecoration(itemDecoration);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makeNetworkCall(0);
            }
        });


        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code     is needed to append new items to the bottom of the list
                final int pageNo = page;
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        makeNetworkCall(pageNo);

                    }
                });
            }
        };
        // Adds the scroll listener to RecyclerView
        rvRecipies.addOnScrollListener(scrollListener);
        makeNetworkCall(0);
        return v;
    }

    public void makeNetworkCall(final int pageNo) {


        NetworkClient.getRecipe("samosa", pageNo * 20, (pageNo * 20) + 20, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray arrayList = response.getJSONArray("hits");
                    ArrayList a = EdamamRecipe.fromJSONArray(arrayList);
                    if (pageNo == 0) {
                        mArrayList = mArrayAdapter.swap(a);
                    } else {
                        mArrayList = mArrayAdapter.add(a);
                    }
                    Log.d("debug", response.toString());
                } catch (Exception ex) {

                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("debug", errorResponse.toString());
                swipeContainer.setRefreshing(false);
                // fragmentListener.onLoading(false);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArrayList = new ArrayList<>();
        mArrayAdapter = new RecipeRecyclerAdapter(getActivity(), mArrayList);
    }
}


