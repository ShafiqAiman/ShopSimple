package com.example.chefantasia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chefantasia.Model.Cart;
import com.example.chefantasia.Model.Product;
import com.example.chefantasia.ViewHolder.CartViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button submitButton;
    private TextView TotalAmount;

    private double TotalPrice = 0.00;

    FirebaseRecyclerAdapter<com.example.chefantasia.Model.Cart, CartViewHolder>adapter;

    FirebaseDatabase database;
    DatabaseReference Cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_activity);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        submitButton = (Button) findViewById(R.id.submitBtn);

        TotalAmount = (TextView) findViewById(R.id.total_price);

        database = FirebaseDatabase.getInstance();
        Cart = database.getReference("Cart");

        loadCart();
    }

    private void loadCart(){

        adapter = new FirebaseRecyclerAdapter<com.example.chefantasia.Model.Cart, CartViewHolder>(com.example.chefantasia.Model.Cart.class, R.layout.cart_items_layout, CartViewHolder.class, Cart) {
            @Override
            protected void populateViewHolder(CartViewHolder cartViewHolder, final com.example.chefantasia.Model.Cart cart, final int i) {
                cartViewHolder.productNAME.setText(cart.getName());
                cartViewHolder.productPRICE.setText("RM "+cart.getPrice());
                cartViewHolder.productQUANTITY.setText(cart.getQuantity());

                Double DProductPrice = ((Double.valueOf(cart.getPrice()))) * (Double.valueOf(cart.getQuantity()));
                TotalPrice = TotalPrice + DProductPrice;
                DecimalFormat df2 = new DecimalFormat("#.##");

                TotalAmount.setText("Total Price = RM"+String.valueOf(df2.format(TotalPrice)));
                //final Cart local = cart;

                cartViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cart.child(cart.getId()).removeValue();
                        Toast.makeText(CartActivity.this,"This item is removed.",Toast.LENGTH_SHORT).show();

                        Double DProductPrice = ((Double.valueOf(cart.getPrice()))) * (Double.valueOf(cart.getQuantity()));
                        TotalPrice = TotalPrice - DProductPrice;
                        DecimalFormat df2 = new DecimalFormat("#.##");
                        TotalAmount.setText("Total Price = RM"+String.valueOf(df2.format(TotalPrice)));
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }


}