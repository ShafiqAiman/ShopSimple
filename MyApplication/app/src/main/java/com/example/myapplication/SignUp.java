package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import Model.User;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtMemberCard,edtName,edtPassword;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtMemberCard = (MaterialEditText) findViewById(R.id.edtMembercard);
        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if member card is used
                        if (dataSnapshot.child(String.valueOf(edtMemberCard.getText())).exists()){
                            Toast.makeText(SignUp.this,"Member Card already registered!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            User user = new User(edtName.getText().toString(),edtPassword.getText().toString());
                            table_user.child(edtMemberCard.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this,"Sign up successfully!",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}