package com.example.ecommerce.seller.activity;

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

public class CheckOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<OrderModel> data=new ArrayList<>();
    List<String> products=new ArrayList<>();
    loader loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);
        init();
    }

    private void init() {
        if(getIntent().getStringExtra("type").equals("seller")) {
            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        else
            findViewById(R.id.back).setVisibility(View.INVISIBLE);

        findViewById(R.id.cart).setVisibility(View.GONE);
        recyclerView=findViewById(R.id.recyleview);
        db = FirebaseFirestore.getInstance();
        loading=new loader(this,getWindow().getDecorView().getRootView());
        fetchProduct();

    }

    private void fetchProduct() {
        loading.show();
        db.collection("Products").document(constant.Email).collection("Details")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                products.add(document.getId());
                            }
                            fetchOrder();
                        }
                        else {
                            loading.dismiss();
                            Toast.makeText(CheckOrder.this,"Error==>"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CheckOrder.this,"Error==>"+e.getMessage(),Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });

    }

    private void fetchOrder() {

        db.collection("Orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(products.contains(document.getString("ID")))
                                    data.add(new OrderModel(document.getString("Name"),document.getString("Email"),document.getString("Address"),document.getString("BImg"),document.getId(),document.getString("PName"),document.getString("Status"),document.getString("Image"),document.getLong("Quantity"),document.getLong("Price"),document.getString("ID")));

                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(CheckOrder.this));
                            CartScreenAdapter customAdapter = new CartScreenAdapter(CheckOrder.this, data,true,true);
                            recyclerView.setAdapter(customAdapter);
                            loading.dismiss();
                            if(data.size()==0)
                                findViewById(R.id.found).setVisibility(View.VISIBLE);
                            else
                                findViewById(R.id.found).setVisibility(View.INVISIBLE);
                        }
                        else {
                            loading.dismiss();
                            Toast.makeText(CheckOrder.this,"Error==>"+task.getException(),Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                Toast.makeText(CheckOrder.this,"Error==>"+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}