package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Categories;

/**
 * Created by NITANT SOOD on 28-11-2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    ArrayList<Categories> mCategoryList;
    Context mContext;
    setOnCategoryClickListener mListener;
    HashMap<String,String> mMap;

    public CategoryAdapter(ArrayList<Categories> mCategoryList, Context mContext, setOnCategoryClickListener mListener,HashMap<String,String> myMap) {
        this.mCategoryList = mCategoryList;
        this.mContext = mContext;
        this.mListener = mListener;
        this.mMap=myMap;
    }

    public interface setOnCategoryClickListener{
        void onCategoryClicked(View view,int position);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_category_item,parent,false);
        return  new CategoryAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        String DpUrl;
            if(mMap==null || mMap.size()==0){
                Toast.makeText(mContext, "its  null....ureka", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(mContext, "sab sahi hai", Toast.LENGTH_SHORT).show();
            }
        holder.mName.setText(mCategoryList.get(position).getName());
        DpUrl=mMap.get(mCategoryList.get(position).getName());
        Picasso.with(mContext)
                .load(DpUrl)
                .placeholder(R.drawable.place_holder)
                .into(holder.mDp);
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mName;
        public ImageView mDp;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            mName =itemView.findViewById(R.id.card_name_category);
            mDp =itemView.findViewById(R.id.card_dp_category);
            mDp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onCategoryClicked(v,getAdapterPosition());
        }
    }

}
