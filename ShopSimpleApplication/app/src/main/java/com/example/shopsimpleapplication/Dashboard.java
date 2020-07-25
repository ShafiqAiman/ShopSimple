package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
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
import com.google.zxing.client.android.Intents;

public class Dashboard extends AppCompatActivity {

    Button callLogOut, verifyBtn, callScan, callToast;
    TextView verifyText;
    FirebaseAuth fAuth;
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
                Intent intent = new Intent(Dashboard.this, ScanBarcode.class);
                startActivity(intent);

            }
        });

        callToast = findViewById(R.id.purchase_history);

        callToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                Intent intent = new Intent(Dashboard.this, PurchaseHistory.class);
                startActivity(intent);

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
