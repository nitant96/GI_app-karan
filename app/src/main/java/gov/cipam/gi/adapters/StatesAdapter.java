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
import java.util.HashMap;

import gov.cipam.gi.R;
import gov.cipam.gi.model.States;

/**
 * Created by NITANT SOOD on 28-11-2017.
 */

public class StatesAdapter extends RecyclerView.Adapter<StatesAdapter.StateViewHolder> {
    setOnStateClickedListener mListener;
    ArrayList<States> mListOfStates;
    Context mContext;
    HashMap<String,String> mMap;

    public StatesAdapter(setOnStateClickedListener mListener, ArrayList<States> mListOfStates, Context mContext,HashMap<String,String> myMap) {
        this.mListener = mListener;
        this.mListOfStates = mListOfStates;
        this.mContext = mContext;
        this.mMap=myMap;
    }

    public interface setOnStateClickedListener{
        void onStateClickedListener(View view,int position);
    }
    @Override
    public StateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_state_item,parent,false);
        return  new StatesAdapter.StateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StateViewHolder holder, int position) {
        holder.mName.setText(mListOfStates.get(position).getName());
        String DpUrl;
        DpUrl=mMap.get(mListOfStates.get(position).getName());

        Picasso.with(mContext)
                .load(DpUrl)
                .placeholder(R.drawable.place_holder)
                .into(holder.mDp);
    }

    @Override
    public int getItemCount() {
        return mListOfStates.size();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mName;
        public ImageView mDp;
        public StateViewHolder(View itemView) {
            super(itemView);

            mName =itemView.findViewById(R.id.card_name_state);
            mDp =itemView.findViewById(R.id.card_dp_state);
            mDp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onStateClickedListener(v,getAdapterPosition());
        }
    }

}
