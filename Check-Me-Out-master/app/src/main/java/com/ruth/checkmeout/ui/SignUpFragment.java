package com.ruth.checkmeout.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.ruth.checkmeout.R;
import com.ruth.checkmeout.models.CheckMeOutSearchResponse;
import com.ruth.checkmeout.networks.CheckMeOutApi;
import com.ruth.checkmeout.networks.CheckMeOutClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment implements View.OnClickListener{
    private Class fragmentClass;
    private Fragment fragment = null;
    @BindView(R.id.signUpEmail)
    EditText signUpEmail;
    @BindView(R.id.signUpName)
    EditText signUpName;
    @BindView(R.id.signUpPassword)
    EditText signUpPassword;
    @BindView(R.id.signUpbutton)
    Button signUpButton;
    @BindView(R.id.txtGoToLogin)
    TextView txtGoToLogin;
    @BindView(R.id.signUpConfirmPassword)
    EditText signUpConfirmPassword;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private ProgressDialog mAuthProgressDialog;
    private String mName;
    private String TAG;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_sign_up,container,false);

        ButterKnife.bind(this,view);
        mAuth=FirebaseAuth.getInstance();
        signUpButton.setOnClickListener(this);
        createAuthStateListener();
        createAuthProcessDialog();
        txtGoToLogin.setOnClickListener(this);

        return view;
    }

    private void createAuthStateListener() {
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!= null){
                    Intent intent=new Intent(getContext(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();

                }

            }
        };
    }

    private void createAuthProcessDialog() {
        mAuthProgressDialog=new ProgressDialog(getContext());
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if(v==signUpButton){
            createNewUser();
        }
        if(v==txtGoToLogin){
            fragmentClass=LogInFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        }

    }

    private void createNewUser() {
        final String name=signUpName.getText().toString().trim();
        final String email=signUpEmail.getText().toString().trim();
        mName= signUpName.getText().toString().trim();
        String password=signUpPassword.getText().toString().trim();
        String confirmPassword=signUpConfirmPassword.getText().toString().trim();
        boolean validName=isValidName(name);
        boolean validEmail=isValidEmail(email);
        boolean validPassword=isValidPassword(password,confirmPassword);
        if(!validEmail||!validName||!validPassword)return;
        mAuthProgressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuthProgressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(),"Authentication Successful",Toast.LENGTH_SHORT).show();
                            createFirebaseUserProfile(task.getResult().getUser());
                        }else {
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createFirebaseUserProfile(FirebaseUser user) {
        UserProfileChangeRequest addProfileName=new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)
                .build();
        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, user.getDisplayName());
                        }
                    }
                });
    }

    private boolean isValidEmail(String email) {
        boolean isGoodEmail=email!=null&& Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if(!isGoodEmail){
            signUpEmail.setError("Please enter a valid email address");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            signUpPassword.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            signUpPassword.setError("Passwords do not match");
            signUpConfirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            signUpName.setError("Please enter your name");
            return false;
        }
        return true;
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
