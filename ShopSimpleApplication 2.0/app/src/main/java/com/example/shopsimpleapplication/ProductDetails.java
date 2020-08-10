package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

public class ProductDetails extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseDatabase database;
    DatabaseReference id, order;
    String y = "";
    String a = "";

    private Button addToCartButton,goCart,goScan;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productName, productId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        database = FirebaseDatabase.getInstance();
        id = database.getReference("Product");
        order = database.getReference("Cart");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        numberButton = (ElegantNumberButton)findViewById(R.id.number_btn);
        productImage = (ImageView)findViewById(R.id.product_image_details);
        productName = (TextView)findViewById(R.id.product_name_details);
        productPrice = (TextView)findViewById(R.id.product_price_details);
        productId = (TextView)findViewById(R.id.product_ID);
        addToCartButton = (Button)findViewById(R.id.add_to_cart_button);
        //goCart = (Button)findViewById(R.id.toCart);
        //goScan = (Button)findViewById(R.id.backScan);
        scanCode();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                a = documentSnapshot.getString("PhoneNo");

            }
        });

        /*goCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetails.this, CartActivity.class);
                startActivity(intent);

            }


        });

        goScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();

            }


        });*/


    }

    private void scanCode(){

        IntentIntegrator integrator = new IntentIntegrator(ProductDetails.this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()!=null){
                //String x = result.getContents();
                y = result.getContents();
                if(!y.isEmpty()){
                    id.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(y).exists()){
                                id.child(y).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final Product product = dataSnapshot.getValue(Product.class);

                                        Picasso.with(getBaseContext()).load(product.getImage())
                                                .into(productImage);
                                        productName.setText(product.getName());
                                        productPrice.setText("RM "+product.getPrice());
                                        productId.setText(product.getId());

                                        addToCartButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Cart cart = new Cart(product.getName(),product.getPrice(), numberButton.getNumber(),product.getId());
                                                //Toast.makeText(ProductDetails.this,a,Toast.LENGTH_SHORT).show();
                                                order.child(a).child(y).setValue(cart);
                                                Toast.makeText(ProductDetails.this,"Item is added",Toast.LENGTH_SHORT).show();

                                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                                                //builder.setMessage("To scan more products press SCANNER button.");
                                                //builder.setMessage("To proceed to shopping cart press CART button.");
                                                //Toast.makeText(MainActivity.this, y, Toast.LENGTH_SHORT).show();
                                                builder.setTitle("ITEM IS ADDED");
                                                builder.setPositiveButton("Continue Scanning Products", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        scanCode();
                                                    }
                                                }).setNegativeButton("To Shopping Cart", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(ProductDetails.this, CartActivity.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                                AlertDialog dialog = builder.create();
                                                dialog.show();

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }else{

                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                                builder.setMessage("Product cannot be found in database.");
                                //Toast.makeText(MainActivity.this, y, Toast.LENGTH_SHORT).show();
                                builder.setTitle("Scanning Result");
                                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        scanCode();
                                    }
                                }).setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                //Compare(productid);


            }
            else {
                Toast.makeText(ProductDetails.this, "No Results", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }
}