package com.example.ecommerce.buyer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.common.Registration;
import com.example.ecommerce.common.constant;
import com.example.ecommerce.seller.model.ItemsModel;

public class Details extends AppCompatActivity {
    ImageView imageView;
    TextView name, price, quantity, description;
    ItemsModel data;
    Long pr, qn;
    String Id;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        init();
    }

    private void init() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Details.this, CartScreen.class));
            }
        });
        data = (ItemsModel) getIntent().getSerializableExtra("details");
        imageView = findViewById(R.id.img);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);

        button = findViewById(R.id.button);


        Glide.with(this).load(data.getImageUrl()).into(imageView);

        name.setText(data.getName());
        price.setText("COP." + data.getPrice());
        quantity.setText(data.getQuantity() + " item(s)");

        qn = data.getQuantity();
        pr = data.getPrice();
        Id = data.getId();

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addToCart(v);
        }
    });
    }



    public void addToCart(View view) {
        if (constant.Email.isEmpty()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Need for login to order");
            alertDialogBuilder.setPositiveButton("Login",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(Details.this, Registration.class));
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            if (data.getQuantity() > 0) {
                try {

                    if (constant.cartItems.size() == 0) {
                        constant.cartItems.add(data);
                        constant.cartItems.get(constant.cartItems.size() - 1).setQuantity(Long.valueOf(1));

                        Toast.makeText(this, "Add To Cart1", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean flg = true;
                        for (int i = 0; i < constant.cartItems.size(); i++) {
                            Log.e("id_", Id);
                            if (constant.cartItems.get(i).getId() == Id) {
                                if (constant.cartItems.get(i).getQuantity() >= qn) {
                                    Toast.makeText(this, "Out of stock", Toast.LENGTH_SHORT).show();
                                    flg = false;
                                    break;
                                }
                                constant.cartItems.get(i).setQuantity(constant.cartItems.get(i).getQuantity() + 1);
                                constant.cartItems.get(i).setPrice((constant.cartItems.get(i).getPrice() + pr));

                                flg = false;
                                Toast.makeText(this, "Add To Cart2", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }
                        if (flg) {
                            constant.cartItems.add(data);
                            constant.cartItems.get(constant.cartItems.size() - 1).setQuantity(Long.valueOf(1));

                            Toast.makeText(this, "Add To Cart3", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    Log.d("IDS1", e.getMessage());
                }
            } else {
                Toast.makeText(this, "Out of Stock", Toast.LENGTH_SHORT).show();
            }
        }
    }

}