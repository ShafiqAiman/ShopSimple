package com.example.shopsimpleapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class Dashboard extends AppCompatActivity implements View.OnClickListener{

    Button  verifyBtn;
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
        //Toast.makeText(Dashboard.this, userId, Toast.LENGTH_SHORT).show();
        final FirebaseUser user = fAuth.getCurrentUser();

        if (!user.isEmailVerified()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
            builder.setMessage("Please verify your email to continue shopping ");
            builder.setTitle("Email Verification");
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Dashboard.this,Login.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

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


        @Override
        public void onBackPressed() {

            Intent intent = new Intent(this,Dashboard.class);
            startActivity(intent);
            // This above line close correctly
        }

}


