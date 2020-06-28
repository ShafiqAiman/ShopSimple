package com.ruth.checkmeout.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ruth.checkmeout.Constants;
import com.ruth.checkmeout.R;
import com.ruth.checkmeout.adapters.ExpenseAdapter;
import com.ruth.checkmeout.models.Expense;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ExpensesFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.expenseList)
    ListView expenseList;
    private ArrayList<Expense> expenses = new ArrayList<>();

//    private String[] months=new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
//    private int[] expenses=new int[]{1000,1800,2000,3000,5000,1000,4990,1234,2000,1299,4267,7464};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_expenses,container,false);
        ButterKnife.bind(this,view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String uid = user.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_EXPENSES).child(uid);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        expenses.add(snapshot.getValue(Expense.class));
                    }
                    Log.i(TAG, "retrieved Number: "+ expenses.size());
                    ExpenseAdapter adapter1=new ExpenseAdapter(view.getContext(),android.R.layout.simple_list_item_1,expenses);
                    expenseList.setAdapter(adapter1);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            Toast.makeText(getContext(),"Make Sure you are logged in",Toast.LENGTH_LONG).show();
        }



        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
