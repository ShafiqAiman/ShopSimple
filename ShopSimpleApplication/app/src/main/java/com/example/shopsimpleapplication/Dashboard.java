package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.zxing.client.android.Intents;

public class Dashboard extends AppCompatActivity {

    Button callLogOut, verifyBtn, callScan, callToast, callReceipt;
    TextView verifyText;
    FirebaseAuth fAuth;

    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);



        callScan = findViewById(R.id.toScan);

        callScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                Intent intent = new Intent(Dashboard.this, ProductDetails.class);
                startActivity(intent);

            }
        });

        callToast = findViewById(R.id.purchase_history);

        callToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                Intent cartDetails = new Intent(Dashboard.this, CartActivity.class);
                startActivity(cartDetails);

            }
        });

        //callReceipt = findViewById(R.id.receiptgen);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                        //uphone.setText(documentSnapshot.getString("PhoneNo"));

                        final String a = documentSnapshot.getString("PhoneNo");
                        callReceipt = findViewById(R.id.receiptgen);

                        callReceipt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(Dashboard.this, "User Phone Number : " + a, Toast.LENGTH_SHORT).show();
                            }

                        });

                    }
                });

        callLogOut = findViewById(R.id.logoutBtn);

        callLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();


            }

        });



        fAuth =FirebaseAuth.getInstance();

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
}


        /*FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();*/
