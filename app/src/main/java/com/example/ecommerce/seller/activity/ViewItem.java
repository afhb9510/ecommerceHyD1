package com.example.ecommerce.seller.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.common.constant;
import com.example.ecommerce.common.loader;
import com.example.ecommerce.seller.adapter.itemviewAdapter;
import com.example.ecommerce.seller.model.ItemsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewItem extends AppCompatActivity {
   SearchView searchView;
   RecyclerView recyclerView;
   List<ItemsModel> data=new ArrayList<>();
    loader loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewitem);
        init();
        fetchRecord();

    }

    private void fetchRecord() {
        loading.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products").document(constant.Email).collection("Details")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG1", document.getId() + " => " + document.getData());
                                data.add(new ItemsModel(document.getString("Name"),  document.getLong("Quantity"),  document.getLong("Price"),document.getString("Image"),document.getId()));
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(ViewItem.this));
                            itemviewAdapter customAdapter = new itemviewAdapter(ViewItem.this,data);
                            recyclerView.setAdapter(customAdapter);
                            loading.dismiss();
                            if(data.size()==0)
                                findViewById(R.id.found).setVisibility(View.VISIBLE);
                            else
                                findViewById(R.id.found).setVisibility(View.INVISIBLE);
                        } else {
                            Log.w("TAG1", "Error getting documents.", task.getException());
                            Toast.makeText(ViewItem.this,"Error==>"+task.getException(),Toast.LENGTH_SHORT).show();

                            loading.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.dismiss();
                Toast.makeText(ViewItem.this,"Error==>"+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void init() {
        loading=new loader(this,getWindow().getDecorView().getRootView());
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.cart).setVisibility(View.GONE);
        recyclerView=findViewById(R.id.recyleview);

    }
}