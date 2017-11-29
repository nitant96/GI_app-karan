package gov.cipam.gi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.adapters.ProductListAdapter;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.utils.Constants;

public class ProductListActivity extends BaseActivity implements ProductListAdapter.setOnProductClickedListener {
    RecyclerView productListRecycler;
    ArrayList<Product> myProductList=new ArrayList<>();
    ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setUpToolbar(this);

        Intent intent=getIntent();
        String clickedListType=intent.getStringExtra(Constants.CLICKED_LIST_TYPE);
        String theItemClicked=intent.getStringExtra(Constants.THE_ITEM_CLICKED);
        if(clickedListType.equals(Constants.STATE)){
            myProductList=HomePageActivity.stateMapping.get(theItemClicked);
        }
        else{
            myProductList=HomePageActivity.categoryMapping.get(theItemClicked);
        }
        productListRecycler=findViewById(R.id.product_list_recycler_view);
        productListAdapter=new ProductListAdapter(myProductList,this,this);
        productListRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        productListRecycler.setAdapter(productListAdapter);
    }

    @Override
    protected int getToolbarID() {
        return R.id.product_list_toolbar;
    }

    @Override
    public void onProductClicked(View view, int position) {
        Toast.makeText(this,myProductList.get(position).getName()+" Clicked !!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,ProductDetailActivity.class));
    }
}
