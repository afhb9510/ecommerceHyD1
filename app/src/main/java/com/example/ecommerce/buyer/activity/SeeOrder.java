package com.example.ecommerce.buyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.buyer.adapter.CartScreenAdapter;
import com.example.ecommerce.buyer.model.OrderModel;
import com.example.ecommerce.common.constant;
import com.example.ecommerce.common.loader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SeeOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<OrderModel> data=new ArrayList<>();
    loader loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeorder);
        init();
        bottombar();
        
    }
    private void bottombar() {
        findViewById(R.id.categorytxt).setVisibility(View.VISIBLE);
        findViewById(R.id.profiletxt).setVisibility(View.INVISIBLE);
        findViewById(R.id.hometxt).setVisibility(View.INVISIBLE);
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SeeOrder.this,BuyerHome.class));
                overridePendingTransition(0,0);
                finish();
            }
        });
        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SeeOrder.this,Profiles.class));
                overridePendingTransition(0,0);
                finish();
            }
        });
    }
    private void init() {
        findViewById(R.id.back).setVisibility(View.INVISIBLE);

        findViewById(R.id.cart).setVisibility(View.GONE);
        recyclerView=findViewById(R.id.recyleview);
        db = FirebaseFirestore.getInstance();
        loading=new loader(this,getWindow().getDecorView().getRootView());
        loading.show();
        fetchOrder();
    }

    private void fetchOrder() {

        db.collection("Orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              if(document.getString("Email").equals(constant.Email))
                                  data.add(new OrderModel(document.getId(),document.getString("PName"),document.getString("Status"),document.getString("Image"),document.getLong("Quantity"),document.getLong("Price"),document.getString("ID")));

                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(SeeOrder.this));
                            CartScreenAdapter customAdapter = new CartScreenAdapter(SeeOrder.this, data,true,false);
                            recyclerView.setAdapter(customAdapter);
                            loading.dismiss();
                            if(data.size()==0)
                                findViewById(R.id.found).setVisibility(View.VISIBLE);
                            else
                                findViewById(R.id.found).setVisibility(View.INVISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SeeOrder.this,"Error==>"+e.getMessage(),Toast.LENGTH_SHORT).show();
                loading.dismiss();

            }
        });
    }
}