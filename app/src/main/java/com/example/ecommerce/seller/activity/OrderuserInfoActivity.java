package com.example.ecommerce.seller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerce.R;

public class OrderuserInfoActivity extends AppCompatActivity {

    TextView name, email, address;
    private ImageButton backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderuser_info);
        name = findViewById(R.id.name);
        backBtn = findViewById(R.id.backBtn);
        email = findViewById(R.id.email);

        address = findViewById(R.id.address);
        name.setText(name.getText().toString() + getIntent().getStringExtra("Nombre"));
        email.setText(email.getText().toString() + getIntent().getStringExtra("Correo"));
        address.setText(address.getText().toString() + getIntent().getStringExtra("Direccion"));
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    }
