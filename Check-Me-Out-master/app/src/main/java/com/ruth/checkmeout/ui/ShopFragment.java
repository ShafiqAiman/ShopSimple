package com.ruth.checkmeout.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ruth.checkmeout.Constants;
import com.ruth.checkmeout.R;
import com.ruth.checkmeout.adapters.ShopFragmentAdapter;
import com.ruth.checkmeout.models.CheckMeOutSearchResponse;
import com.ruth.checkmeout.models.Expense;
import com.ruth.checkmeout.networks.CheckMeOutApi;
import com.ruth.checkmeout.networks.CheckMeOutClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


@RequiresApi(api = Build.VERSION_CODES.O)
public class ShopFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.btnScan)
    FloatingActionButton btnScan;
    @BindView(R.id.cartView)
    RecyclerView cartView;
    @BindView(R.id.btnCheckOut)
    Button btnCheckOut;
    private Class fragmentClass;
    private ShopFragmentAdapter mAdapter;
    private CheckMeOutSearchResponse bundle;
    private Fragment fragment = null;
    private ArrayList<String> codes=new ArrayList<>();
    private List<CheckMeOutSearchResponse> items=new ArrayList<>();
    private int total=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this,view);

        Bundle getCode=this.getArguments();
        Log.i(TAG, "Code size : "+ codes.size());
        if(getCode!=null){
            if(codes.size()==0){
                codes=getCode.getStringArrayList("codes");
            }
            else {
                codes.addAll(getCode.getStringArrayList("codes"));

            }

        }
        Log.i(TAG, "Code size : "+ codes.size());
        if(codes.size()>0){
            for(String code : codes){
                CheckMeOutApi client= CheckMeOutClient.getItem();
                Call<CheckMeOutSearchResponse> call=client.getItem(code);
                call.enqueue(new Callback<CheckMeOutSearchResponse>(){

                    @Override
                    public void onResponse(Call<CheckMeOutSearchResponse> call, Response<CheckMeOutSearchResponse> response) {
                        //showSuccessfulMessage();
                        if(response.isSuccessful()&&response.body()!=null){

                            bundle=new CheckMeOutSearchResponse();
                            bundle.setId(response.body().getId());
                            bundle.setPrice(response.body().getPrice());
                            bundle.setName(response.body().getName());
                            bundle.setCode(response.body().getCode());
                            items.add(bundle);
                            mAdapter=new ShopFragmentAdapter(items,getContext());
                            cartView.setAdapter(mAdapter);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
                            cartView.setLayoutManager(layoutManager);
                            cartView.setHasFixedSize(true);

                        }
                        else {
                            showUnsuccessfulMessage();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckMeOutSearchResponse> call, Throwable t) {
                        showFailureMessage();

                    }
                });

            }

            showItems();

        }



        btnScan.setOnClickListener(this);
        btnCheckOut.setOnClickListener(this);


        return view;
    }

    private void showItems() {
        Log.i(TAG, "showSuccessfulMessage: "+codes.size());
        cartView.setVisibility(View.VISIBLE);
    }
    private void showUnsuccessfulMessage() {

        Toast.makeText(getContext(),"The item scanned does not exist add it to the api provided in the ReadMe",Toast.LENGTH_LONG).show();
    }

    private void showFailureMessage() {

        Toast.makeText(getContext(),"Check internet Connection",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        if(v==btnScan){

            fragmentClass=ScanningFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();

            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        }
        else if(v==btnCheckOut){
            Toast.makeText(getContext(),"Coming Soon",Toast.LENGTH_LONG).show();
            for(int i=0;i<items.size();i++){
                total=total+ items.get(i).getPrice();
            }
            Expense entry=new Expense(total);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
                String uid = user.getUid();
                DatabaseReference restaurantRef = FirebaseDatabase
                        .getInstance()
                        .getReference(Constants.FIREBASE_CHILD_EXPENSES).child(uid);;
                restaurantRef.push().setValue(entry);
                Toast.makeText(getContext(), "Complete", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

            }
            else {
                Toast.makeText(getContext(),"Make sure you are logged in",Toast.LENGTH_LONG).show();
            }


        }

    }



}
