package com.example.ecommerce.buyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.buyer.model.OrderModel;
import com.example.ecommerce.common.constant;
import com.example.ecommerce.seller.activity.OrderuserInfoActivity;
import com.example.ecommerce.seller.model.ItemsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartScreenAdapter extends RecyclerView.Adapter<CartScreenAdapter.MyViewHolder> {
    List<ItemsModel> data;
    List<OrderModel> order;
    Context context;
    FirebaseFirestore db;
    boolean flag;
    boolean check;

    public CartScreenAdapter(Context context, List<OrderModel> order, boolean flg,boolean chk)
    {
        this.context = context;
        this.order=order;
        flag=flg;
        check=chk;
        db = FirebaseFirestore.getInstance();
    }
    public CartScreenAdapter(Context context, List<ItemsModel> data) {
        this.context = context;
        this.data=data;
        flag=false;
        db = FirebaseFirestore.getInstance();
    }
    @Override
    public CartScreenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartscreenitem, parent, false);

        CartScreenAdapter.MyViewHolder vh = new CartScreenAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CartScreenAdapter.MyViewHolder holder, final int position) {

        if(flag){
            holder.status.setVisibility(View.VISIBLE);

            Glide.with(context).load(order.get(position).getImgurl()).into(holder.imageView);
            holder.name.setText("Product Name:"+order.get(position).getName());
            holder.price.setText("COP."+order.get(position).getPrice());
            holder.quantity.setText("items:"+order.get(position).getQuantity());
            holder.status.setText("Status:"+order.get(position).getStatus());
            if(order.get(position).getStatus().equals("Pending"))
                holder.cancel.setVisibility(View.VISIBLE);
            else
                holder.cancel.setVisibility(View.GONE);

            if(check){
                if(order.get(position).getStatus().equals("Pending")) {
                    holder.cancel.setVisibility(View.VISIBLE);
                    holder.accept.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.cancel.setVisibility(View.GONE);
                    holder.accept.setVisibility(View.GONE);
                }
                holder.cancel.setText("Reject");
                holder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.collection("Orders").document(order.get(position).getId())
                                .update("Status","Reject").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                order.get(position).setStatus("Reject");
                                notifyDataSetChanged();
                            }
                        });

                    }
                });



                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> user = new HashMap<>();
                        user.put("Status","Accept");

                        db.collection("Orders").document(order.get(position).getId())
                                .update("Status","Accept").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                order.get(position).setStatus("Accept");
                                ReduceQuantity(order.get(position).getQuantity(),order.get(position).getPID(),order.get(position).getId());
                            }
                        });
                    }
                });
            }
            else {
                holder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.collection("Orders").document(order.get(position).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                order.remove(position);
                                notifyDataSetChanged();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context, OrderuserInfoActivity.class);
                    intent.putExtra("name",order.get(position).getBname());
                    intent.putExtra("email",order.get(position).getBemail());
                    intent.putExtra("address",order.get(position).getBaddress());

                    context.startActivity(intent);
                }
            });
        }
        else{
            Glide.with(context).load(data.get(position).getImageUrl()).into(holder.imageView);
            holder.name.setText("Product Name:"+data.get(position).getName());
            holder.price.setText("COP."+data.get(position).getPrice());
            holder.quantity.setText("Items:"+data.get(position).getQuantity());
            holder.plus.setVisibility(View.VISIBLE);
            holder.minus.setVisibility(View.VISIBLE);
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(data.get(position).getQuantity()>0){
                        //data.get(position).setPrice(data.get(position).getPrice()+(data.get(position).getPrice()/data.get(position).getQuantity()));
                        //data.get(position).setQuantity(data.get(position).getQuantity()+1);

                        constant.cartItems.get(position).setPrice(data.get(position).getPrice()+(data.get(position).getPrice()/data.get(position).getQuantity()));
                        constant.cartItems.get(position).setQuantity(data.get(position).getQuantity()+1);
                        notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(context,"Out of Stock",Toast.LENGTH_SHORT).show();
                    }

                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.get(position).getQuantity()>1){
                       // data.get(position).setPrice(data.get(position).getPrice()-(data.get(position).getPrice()/data.get(position).getQuantity()));
                      //  data.get(position).setQuantity(data.get(position).getQuantity()-1);

                        constant.cartItems.get(position).setPrice(data.get(position).getPrice()-(data.get(position).getPrice()/data.get(position).getQuantity()));
                        constant.cartItems.get(position).setQuantity(data.get(position).getQuantity()-1);
                    }
                    else {
                       // data.remove(position);
                        constant.cartItems.remove(position);
                    }
                    notifyDataSetChanged();
                }
            });
        }

    }

    private void ReduceQuantity(final Long quantity, final String id,final String oid) {
        db.collection("Products").document(constant.Email).collection("Details")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(id)) {
                                    if (document.getLong("Quantity") >= quantity) {

                                    db.collection("Products").
                                            document(constant.Email).
                                            collection("Details")
                                            .document(id).
                                            update("Quantity", document.getLong("Quantity") - quantity)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    notifyDataSetChanged();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                    else {
                                        Toast.makeText(context, "Out Of Stock", Toast.LENGTH_SHORT).show();
                                        db.collection("Orders").document(oid)
                                                .update("Status","Reject").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {


                                            }
                                        });
                                    }

                                }
                            }

                        }
                    }
                });
    }


    @Override
    public int getItemCount() {
        if(flag)
            return order.size();
        else
            return data.size();

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,plus,minus; Button cancel,accept,info;
        TextView name,price,quantity,status;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.img);

            name= itemView.findViewById(R.id.name);
            price= itemView.findViewById(R.id.price);
            quantity= itemView.findViewById(R.id.quantity);
            status=itemView.findViewById(R.id.status);
            cancel=itemView.findViewById(R.id.cancel);
            accept=itemView.findViewById(R.id.accept);
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);

            cardView=itemView.findViewById(R.id.orderCard);

        }
    }
}