package com.example.shopsimpleapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopsimpleapplication.Model.BraintreeToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class Login extends AppCompatActivity {

    TextInputLayout  regEmail, regPassword;
    EditText resetMail;
    Button callSignUp, callDashboard, forgotPassBtn;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        fAuth = FirebaseAuth.getInstance();




        callDashboard = findViewById(R.id.loginBtn);

        callDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = regEmail.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();

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

                //authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "User Logged In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Dashboard.class));

                        }else{
                            Toast.makeText(Login.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                //Intent intent = new Intent(Login.this, Dashboard.class);
                //startActivity(intent);
            }

        });

        callSignUp = findViewById(R.id.signup_screen);

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }

        });

        forgotPassBtn = findViewById(R.id.forgotPass_btn);

        forgotPassBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email to Receive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract email and send reset password link

                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>(){
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent to Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener(){
                            @Override
                            public void onFailure(@NonNull Exception e){
                                Toast.makeText(Login.this,"Error ! Reset Link s NOt Sent." + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //redirect bac to login n close dialog
                    }
                });

                passwordResetDialog.create().show();
            }

        });

    }

    //private void startActivity(Intent intent) {
    }
