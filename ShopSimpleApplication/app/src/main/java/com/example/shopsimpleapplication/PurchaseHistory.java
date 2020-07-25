package com.example.shopsimpleapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

public class PurchaseHistory extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    TextView uphone;
    Button toastCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        uphone = findViewById(R.id.phone);

        fAuth =FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                uphone.setText(documentSnapshot.getString("PhoneNo"));

                final String a = documentSnapshot.getString("PhoneNo");
                toastCall = findViewById(R.id.toast);

                toastCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(PurchaseHistory.this, "User Phone Number : " + a, Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });

    }
}