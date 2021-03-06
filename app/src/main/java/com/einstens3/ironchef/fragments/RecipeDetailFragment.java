package com.einstens3.ironchef.fragments;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.einstens3.ironchef.R;
import com.einstens3.ironchef.models.Challenge;
import com.einstens3.ironchef.models.Recipe;
import com.einstens3.ironchef.services.RecipeService;
import com.einstens3.ironchef.utilities.StringUtils;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.ArrayList;

import static android.os.Build.ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment {
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private static final String ARG_PARAM_RECIPE_OBJECT_ID = "objectId";

    String objectId;
    Recipe recipe;

    View view;
    ImageView ivPhoto;
    TextView tvTitle;
    TextView tvDescription;
    TextView tvAuthor;
    TextView tvCategory;
    TextView tvCookingTime;
    TextView tvServing;
    ListView lvIngredients;
    ImageView ivLike;
    ImageView btnAccept;
    ViewPager viewPager;
    CollapsingToolbarLayout collapsingToolbar;
    PagerSlidingTabStrip pagerSlidingTabStrip;

    ArrayList<String> stepList = null;
    ArrayList<String> ingridients = null;

    public class RecipieDetailPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = { "Steps", "Ingredients"};
        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                  RecipeDetailRecipeListFragment recipeDetailRecipeListFragment
                          = RecipeDetailRecipeListFragment.newInstance(stepList);
                return recipeDetailRecipeListFragment;

            } else  if (position == 1){
                RecipieDetailRecipieIngridientFragment  recipieDetailRecipieIngridientFragment
                        = RecipieDetailRecipieIngridientFragment.newInstance(ingridients);
                return recipieDetailRecipieIngridientFragment;
            } else {
                RecipeDetailRecipeListFragment recipeDetailRecipeListFragment
                        = RecipeDetailRecipeListFragment.newInstance(stepList);
                return recipeDetailRecipeListFragment;
            }

        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public RecipieDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

    }

    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance(String objectId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_RECIPE_OBJECT_ID, objectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            objectId = getArguments().getString(ARG_PARAM_RECIPE_OBJECT_ID);
        }
        stepList = new ArrayList<>();
        ingridients = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view =  inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        bindControls();
        updateControlStates();
        setEventHandlers();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.frRecipieDetailtoolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        return this.view;
    }

    private void bindControls() {
        ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        //tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        tvCookingTime = (TextView) view.findViewById(R.id.tvCookingTime);
        tvServing = (TextView) view.findViewById(R.id.tvServing);
        //lvIngredients = (ListView) view.findViewById(R.id.lvIngredients);
        ivLike = (ImageView)view.findViewById(R.id.ivLike);
        btnAccept  = (ImageView)view.findViewById(R.id.btnAccept);
        NestedScrollView scrollView = (NestedScrollView) view.findViewById (R.id.nest_scrollview);
        scrollView.setFillViewport (true);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
    }

    private void updateControlStates() {
        new RecipeService().queryRecipe(objectId, new RecipeService.QueryRecipeCallback() {
            @Override
            public void success(Recipe recipe) {
                RecipeDetailFragment.this.recipe = recipe;
                try {
                    if (recipe.getName() != null){
                        collapsingToolbar.setTitle(recipe.getName());
                    }
                    if (recipe.getDescription() != null)
                        tvDescription.setText(recipe.getDescription());
                    if (recipe.getAuthor() != null)
                        tvAuthor.setText(recipe.getAuthor().fetchIfNeeded().getUsername());
                    else
                        tvAuthor.setText("anonymous");
                    if (recipe.getCookingTime() != 0)
                        tvCookingTime.setText(String.valueOf(recipe.getCookingTime()));
                    if (recipe.getServing() != 0)
                        tvServing.setText(String.valueOf(recipe.getServing()));
                    if (recipe.getCategories() != null)
                        tvCategory.setText(StringUtils.fromList(recipe.getCategories()));
                    if (recipe.getPhoto() != null){
                        //ivPhoto.setImageURI(Uri.fromFile(recipe.getPhoto().getFile()));
                        Glide.with(ivPhoto.getContext()).load(Uri.fromFile(recipe.getPhoto().getFile())).into(ivPhoto);
                    }

                    if(recipe.getSteps() != null) {
                        stepList.addAll(recipe.getSteps());
                    }

                    recipe.countLikes(new CountCallback() {
                        @Override
                        public void done(int count, ParseException e) {
                            if(count>0){
                                ivLike.setImageResource(R.drawable.ic_heart);
                                ivLike.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                            }
                        }
                    });

                    recipe.getOwnChallenge(new GetCallback<Challenge>() {
                        @Override
                        public void done(Challenge object, ParseException e) {

                            if(object != null){
                                if(object.getState() == Challenge.STATE_ACCEPTED){
                                    btnAccept.setImageResource(R.drawable.ic_knives);
                                    btnAccept.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                                }
                                if(object.getState() == Challenge.STATE_COMPLETED){
                                    btnAccept.setImageResource(R.drawable.ic_check);
                                    btnAccept.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                                }
                            }
                        }
                    });

                    if(recipe.getIngredients() != null) {
                        ingridients.addAll(recipe.getIngredients());
                    }

                    viewPager = (ViewPager) view.findViewById(R.id.recipeDetailviewpager);
                    pagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.pstRecipeDetailTabs);
                    viewPager.setAdapter(new RecipieDetailPagerAdapter(getFragmentManager()));
                    pagerSlidingTabStrip.setViewPager(viewPager);

                } catch (ParseException e) {
                    Log.e(TAG, "Parse Error", e);
                    Toast.makeText(getContext(), "Parse Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void error(ParseException e) {
                Toast.makeText(getContext(), "Failed to obtain Recipe for ID: " + ID, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setEventHandlers() {
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivLike.setImageResource(R.drawable.ic_heart);
                ivLike.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                RecipeDetailFragment.this.recipe.like();
                Toast.makeText(getContext(), "Liked Recipe", Toast.LENGTH_LONG).show();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAccept.setImageResource(R.drawable.ic_knives);
                btnAccept.setColorFilter(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                Challenge.acceptChallenge(RecipeDetailFragment.this.recipe);
                Toast.makeText(getContext(), "Challenge Accepted", Toast.LENGTH_LONG).show();
            }
        });
    }
}
