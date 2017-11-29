package gov.cipam.gi.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import gov.cipam.gi.R;
import gov.cipam.gi.activities.HomePageActivity;
import gov.cipam.gi.activities.ProductListActivity;
import gov.cipam.gi.adapters.CategoryAdapter;
import gov.cipam.gi.adapters.StatesAdapter;
import gov.cipam.gi.model.Categories;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.adapters.ViewPageAdapter;
import gov.cipam.gi.model.States;
import gov.cipam.gi.utils.Constants;

/**
 * Created by karan on 11/20/2017.
 */

public class HomePage extends Fragment implements CategoryAdapter.setOnCategoryClickListener, StatesAdapter.setOnStateClickedListener {

    RecyclerView rvState,rvCategory;
    ScrollView scrollView;
    AutoScrollViewPager autoScrollViewPager;
    RecyclerView.LayoutManager layoutManager,layoutManager2;
//    StatesFirebaseAdapter statesFirebaseAdapter;
    public static ArrayList<Product> mainGIList;
    public static StatesAdapter statesAdapter;
    public static CategoryAdapter categoryAdapter;
//    CategoryFirebaseAdapter categoryFirebaseAdapter;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseState,mDatabaseCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        rvState =  view.findViewById(R.id.recycler_states);
        rvCategory =  view.findViewById(R.id.recycler_categories);
        autoScrollViewPager = view.findViewById(R.id.viewpager);
        scrollView=view.findViewById(R.id.scroll_view_home);

        categoryAdapter=new CategoryAdapter(HomePageActivity.mCategoryList,getContext(),this,HomePageActivity.mCategoryMap);
        statesAdapter=new StatesAdapter(this,HomePageActivity.mStateList,getContext(),HomePageActivity.mStatesMap);
        scrollView.setSmoothScrollingEnabled(true);
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rvState.setLayoutManager(layoutManager);
//        rvState.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(),rvState,this));
//        rvState.setAdapter(statesFirebaseAdapter);
        rvCategory.setLayoutManager(layoutManager2);
        //rvCategory.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(),rvCategory,this));
//        rvCategory.setAdapter(categoryFirebaseAdapter);
        rvState.setAdapter(statesAdapter);
        rvCategory.setAdapter(categoryAdapter);
        setAutoScroll();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setAutoScroll() {
        autoScrollViewPager.setAdapter(new ViewPageAdapter(getContext()));
        autoScrollViewPager.getSlideBorderMode();
        autoScrollViewPager.isStopScrollWhenTouch();
        autoScrollViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
        autoScrollViewPager.startAutoScroll();
        autoScrollViewPager.setScrollDurationFactor(5);
        autoScrollViewPager.setStopScrollWhenTouch(true);
        autoScrollViewPager.setInterval(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCategoryClicked(View view, int position) {
        Categories clickedCategory=HomePageActivity.mCategoryList.get(position);
        Toast.makeText(getContext(),clickedCategory.getName()+" Clicked !!", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getContext(), ProductListActivity.class);
        intent.putExtra(Constants.CLICKED_LIST_TYPE,Constants.CATEGORY);
        intent.putExtra(Constants.THE_ITEM_CLICKED,clickedCategory.getName());
        startActivity(intent);
    }

    @Override
    public void onStateClickedListener(View view, int position) {
        States clickedState=HomePageActivity.mStateList.get(position);
        Toast.makeText(getContext(),clickedState.getName()+" Clicked", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getContext(), ProductListActivity.class);
        intent.putExtra(Constants.CLICKED_LIST_TYPE,Constants.STATE);
        intent.putExtra(Constants.THE_ITEM_CLICKED,clickedState.getName());
        startActivity(intent);
    }
}
