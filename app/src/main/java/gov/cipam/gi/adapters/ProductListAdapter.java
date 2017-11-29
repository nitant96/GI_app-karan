package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Product;

/**
 * Created by NITANT SOOD on 29-11-2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    ArrayList<Product> GIList;
    Context mContext;
    setOnProductClickedListener mListener;

    public ProductListAdapter(ArrayList<Product> GIList, Context mContext, setOnProductClickedListener mListener) {
        this.GIList = GIList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public interface setOnProductClickedListener{
        void onProductClicked(View view,int position);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_product_list,parent,false);
        return  new ProductListAdapter.ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        holder.mTitle.setText(GIList.get(position).getName());
        holder.mFiller.setText(GIList.get(position).getDetail());
        holder.mState.setText(GIList.get(position).getCategory());
        Picasso.with(mContext)
                .load(GIList.get(position).getDpurl())
                .fit()
                .placeholder(R.drawable.place_holder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return GIList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTitle,mFiller,mState;
        public ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            mTitle=itemView.findViewById(R.id.card_view_title);
            mFiller=itemView.findViewById(R.id.card_view_filler);
            mState=itemView.findViewById(R.id.card_view_state);
            imageView=itemView.findViewById(R.id.card_view_image);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onProductClicked(v,getAdapterPosition());
        }
    }

}
