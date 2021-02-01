package com.ruth.checkmeout.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruth.checkmeout.R;
import com.ruth.checkmeout.models.CheckMeOutSearchResponse;
import com.ruth.checkmeout.ui.ShopFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopFragmentAdapter extends RecyclerView.Adapter<ShopFragmentAdapter.ShopFragmentViewHolder> {


    private List<CheckMeOutSearchResponse> mGoods;
    private Context mContext;
    private int total;


    public ShopFragmentAdapter(List<CheckMeOutSearchResponse> mGoods, Context mContext) {
        this.mGoods = mGoods;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ShopFragmentAdapter.ShopFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list_item, parent, false);
        ShopFragmentViewHolder viewHolder = new ShopFragmentViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ShopFragmentViewHolder holder, int position) {
        holder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoods.remove(position);
                notifyItemChanged(position);
                notifyItemRangeRemoved(position,1);

            }
        });
        holder.bindGood(mGoods.get(position));
        calculateTotal();

    }

    public int calculateTotal() {
        for(int i=0;i<getItemCount();i++){
            total=total+mGoods.get(i).getPrice();

        }
        return total;
    }

    @Override
    public int getItemCount() {
        return mGoods.size();
    }



    public class ShopFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.viewBarcode)
        TextView viewBarcode;
        @BindView(R.id.viewName)
        TextView viewName;
        @BindView(R.id.viewPrice)
        TextView viewPrice;
        @BindView(R.id.viewDelete) TextView viewDelete;

        public ShopFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //itemView.setOnClickListener(this);
        }
        public void bindGood(CheckMeOutSearchResponse good) {
            //Picasso.get().load(restaurant.getImageUrl()).into(mRestaurantImageView);
            viewBarcode.setText(good.getCode());
            viewName.setText(good.getName());
            viewPrice.setText(good.getPrice().toString());
        }

        @Override
        public void onClick(View v) {
            int currentPosition=getLayoutPosition();


        }
    }
}
