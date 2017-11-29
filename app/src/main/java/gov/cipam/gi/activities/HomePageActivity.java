package gov.cipam.gi.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import gov.cipam.gi.R;
import gov.cipam.gi.common.SharedPref;
import gov.cipam.gi.fragments.HomePage;
import gov.cipam.gi.fragments.Tab2;
import gov.cipam.gi.model.Categories;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.model.States;
import gov.cipam.gi.model.Users;
import gov.cipam.gi.utils.Constants;

public class HomePageActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,TabLayout.OnTabSelectedListener {

    private FirebaseAuth mAuth;
    Users user;
    ArrayList<Product> mainGIList=new ArrayList<>();
    private Firebase mRef1,mRef2,mRef3;
    TextView nav_user,nav_email;
    HomePage homePage;
    SearchView searchView;
    Tab2 tab2;
    boolean download1=false,download2=false,download3=false;

    public static ArrayList<Categories> mCategoryList=new ArrayList<>();
    public static ArrayList<States> mStateList=new ArrayList<>();
    public static HashMap<String,ArrayList<Product>> stateMapping=new HashMap<>();
    public static HashMap<String,ArrayList<Product>> categoryMapping=new HashMap<>();
    public static HashMap<String,String> mCategoryMap=new HashMap<>();
    public static HashMap<String,String>  mStatesMap=new HashMap<>();

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Firebase.setAndroidContext(this);

        setUpToolbar(this);
        mAuth = FirebaseAuth.getInstance();
        homePage =new HomePage();
        tab2=new Tab2();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(0);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            mRef1 = new Firebase("https://gi-india.firebaseio.com/Giproducts");
            mRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot oneGIData : dataSnapshot.getChildren()) {
                        Product currentGI;
                        ArrayList<Seller> oneGISellersList=new ArrayList<>();
                        currentGI = oneGIData.getValue(Product.class);
                        DataSnapshot sellerData=oneGIData.child("seller");
                        for(DataSnapshot oneSellerData : sellerData.getChildren()){
                            Seller oneSeller=oneSellerData.getValue(Seller.class);
                            oneGISellersList.add(oneSeller);
                        }
                        currentGI.setSeller(oneGISellersList);
                        mainGIList.add(currentGI);
                    }
                    for (int i = 0; i < mainGIList.size(); i++) {
                        String currentState = mainGIList.get(i).getState();
                        String currentCategory = mainGIList.get(i).getCategory();
                        ArrayList<Product> stateList = stateMapping.get(currentState);
                        ArrayList<Product> categoryList = categoryMapping.get(currentCategory);
                        if (stateList == null) {
                            stateList = new ArrayList<>();
                        }
                        if (categoryList == null) {
                            categoryList = new ArrayList<>();
                        }
                        stateList.add(mainGIList.get(i));
                        categoryList.add(mainGIList.get(i));
                        stateMapping.put(currentState, stateList);
                        categoryMapping.put(currentCategory, categoryList);
                    }
                    for (String oneStateName : stateMapping.keySet()) {
                        States oneState = new States();
                        oneState.setName(oneStateName);
                        ArrayList<Product> k = stateMapping.get(oneStateName);
                        oneState.setStateProductList(k);
                        mStateList.add(oneState);
                    }
                    for (String oneCategoryName : categoryMapping.keySet()) {
                        Categories oneCategory = new Categories();
                        oneCategory.setName(oneCategoryName);
                        ArrayList<Product> k = categoryMapping.get(oneCategoryName);
                        oneCategory.setCategoryProductList(k);
                        mCategoryList.add(oneCategory);
                    }
                    Toast.makeText(HomePageActivity.this, "downnload 1 success", Toast.LENGTH_SHORT).show();
                    HomePage.statesAdapter.notifyDataSetChanged();
                    HomePage.categoryAdapter.notifyDataSetChanged();
//                    download1 = true;
//                    shallStartOnStart();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(HomePageActivity.this, "Download 1 cancelled", Toast.LENGTH_SHORT).show();
                }
            });

            mRef2 = new Firebase("https://gi-india.firebaseio.com/States");
            mRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot oneStateData : dataSnapshot.getChildren()) {
                        States oneState = oneStateData.getValue(States.class);
                        mStatesMap.put(oneState.getName(), oneState.getDpurl());
                    }
                    Toast.makeText(HomePageActivity.this, "Download 2 Success", Toast.LENGTH_SHORT).show();
                    HomePage.statesAdapter.notifyDataSetChanged();
//                    download2 = true;
//                    shallStartOnStart();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(HomePageActivity.this, "Download 2 Cancelled", Toast.LENGTH_SHORT).show();
                }
            });

            mRef3 = new Firebase("https://gi-india.firebaseio.com/Categories");
            mRef3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot oneCategoryData : dataSnapshot.getChildren()) {
                        Categories oneCategory = oneCategoryData.getValue(Categories.class);
                        mCategoryMap.put(oneCategory.getName(), oneCategory.getDpurl());
                    }
                    Toast.makeText(HomePageActivity.this, "Download 3 Success", Toast.LENGTH_SHORT).show();
                    HomePage.categoryAdapter.notifyDataSetChanged();
//                    download3 = true;
//                    shallStartOnStart();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(HomePageActivity.this,firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        View hView =  navigationView.getHeaderView(0);
        nav_user = hView.findViewById(R.id.nav_header_name);
        nav_email=hView.findViewById(R.id.nav_header_email);
        user = SharedPref.getSavedObjectFromPreference(HomePageActivity.this, Constants.KEY_USER_INFO,Constants.KEY_USER_DATA,Users.class);
        setUserName();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_home_page, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.isSubmitButtonEnabled();
        searchView.animate();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }
        else if(id==R.id.action_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(R.string.logout);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mAuth.signOut();
                    startActivity(new Intent(HomePageActivity.this, SignInActivity.class));
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            }).show();
        }
        else if (id==R.id.action_search){
            searchView.onActionViewExpanded();
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentInflate(homePage);
                    return true;
                case R.id.navigation_dashboard:
                    fragmentInflate(tab2);
                    return true;
            }
            return false;
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
          startActivity(new Intent(this,AccountInfoActivity.class));
        } else if (id == R.id.nav_sign_up) {

        }  else if (id == R.id.nav_about_us) {
            startActivity(new Intent(this,AboutActivity.class));

        } else if (id == R.id.nav_share){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT,"google.com").setType("text/plain");
            startActivity(Intent.createChooser(intent,"Choose an app"));

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
            SharedPreferences preferences =
                    getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);

            if (!preferences.getBoolean(Constants.ONBOARDING_COMPLETE, false)) {

                startActivity(new Intent(this, IntroActivity.class));

                finish();
            }
            else{
                if(currentUser ==null){
                    startActivity(new Intent(this,SignInActivity.class));
                }
                else{
                    if(download1 && download2 && download3) {
                        fragmentInflate(homePage);
                    }
                    else{
                        fragmentInflate(homePage);
                    }
                }
            }
        super.onStart();
    }

    private void setUserName() {
        if(user!=null) {
            nav_email.setText(user.getEmail());
            nav_user.setText("Hi "+user.getName().substring(0,1).toUpperCase()+user.getName().substring(1));
        }
        else {
            nav_email.setText(getString(R.string.login_request));
            nav_user.setText(getString(R.string.hi));
        }
    }

    public void fragmentInflate(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_page_frame_layout, fragment);
        fragmentTransaction.commit();
    }
    @Override
    protected int getToolbarID() {
        return R.id.toolbar;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
