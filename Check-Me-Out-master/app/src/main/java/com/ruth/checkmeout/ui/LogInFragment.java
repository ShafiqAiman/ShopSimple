package com.ruth.checkmeout.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ruth.checkmeout.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInFragment extends Fragment implements View.OnClickListener {
    private Class fragmentClass;
    private Fragment fragment = null;
    private String email;
    private String name;
    private String password;
    @BindView(R.id.activity_main)
    DrawerLayout dl;
    @BindView(R.id.navigation)
    NavigationView nv;
    @BindView(R.id.link_signup)
    TextView link_signup;
    @BindView(R.id.logInButton)
    Button logInButton;
    @BindView(R.id.logInEmail)
    EditText logInEmail;
    @BindView(R.id.logInPassword) EditText logInPassword;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private ProgressDialog mAuthProgressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_login,container,false);
        ButterKnife.bind(this,view);

        link_signup.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        createAuthProgressDialog();
        createAuthStateListener();


        return view;
    }

    private void createAuthStateListener() {
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                        if(user!=null){
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();

                        }
            }
        };
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(getContext());
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if(v==link_signup){
            fragmentClass=SignUpFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


        }
        else if(v==logInButton){
            loginWithPassword();
        }

    }

    private void loginWithPassword() {
        String email=logInEmail.getText().toString().trim();
        String password=logInPassword.getText().toString().trim();
        if (email.equals("")) {
            logInEmail.setError("Please enter your email");
            return;
        }
        if (password.equals("")) {
            logInPassword.setError("Password cannot be blank");
            return;
        }
        mAuthProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuthProgressDialog.dismiss();
                        if(!task.isSuccessful()){
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
