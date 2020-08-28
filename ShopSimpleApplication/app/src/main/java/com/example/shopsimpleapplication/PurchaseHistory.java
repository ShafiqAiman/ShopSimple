package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class PurchaseHistory extends AppCompatActivity {

    //Variables
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
    public static String CustPhone,a;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        myPDFListView = (ListView)findViewById(R.id.myListView);
        uploadPDFS = new ArrayList<>();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("receipts");


        // to the dashboard page
        callDash = findViewById(R.id.backDashboard);

        callDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PurchaseHistory.this,Dashboard.class);
                startActivity(intent);
                finish();

            }


        });





        //to display all user receipts
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


    //to fetch all receipts for user
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

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this,Dashboard.class);
        startActivity(intent);
        // This above line close correctly
    }



}