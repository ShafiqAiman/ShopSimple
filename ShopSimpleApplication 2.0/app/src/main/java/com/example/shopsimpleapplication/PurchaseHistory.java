package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Text;

public class PurchaseHistory extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseStorage fStorage;
    FirebaseDatabase database;
    String userId;
    Button callDash;
    ListView myPDFListView;
    DatabaseReference databaseReference,Receipts;
    List<uploadPDF> uploadPDFS;
    StorageReference storageReference;
    Date dateObj;
    DateFormat dateFormat;

    public static String CustPhone,a;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dateObj = new Date();

        myPDFListView = (ListView)findViewById(R.id.myListView);
        uploadPDFS = new ArrayList<>();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("receipts");


        callDash = findViewById(R.id.backDashboard);

        callDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PurchaseHistory.this,Dashboard.class);
                startActivity(intent);

            }


        });



        //viewAllFiles();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                CustPhone = documentSnapshot.getString("PhoneNo");
                a = documentSnapshot.getString("PhoneNo");
                uploadPDFS = new ArrayList<>();


                viewAllFiles();

                myPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                        uploadPDF uploadPDF = uploadPDFS.get(position);

                        Intent intent = new Intent();
                        intent.setType(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(uploadPDF.getUrl());

                        if(uri.toString().contains(".pdf")){
                            intent.setDataAndType(uri,CustPhone + "/pdf");

                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

            }
        });



    }

    private void viewAllFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("receipts").child(CustPhone);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    uploadPDF uploadPDF = postSnapshot.getValue(com.example.shopsimpleapplication.uploadPDF.class);
                    uploadPDFS.add(uploadPDF);
                }

                String[] uploads = new String[uploadPDFS.size()];

                for (int i = 0; i < uploads.length; i++) {

                    uploads[i] = uploadPDFS.get(i).getName();

                }

                ArrayAdapter<String> adapter = new ArrayAdapter <String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                myPDFListView.setAdapter(adapter);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}