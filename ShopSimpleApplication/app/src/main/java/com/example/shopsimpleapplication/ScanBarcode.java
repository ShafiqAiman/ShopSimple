package com.example.shopsimpleapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanBarcode extends AppCompatActivity{

    Button scanBtn, cartBtn;

    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        scanBtn = findViewById(R.id.scanBtn);
        cartBtn = findViewById(R.id.cart);
        //scanBtn.setOnClickListener(MainActivity.this);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetails = new Intent(ScanBarcode.this,ProductDetails.class);
                startActivity(productDetails);

            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartDetails = new Intent(ScanBarcode.this,CartActivity.class);
                startActivity(cartDetails);

            }
        });
    }

}