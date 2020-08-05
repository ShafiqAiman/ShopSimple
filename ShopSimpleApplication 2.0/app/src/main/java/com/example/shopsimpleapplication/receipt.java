package com.example.shopsimpleapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;


import com.example.shopsimpleapplication.Model.Cart;
import com.example.shopsimpleapplication.Model.Receipt;
import com.example.shopsimpleapplication.ViewHolder.CartViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.storage.FirebaseStorage;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class receipt extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseDatabase database;
    DatabaseReference Cart;
    FirebaseStorage fStorage;
    String userId;
    Button createButton;
    Date dateObj;
    DateFormat dateFormat;
    int pageWidth = 2000;
    public static String CustName, CustPhone;
    //RecyclerView recyclerView;
    //RecyclerView.LayoutManager layoutManager;
    //FirebaseRecyclerAdapter<com.example.shopsimpleapplication.Model.Receipt, CartViewHolder>adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        //recyclerView = findViewById(R.id.cart_list);

        createButton = findViewById(R.id.create_Button);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        //to get total price from cart
        Intent intent = getIntent();
        String totalPrice = intent.getStringExtra(CartActivity.EXTRA_NUMBER);

        String[] pID = intent.getStringArrayExtra("pID");
        String[] pName = intent.getStringArrayExtra("pName");
        String[] pPrice = intent.getStringArrayExtra("pPrice");
        String[] pQuantity = intent.getStringArrayExtra("pQuantity");

        String count = intent.getStringExtra("count");
        int Count = Integer.parseInt(count);



        //Cart = database.getReference("Cart");

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                CustName = documentSnapshot.getString("Name");
                CustPhone = documentSnapshot.getString("PhoneNo");
            }
        });


        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        //loadCartDetails();
        createPDF(totalPrice, pID,pName,pPrice,pQuantity, Count);


    }





    private void createPDF(final String totalPrice, final String[] pID, final String[] pName, final String[] pPrice, final String[] pQuantity,final int count) {
        createButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View vi) {

                Toast.makeText(receipt.this, pName[0], Toast.LENGTH_LONG).show();
                dateObj = new Date();


                PdfDocument myPdfDocument = new PdfDocument();
                Paint myPaint = new Paint();
                Paint titlePaint = new Paint();

                PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(2000, 2010, 1).create();
                PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                Canvas canvas = myPage1.getCanvas();

                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                titlePaint.setTextSize(70);
                canvas.drawText("INVOICE", pageWidth / 2, 500, titlePaint);


                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("Customer Name: " + CustName, 20, 590, myPaint);
                canvas.drawText("Contact No.:" + CustPhone, 20, 640, myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                int a = 0;
                canvas.drawText("Invoice No: 01", 20, 690, myPaint);

                dateFormat = new SimpleDateFormat("dd/MM/yy");
                canvas.drawText("Date: " + dateFormat.format(dateObj), 20, 740, myPaint);

                dateFormat = new SimpleDateFormat("HH:mm:ss");
                canvas.drawText("Time: " + dateFormat.format(dateObj), 20, 790, myPaint);

                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(2);
                canvas.drawRect(20, 810, pageWidth - 20, 860, myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setStyle(Paint.Style.FILL);
                canvas.drawText("Product Id", 70, 850, myPaint);
                canvas.drawText("Item Name", 300, 850, myPaint);
                canvas.drawText("Price", 800, 850, myPaint);
                canvas.drawText("Qty", 1000, 850, myPaint);
                canvas.drawText("Total", 1200, 850, myPaint);

                canvas.drawLine(280, 820, 280, 850, myPaint);
                canvas.drawLine(780, 820, 780, 850, myPaint);
                canvas.drawLine(980, 820, 980, 850, myPaint);
                canvas.drawLine(1130, 820, 1130, 850, myPaint);



                int b = 900;

                for (int i=0;i<count;i++){

                    canvas.drawText(pID[i], 70, b, myPaint);
                    canvas.drawText(pName[i], 300, b, myPaint);
                    canvas.drawText(pPrice[i], 800, b, myPaint);
                    canvas.drawText(pQuantity[i], 1000, b, myPaint);
                    b = b+50;
                }

                canvas.drawText("" + totalPrice, 1050, 900, myPaint);
                //canvas.drawText("" + productPRICE, 20, 640, myPaint);
                //canvas.drawText("" + productQUANTITY, 20, 640, myPaint);





                myPdfDocument.finishPage(myPage1);

                File file = new File(Environment.getExternalStorageDirectory(), "Receipt.pdf");

                try {
                    myPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myPdfDocument.close();

                Toast.makeText(receipt.this, "Receipt Downloaded", Toast.LENGTH_SHORT).show();


            }
        });
    }




}