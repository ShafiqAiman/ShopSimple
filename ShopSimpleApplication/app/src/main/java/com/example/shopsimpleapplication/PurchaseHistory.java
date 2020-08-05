package com.example.shopsimpleapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import org.w3c.dom.Text;

public class PurchaseHistory extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    TextView uphone;
    Button toastCall, pay;
    private static final String TAG = "paymentExample";

    public static final String PAYPAL_KEY = "AR3w-xzDaAq8p815GtutTJiuuCyLkMAIZ8VAVL8DwVflii_os8ItcqoBIJLgowbHog1QinPPSXDMHyvc";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static PayPalConfiguration config;
    PayPalPayment thingsToBuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        uphone = findViewById(R.id.phone);

        fAuth = FirebaseAuth.getInstance();
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


    pay =(Button)findViewById(R.id.payBtn);
    pay.setOnClickListener(new View.OnClickListener()

    {

        @Override
        public void onClick (View v){

        MakePayment();
    }

    });

    configPayPal();

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

        thingsToBuy = new PayPalPayment(new BigDecimal(String.valueOf("10.45")), "MYR", "Payment", PayPalPayment.PAYMENT_INTENT_SALE);
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
}

