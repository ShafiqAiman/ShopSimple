package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class receipt extends AppCompatActivity {
    Button createButton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseDatabase database;
    DatabaseReference Cart;
    FirebaseStorage fStorage;
    String userId;
    String x ="";
    String y = "";
    String z = "";
    String time = "";
    String w, a = "";
    Bitmap bmp, scaledBitmap;
    Date dateObj;
    DateFormat dateFormat;
    int pageWidth = 2000;
    public static String CName, CPhone;
    //RecyclerView recyclerView;
    //RecyclerView.LayoutManager layoutManager;
    //FirebaseRecyclerAdapter<com.example.shopsimpleapplication.Model.Receipt, CartViewHolder>adapter;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        createButton = findViewById(R.id.create_Button);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("receipts");

        //put image logo on pdf
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_final);
        scaledBitmap = Bitmap.createScaledBitmap(bmp,550, 500,false);


        //get Customer Name and Phone Number


        //to get total price from cart
        Intent intent = getIntent();
        String totalPrice = intent.getStringExtra(CartActivity.EXTRA_NUMBER);

        String[] pID = intent.getStringArrayExtra("pID");
        String[] pName = intent.getStringArrayExtra("pName");
        String[] pPrice = intent.getStringArrayExtra("pPrice");
        String[] pQuantity = intent.getStringArrayExtra("pQuantity");

        String count = intent.getStringExtra("count");
        int Count = Integer.parseInt(count);

        CName = intent.getStringExtra("CName");
        CPhone = intent.getStringExtra("CPhone");

        String str = pID+System.getProperty("line.separator")+pName+System.getProperty("line.separator");






        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        //load CartDetails();
        createPDF(totalPrice, pID, pName, pPrice, pQuantity, Count, CPhone, CName);


    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPDF(final String totalPrice, final String[] pID, final String[] pName, final String[] pPrice, final String[] pQuantity, final int count, String CPhone, String CName) {
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
        canvas.drawText("Customer Name: " + CName, 40, 590, myPaint);
        canvas.drawText("Contact No.:" + CPhone, 40, 640, myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(35f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Invoice No: " + System.currentTimeMillis(), 40, 690, myPaint);

        dateFormat = new SimpleDateFormat("dd/MM/yy");
        canvas.drawText("Date: " + dateFormat.format(dateObj), 40, 740, myPaint);

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas.drawText("Time: " + dateFormat.format(dateObj), 40, 790, myPaint);

        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(20, 810, pageWidth - 150, 860, myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("Product Id", 65, 850, myPaint);
        canvas.drawText("Item Name", 380, 850, myPaint);
        canvas.drawText("Price", 1230, 850, myPaint);
        canvas.drawText("Qty", 1410, 850, myPaint);
        canvas.drawText("Total", 1580, 850, myPaint);

        canvas.drawLine(360, 820, 360, 850, myPaint);
        canvas.drawLine(1170, 820, 1170, 850, myPaint);
        canvas.drawLine(1380, 820, 1380, 850, myPaint);
        canvas.drawLine(1500, 820, 1500, 850, myPaint);

        //image in pdf
        canvas.drawBitmap(scaledBitmap, 30, 40, myPaint);

        int b = 900;

        for (int i = 0; i < count; i++) {

            canvas.drawText(pID[i], 65, b, myPaint);
            canvas.drawText(pName[i], 380, b, myPaint);
            canvas.drawText(pPrice[i], 1230, b, myPaint);
            canvas.drawText(pQuantity[i], 1410, b, myPaint);
            b = b + 50;
        }

        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawLine(20, b - 20, pageWidth - 150, b - 20, myPaint);
        canvas.drawText("" + totalPrice, 1580, b + 15, myPaint);


        myPdfDocument.finishPage(myPage1);

        dateFormat = new SimpleDateFormat("HHmmss");
        time = dateFormat.format(dateObj);
        dateFormat = new SimpleDateFormat("ddMMyy");
        y = dateFormat.format(dateObj);
        File file = new File(Environment.getExternalStorageDirectory(), time + "_" + y + ".pdf");

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        myPdfDocument.close();

        //selectPDFFile();

        uploadPDFFile(file);

        Intent intent = new Intent(receipt.this, PurchaseHistory.class);
        startActivity(intent);


        Toast.makeText(receipt.this, "Receipt Downloaded", Toast.LENGTH_SHORT).show();
        Toast.makeText(receipt.this, "" + CName, Toast.LENGTH_SHORT).show();
    }



    private void uploadPDFFile(File data) {

        dateObj = new Date();

        dateFormat = new SimpleDateFormat("ddMMyy");
        z = CPhone + "_" + dateFormat.format(dateObj);
        a = dateFormat.format(dateObj);
        dateFormat = new SimpleDateFormat("HHmmss");
        time = dateFormat.format(dateObj);
        w = z +"_"+ time;

        StorageReference reference = storageReference.child(CPhone+ "/"+ w +".pdf");
        reference.putFile(Uri.fromFile(data))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        dateFormat = new SimpleDateFormat("ddMMyy");
                        x = CPhone + "_" + dateFormat.format(dateObj);
                        uploadPDF uploadPDF = new uploadPDF( a + "_" + time + ".pdf",url.toString());
                        databaseReference.child(CPhone).child(databaseReference.push().getKey()).setValue(uploadPDF);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {


            }
        });
    }


}