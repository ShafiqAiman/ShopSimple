package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shopsimpleapplication.Model.Cart;
import com.example.shopsimpleapplication.Model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class UpdateProduct extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseDatabase database;
    DatabaseReference id, order;
    String pid = "";
    String phoneNo = "";
    String quantity = "";

    private Button addToCartButton,goCart,goScan;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productName, productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        database = FirebaseDatabase.getInstance();
        id = database.getReference("Product");
        order = database.getReference("Cart");
        pid = getIntent().getStringExtra("ID");
        phoneNo = getIntent().getStringExtra("CPhone1");
        quantity = getIntent().getStringExtra("quantity");

        numberButton = (ElegantNumberButton)findViewById(R.id.number_Btn1);
        productImage = (ImageView)findViewById(R.id.image_details);
        productName = (TextView)findViewById(R.id.name_details);
        productPrice = (TextView)findViewById(R.id.price_details);
        productId = (TextView)findViewById(R.id.productID);
        addToCartButton = (Button)findViewById(R.id.addToCart_button1);
        goCart = (Button)findViewById(R.id.toCart1);
        goScan = (Button)findViewById(R.id.backScan1);

        id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                id.child(pid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Product product = dataSnapshot.getValue(Product.class);

                        Picasso.with(getBaseContext()).load(product.getImage())
                                .into(productImage);
                        productName.setText(product.getName());
                        productPrice.setText("RM "+product.getPrice());
                        productId.setText(product.getId());

                        numberButton.setNumber(quantity);

                        addToCartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //order.child(phoneNo).child(pid).removeValue();

                                Cart cart = new Cart(product.getName(),product.getPrice(), numberButton.getNumber(),product.getId());
                                order.child(phoneNo).child(pid).setValue(cart);
                                Toast.makeText(UpdateProduct.this,"Item is updated",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateProduct.this, CartActivity.class);
                                startActivity(intent);




                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}