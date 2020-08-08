package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Dashboard extends AppCompatActivity implements View.OnClickListener{

    Button callLogOut, verifyBtn, callScan, callCart, callHistory;
    CardView scanner, cart, receipt, logout;
    TextView verifyText;
    FirebaseAuth fAuth;

    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);


        /*callScan = findViewById(R.id.toScan);

        callScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                Intent intent = new Intent(Dashboard.this, ProductDetails.class);
                startActivity(intent);

            }
        });

        callCart = findViewById(R.id.toCart);

        callCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                Intent intent = new Intent(Dashboard.this, CartActivity.class);
                startActivity(intent);

            }
        });

        //callReceipt = findViewById(R.id.receiptgen);

        callHistory = findViewById(R.id.receiptgen);

        callHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, PurchaseHistory.class);
                startActivity(intent);

            }


        });*/

        //define cards
        scanner = (CardView) findViewById(R.id.toScan);
        cart = (CardView) findViewById(R.id.toCart);
        receipt = (CardView) findViewById(R.id.toReceipts);

        scanner.setOnClickListener(this);
        cart.setOnClickListener(this);
        receipt.setOnClickListener(this);



        logout = (CardView)findViewById(R.id.toLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();


            }

        });


        fAuth = FirebaseAuth.getInstance();

        verifyBtn = findViewById(R.id.verifyBtn);
        verifyText = findViewById(R.id.verifyText);

        userId = fAuth.getCurrentUser().getUid();
        final FirebaseUser user = fAuth.getCurrentUser();

        if (!user.isEmailVerified()) {
            verifyText.setVisibility(View.VISIBLE);
            verifyBtn.setVisibility(View.VISIBLE);

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email has been sent.", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent" + e.getMessage());
                        }
                    });
                }
            });

        }
    }

    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()){

            case R.id.toScan : i = new Intent(this, ProductDetails.class);startActivity(i);break;
            case R.id.toCart : i = new Intent(this, CartActivity.class);startActivity(i); break;
            case R.id.toReceipts : i = new Intent(this, PurchaseHistory.class);startActivity(i); break;
            default:break;

        }

    }

        /*public void receiptgen(View v){
            startActivity(new Intent(getApplicationContext(),PurchaseHistory.class));
        }*/


}


        /*FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();*/
