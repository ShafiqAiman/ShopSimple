package com.example.addtocart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addToCartButton = (Button)findViewById(R.id.pd_add_to_cart_button);
        numberButton = (ElegantNumberButton)findViewById(R.id.number_btn);
        productImage = (ImageView)findViewById(R.id.product_image_details);
        productName = (TextView)findViewById(R.id.product_name_details);
        productDescription = (TextView)findViewById(R.id.product_description_details);
        productPrice = (TextView)findViewById(R.id.product_price_details);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingToCartList();
            }
        });
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat( "MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat( "HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
    }
}