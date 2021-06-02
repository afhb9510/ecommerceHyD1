package com.example.ecommerce.buyer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.buyer.adapter.HomeAdapter;
import com.example.ecommerce.common.Registration;
import com.example.ecommerce.common.constant;
import com.example.ecommerce.common.loader;
import com.example.ecommerce.seller.model.ItemsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuyerHome extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ItemsModel> data=new ArrayList<>();
    loader loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottombar();
        init();
        recyclerView =  findViewById(R.id.recyleview);
         loading=new loader(this,getWindow().getDecorView().getRootView());
        loading.show();
        fetchSeller();
    }

    private void bottombar() {
        findViewById(R.id.categorytxt).setVisibility(View.INVISIBLE);
        findViewById(R.id.profiletxt).setVisibility(View.INVISIBLE);
        findViewById(R.id.hometxt).setVisibility(View.VISIBLE);
        findViewById(R.id.category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(constant.Email.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BuyerHome.this);
                    alertDialogBuilder.setMessage("Necesita realizar el login para ver las ordenes");
                    alertDialogBuilder.setPositiveButton("Login",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    startActivity(new Intent(BuyerHome.this, Registration.class));
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {
                    startActivity(new Intent(BuyerHome.this, SeeOrder.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        });
        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(constant.Email.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BuyerHome.this);
                    alertDialogBuilder.setMessage("Necesita realizar el login para ver las ordenes");
                    alertDialogBuilder.setPositiveButton("Login",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    startActivity(new Intent(BuyerHome.this, Registration.class));
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else{
                startActivity(new Intent(BuyerHome.this,Profiles.class));
                overridePendingTransition(0,0);
                finish();}
            }
        });
    }

    private void init() {
        findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { startActivity(new Intent(BuyerHome.this, CartScreen.class)); }});
        findViewById(R.id.back).setVisibility(View.GONE);
        recyclerView =  findViewById(R.id.recyleview);
        recyclerView=findViewById(R.id.recyleview);


    }

    private void fetchSeller() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Registration")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<String> doc=new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               if(document.getString("Type").equalsIgnoreCase("seller"))
                                   doc.add(document.getId());
                            }
                            if(doc.size()>0){
                             fetchRecord(doc);
                                findViewById(R.id.found).setVisibility(View.GONE);
                            }
                            else
                            {
                                loading.dismiss();
                                findViewById(R.id.found).setVisibility(View.VISIBLE);
                            }

                        } else {

                            Toast.makeText(BuyerHome.this,"Error=>"+task.getException(),Toast.LENGTH_SHORT).show();
                            loading.dismiss();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BuyerHome.this,"Error=>"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });;
    }


    private void fetchRecord(List<String> doc) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (String document:doc) {
            db.collection("Products").document(document).collection("Details")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    data.add(new ItemsModel(document.getString("Name") ,  document.getLong("Quantity"),  document.getLong("Price") ,document.getString("Image"),document.getId()));
                                }
                                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                                HomeAdapter customAdapter = new HomeAdapter(BuyerHome.this,data);
                                recyclerView.setAdapter(customAdapter);
                                if(data.size()==0)
                                    findViewById(R.id.found).setVisibility(View.VISIBLE);
                                else
                                    findViewById(R.id.found).setVisibility(View.INVISIBLE);
                            } else {
                                Log.w("TAG1", "Error con el documento.", task.getException());
                                Toast.makeText(BuyerHome.this,"Error=>"+task.getException(),Toast.LENGTH_SHORT).show();
                            }
                            loading.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BuyerHome.this,"Error=>"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            });
        }
    }
}