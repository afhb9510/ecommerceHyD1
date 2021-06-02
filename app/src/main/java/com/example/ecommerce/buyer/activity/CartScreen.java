package com.example.ecommerce.buyer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.buyer.adapter.CartScreenAdapter;
import com.example.ecommerce.common.constant;
import com.example.ecommerce.common.loader;
import com.example.ecommerce.seller.model.ItemsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CartScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore db;
    loader loading;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartscreen);
        init();
        loading=new loader(this,getWindow().getDecorView().getRootView());

    }

    private void init() {
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.cart).setVisibility(View.GONE);
        recyclerView=findViewById(R.id.recyleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartScreen.this));
        CartScreenAdapter customAdapter = new CartScreenAdapter(CartScreen.this, constant.cartItems);
        recyclerView.setAdapter(customAdapter);
    }

    public void order(View view) {
        if(constant.cartItems.size()==0)
            Toast.makeText(this,"Primero agregue el producto al carrito de compras",Toast.LENGTH_SHORT).show();
        else {
            loading.show();
            for (ItemsModel items : constant.cartItems) {
                Map<String, Object> user = new HashMap<>();
                user.put("Name", constant.FName + " " + constant.LName);
                user.put("Email", constant.Email);
                user.put("Address", constant.Address);
                user.put("BImg", constant.Image);
                user.put("ID", items.getId());
                user.put("PName", items.getName());
                user.put("Price", items.getPrice());
                user.put("Quantity", items.getQuantity());
                user.put("Status", "Pending");
                user.put("Image", items.getImageUrl());
                db.collection("Orders")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CartScreen.this, "Error==>" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            constant.cartItems.clear();
            Toast.makeText(this, "Orden exitosa", Toast.LENGTH_SHORT).show();
            loading.dismiss();
            finish();
        }
    }
}