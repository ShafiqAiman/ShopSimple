package com.example.chefantasia.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chefantasia.Interface.ItemClickListener;
import com.example.chefantasia.R;


public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productNAME,productPRICE, productQUANTITY;
    public Button deleteBtn;


    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        productNAME = (TextView)itemView.findViewById(R.id.cart_productName);
        productPRICE = (TextView)itemView.findViewById(R.id.cart_productPrice);
        productQUANTITY = (TextView)itemView.findViewById(R.id.cart_productQuantity);
        deleteBtn = (Button)itemView.findViewById(R.id.deleteBtn);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}