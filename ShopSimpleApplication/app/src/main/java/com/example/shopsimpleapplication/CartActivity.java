package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shopsimpleapplication.ViewHolder.CartViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button pay,ClearAll;
    private TextView TotalAmount;

    private double TotalPrice = 0.00;

    private String[] PIDArray = new String[0];
    private String[] PNameArray = new String[0];
    private String[] PPriceArray = new String[0];
    private String[] PQuantityArray = new String[0];

    private int childCount = 0;
    private int m = 0;

    private static String Amount = "";
    public static final String EXTRA_NUMBER= "com.example.shopsimpleapplication.EXTRA_TEXT";

    public static final String PAYPAL_KEY = "AR3w-xzDaAq8p815GtutTJiuuCyLkMAIZ8VAVL8DwVflii_os8ItcqoBIJLgowbHog1QinPPSXDMHyvc";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static PayPalConfiguration config;
    PayPalPayment thingsToBuy;


    FirebaseRecyclerAdapter<com.example.shopsimpleapplication.Model.Cart, CartViewHolder>adapter;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseDatabase database;
    DatabaseReference Cart, delete;
    String a = "";
    String CName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //pay = (Button) findViewById(R.id.payBtn);
        TotalAmount = (TextView) findViewById(R.id.total_price);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                CName = documentSnapshot.getString("Name");
                a = documentSnapshot.getString("PhoneNo");
                database = FirebaseDatabase.getInstance();
                Cart = database.getReference("Cart").child(a);
                Cart.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        childCount = (int) snapshot.getChildrenCount();
                        PIDArray = new String[childCount];
                        PNameArray = new String[childCount];
                        PPriceArray = new String[childCount];
                        PQuantityArray = new String[childCount];

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                loadCart();

            }
        });


        ClearAll =(Button)findViewById(R.id.clearCart);
        ClearAll.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick (View v){

               Cart.removeValue();
            }

        });

        pay =(Button)findViewById(R.id.payBtn);
        pay.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick (View v){

                MakePayment();
            }

        });

        configPayPal();

        //fx to empty the cart

    }

    private void configPayPal() {

        config = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(PAYPAL_KEY)
                .merchantName("Paypal Login")
                .merchantPrivacyPolicyUri(Uri.parse("https://wwww.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://wwww.example.com/legal"));

    }

    private void MakePayment() {

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        thingsToBuy = new PayPalPayment(new BigDecimal(String.valueOf(""+ TotalPrice)), "MYR", "Payment", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent payment = new Intent(this, PaymentActivity.class);
        payment.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);
        payment.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startActivityForResult(payment, REQUEST_CODE_PAYMENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm != null) {

                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject().toString(4));
                        Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();

                        DecimalFormat df = new DecimalFormat("#,###,##0.00");
                        //priceTotal = df.format(TotalPrice);
                        //Double priceTotal = Double.parseDouble(String.valueOf(TotalPrice));
                        Intent intent = new Intent(this,receipt.class);
                        intent.putExtra(EXTRA_NUMBER, df.format(TotalPrice));
                        intent.putExtra("pID",PIDArray);
                        intent.putExtra("pName",PNameArray);
                        intent.putExtra("pPrice",PPriceArray);
                        intent.putExtra("pQuantity",PQuantityArray);
                        intent.putExtra("count",String.valueOf(childCount));
                        intent.putExtra("CName",CName);
                        intent.putExtra("CPhone",a);
                        startActivity(intent);
                        Cart.removeValue();

                    } catch (JSONException e) {

                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

                    }


                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment has been canceled", Toast.LENGTH_LONG).show();
            } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "error occurred", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization authorization = data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (authorization != null) {
                    try {
                        Log.i("FuturePaymentExample", authorization.toJSONObject().toString(4));
                        String authorization_code = authorization.getAuthorizationCode();
                        Log.d("FuturePaymentExample", authorization_code);

                        Log.e("paypal", "future payment code received from PayPal  :" + authorization_code);

                    } catch (JSONException e) {
                        Toast.makeText(this, "Failure Occurred", Toast.LENGTH_LONG).show();
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred:  ", e);

                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "payment has been cancelled", Toast.LENGTH_LONG).show();
                Log.d("FuturePaymentExample", "The user cancelled.");
            } else if (requestCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "error occurred", Toast.LENGTH_LONG).show();
                Log.d("FuturePaymentExample", "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs");
            }
        }
    }


    private void loadCart(){
        adapter = new FirebaseRecyclerAdapter<com.example.shopsimpleapplication.Model.Cart, CartViewHolder>(com.example.shopsimpleapplication.Model.Cart.class, R.layout.cart_items_layout, CartViewHolder.class, Cart) {
            @Override
            protected void populateViewHolder(CartViewHolder cartViewHolder, final com.example.shopsimpleapplication.Model.Cart cart, final int i) {
                cartViewHolder.productNAME.setText(cart.getName());
                cartViewHolder.productPRICE.setText("RM "+cart.getPrice());
                cartViewHolder.productQUANTITY.setText(cart.getQuantity());

                Double DProductPrice = ((Double.valueOf(cart.getPrice()))) * (Double.valueOf(cart.getQuantity()));
                TotalPrice = TotalPrice + DProductPrice;
                DecimalFormat df2 = new DecimalFormat("#,###,##0.00");

                PIDArray[m] = cart.getId();
                PNameArray[m] = cart.getName();
                PPriceArray[m] = cart.getPrice();
                PQuantityArray[m] = cart.getQuantity();

                m++;

                TotalAmount.setText("Total Price = RM"+String.valueOf(df2.format(TotalPrice)));
                //final Cart local = cart;

                cartViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cart.child(cart.getId()).removeValue();
                        Toast.makeText(CartActivity.this,"This item is removed.",Toast.LENGTH_SHORT).show();

                        Double DProductPrice = ((Double.valueOf(cart.getPrice()))) * (Double.valueOf(cart.getQuantity()));
                        TotalPrice = TotalPrice - DProductPrice;
                        DecimalFormat df2 = new DecimalFormat("#,###,##0.00");
                        TotalAmount.setText("Total Price = RM"+String.valueOf(df2.format(TotalPrice)));
                        //Amount = TotalAmount.getText().toString();
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }


}
