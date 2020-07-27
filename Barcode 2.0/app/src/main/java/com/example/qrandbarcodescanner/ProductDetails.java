package com.example.qrandbarcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.qrandbarcodescanner.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

public class ProductDetails extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference id, order;
    String y = "";

    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        database = FirebaseDatabase.getInstance();
        id = database.getReference("Product");
        order = database.getReference("Cart");

        numberButton = (ElegantNumberButton)findViewById(R.id.number_btn);
        productImage = (ImageView)findViewById(R.id.product_image_details);
        productName = (TextView)findViewById(R.id.product_name_details);
        productDescription = (TextView)findViewById(R.id.product_description_details);
        productPrice = (TextView)findViewById(R.id.product_price_details);
        addToCartButton = (Button)findViewById(R.id.add_to_cart_button);
        scanCode();

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
                                        productPrice.setText(product.getPrice());

                                        addToCartButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Product cart = new Product(product.getImage(),product.getName(),product.getPrice());
                                                order.child(y).setValue(cart);
                                                Toast.makeText(ProductDetails.this,"Item is added",Toast.LENGTH_SHORT).show();
                                                scanCode();

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