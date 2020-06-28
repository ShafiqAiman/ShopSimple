package com.ruth.checkmeout.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ruth.checkmeout.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBarDrawerToggle checkMeOutToggle;

    @BindView(R.id.activity_main)DrawerLayout checkMeOutDrawerLayout;
    @BindView(R.id.navigation) NavigationView checkMeOutNavigation;
    private Class fragmentClass;
    private Fragment fragment = null;

    @BindView(R.id.txtGoToScan)
    TextView txtGoToScan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ButterKnife.bind(this);
        txtGoToScan.setOnClickListener(this);

        checkMeOutToggle=new ActionBarDrawerToggle(this,checkMeOutDrawerLayout,R.string.Open,R.string.Close);
        checkMeOutToggle.setDrawerIndicatorEnabled(true);
        checkMeOutDrawerLayout.addDrawerListener(checkMeOutToggle);
        checkMeOutToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        checkMeOutNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();
                if(id==R.id.home||id==R.id.about){

                    Toast.makeText(MainActivity.this, "Coming Soon",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);

                }
                else {
                    switch (id){

                        case R.id.expenses:
                            Toast.makeText(MainActivity.this, "Expenses",Toast.LENGTH_SHORT).show();
                            fragmentClass= ExpensesFragment.class;break;

                        case R.id.account:
                            Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();
                            fragmentClass= MyAccountFragment.class;break;
                        case R.id.signInLink:
                            Toast.makeText(MainActivity.this, "Log In",Toast.LENGTH_SHORT).show();
                            fragmentClass= LogInFragment.class;break;
                        case R.id.signOutLink:
                            Toast.makeText(MainActivity.this, "Sign Out",Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            fragmentClass= LogInFragment.class;
                            break;
                        case R.id.shop:
                            Toast.makeText(MainActivity.this, "Shop",Toast.LENGTH_SHORT).show();
                            fragmentClass= ShopFragment.class;break;


                        default:
                            return true;
                    }
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


                }
                checkMeOutDrawerLayout.closeDrawers();


                return true;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                checkMeOutDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v==txtGoToScan){
            fragmentClass=ScanningFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();

            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager= getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

    }

}
