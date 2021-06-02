package com.example.ecommerce.seller.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.buyer.activity.Details;
import com.example.ecommerce.buyer.activity.SeeOrder;
import com.example.ecommerce.buyer.adapter.CartScreenAdapter;
import com.example.ecommerce.buyer.model.OrderModel;
import com.example.ecommerce.common.Registration;
import com.example.ecommerce.common.constant;
import com.example.ecommerce.seller.model.ItemsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class itemviewAdapter extends RecyclerView.Adapter<itemviewAdapter.MyViewHolder> {

    Context context;
    List<ItemsModel> data;
    FirebaseFirestore db;
    public itemviewAdapter(Context context, List<ItemsModel> data) {
        this.context = context;
        this.data=data;
        db = FirebaseFirestore.getInstance();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selleritemview, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Glide.with(context).load(data.get(position).getImageUrl()).into(holder.imageView);
        holder.name.setText(data.get(position).getName());

        holder.quantity.setText("Items:"+data.get(position).getQuantity());
        holder.price.setText("COP."+data.get(position).getPrice());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Esta seguro de eliminar este producto?");
                alertDialogBuilder.setPositiveButton("si",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                db.collection("Products").document(constant.Email).collection("Details")
                                        .document(data.get(position).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"Successfully Delete",Toast.LENGTH_SHORT).show();
                                        Alsodeleteorders(data.get(position).getId());
                                        data.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,"Intente nuevamente\n"+e.getMessage(),Toast.LENGTH_SHORT).show();

                                    }
                                });
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
        });

    }

    private void Alsodeleteorders(final String id) {
        db.collection("Orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("docid",document.getId());
                                Log.e("pid",id);

                                if(document.getString("ID").equals(id)&&document.getString("Status")!="Accept")
                                    db.collection("Orders").document(document.getId()).delete();
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Error==>"+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
         ImageView imageView,delete;
         TextView name,quantity,price ;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.img);
            delete= itemView.findViewById(R.id.delete);
            name= itemView.findViewById(R.id.name);

            price= itemView.findViewById(R.id.price);
            quantity= itemView.findViewById(R.id.quantity);

        }
    }

}
