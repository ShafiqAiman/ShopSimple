package com.example.shopsimpleapplication;

import android.graphics.Bitmap;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Date;

public class babi {
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

    StorageReference storageReference;
    DatabaseReference databaseReference;


}
