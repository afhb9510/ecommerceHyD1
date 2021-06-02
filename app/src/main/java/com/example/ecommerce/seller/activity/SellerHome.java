package com.example.ecommerce.seller.activity;

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

public class SellerHome extends AppCompatActivity {
    TextView email,name;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerhome);
        findViewById(R.id.back).setVisibility(View.INVISIBLE);

        email=findViewById(R.id.email);
        name=findViewById(R.id.name);
        email.setText(constant.Email);
        name.setText(constant.FName+" "+constant.LName);
        logout=findViewById(R.id.cart);
        logout.setImageResource(R.drawable.ic_baseline_exit_to_app_24);
        logout.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(SellerHome.this, Registration.class));
            finishAffinity();}});
    }

    public void addItem(View view) {
        startActivity(new Intent(this, AddItem.class));
    }

    public void viewItem(View view) {
        startActivity(new Intent(this, ViewItem.class));
    }

    public void checkOrder(View view) {
        startActivity(new Intent(this, CheckOrder.class).putExtra("type","seller"));
    }


}