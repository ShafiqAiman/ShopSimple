package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    public static final String TAG = "TAG";
    //Variables
    TextInputLayout regName, regUsername, regPhoneNo, regEmail, regPassword;
    Button callLogIn, registerBtn;
    TextView regToLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //private DatabaseReference userRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPhoneNo = findViewById(R.id.reg_phoneNo);
        regPassword = findViewById(R.id.reg_password);
        registerBtn = findViewById(R.id.regBtn);
        regToLoginBtn = findViewById(R.id.login_btn);



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        /*if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }*/

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String email = regEmail.getEditText().getText().toString();
                final String password = regPassword.getEditText().getText().toString();
                final String fullName = regName.getEditText().getText().toString();
                final String phoneNo = regPhoneNo.getEditText().getText().toString();

                if(TextUtils.isEmpty(email)){
                    regEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    regPassword.setError("Password is Required");
                    return;
                }

                if(password.length() < 6){
                    regPassword.setError("Password must be more than 6 characters.");
                    return;
                }

                //register user to firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if(task.isSuccessful()){
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("Name",fullName);
                                user.put("Email",email);
                                user.put("PhoneNo",phoneNo);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>(){
                                    @Override
                                    public void onSuccess(Void aVoid){
                                        Log.d(TAG, "onSuccess: user Profile is created for"+ userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener(){
                                    @Override
                                    public void onFailure(@NonNull Exception e){
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });

                                        //send verification link
                                        FirebaseUser User = fAuth.getCurrentUser();

                                        User.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>(){
                                            @Override
                                            public void onSuccess(Void aVoid){
                                                Toast.makeText(SignUp.this,"Verification Email has been sent.",Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener(){
                                            @Override
                                            public void onFailure(@NonNull Exception e){
                                                Log.d("tag","onFailure: Email not sent" + e.getMessage());
                                            }
                                        });


                                        Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                    }else{
                                        Toast.makeText(SignUp.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            }
                    });
                }


            });

        callLogIn = findViewById(R.id.login_btn);

        callLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }

        });

         }



}
