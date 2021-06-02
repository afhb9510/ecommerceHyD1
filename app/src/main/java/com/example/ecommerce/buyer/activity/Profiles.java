package com.example.ecommerce.buyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.common.Registration;
import com.example.ecommerce.common.constant;
import com.google.firebase.auth.FirebaseAuth;

public class Profiles extends AppCompatActivity {
    TextView name,email;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        bottombar();
       init();
    }
    private void bottombar() {
        findViewById(R.id.back).setVisibility(View.INVISIBLE);
        findViewById(R.id.categorytxt).setVisibility(View.INVISIBLE);
        findViewById(R.id.profiletxt).setVisibility(View.VISIBLE);
        findViewById(R.id.hometxt).setVisibility(View.INVISIBLE);
        findViewById(R.id.category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Profiles.this,SeeOrder.class));
                overridePendingTransition(0,0);
                finish();
            }
        });
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profiles.this,BuyerHome.class));
                overridePendingTransition(0,0);
                finish();
            }
        });
    }

    private void init() {
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        name.setText(constant.FName+" "+constant.LName);
        email.setText(constant.Email);
        logout=findViewById(R.id.cart);
        logout.setImageResource(R.drawable.ic_baseline_exit_to_app_24);
        logout.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Profiles.this, Registration.class));
        finishAffinity();}});


    }
}