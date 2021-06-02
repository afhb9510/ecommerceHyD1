package com.example.ecommerce.seller.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.common.GetPath;
import com.example.ecommerce.common.constant;
import com.example.ecommerce.common.loader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AddItem extends AppCompatActivity {

    private static final int STORAGE_REQUEST_CODE = 300;
    String imgurl="";
    String downloadUrl="";
    String filename="";
    EditText name ,price,quantity ;
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    ImageView PImage;
    loader loading;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.cart).setVisibility(View.GONE);


        init();
    }

    private void init() {
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        PImage=findViewById(R.id.img);
        name=findViewById(R.id.name);
        price=findViewById(R.id.Price);
        quantity=findViewById(R.id.Quantity);
        loading=new loader(this,getWindow().getDecorView().getRootView());
        button=findViewById(R.id.addItem);
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        addItem(v);
    }
});
    }

    public void addItem(View view) {
        if(name.getText().toString().isEmpty()||price.getText().toString().isEmpty()||quantity.getText().toString().isEmpty())
            Toast.makeText(this,"Llena el formulario",Toast.LENGTH_SHORT).show();
        else if(Integer.parseInt(price.getText().toString())<1 || Integer.parseInt(quantity.getText().toString())<1)
            Toast.makeText(this,"El precio y la cantidad debe ser mayor a 1",Toast.LENGTH_SHORT).show();
        else
        {
            loading.show();
            if(downloadUrl.isEmpty())
             addImage();
            else
                addRecord();

        }
    }

    private void addImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }
        else
        {


            //your code

            Uri file = Uri.fromFile(new File(imgurl));
            final StorageReference riversRef = FirebaseStorage.getInstance().getReference("Images/"+System.currentTimeMillis()+".png");

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=uri.toString();
                                    addRecord();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddItem.this,"Intente nuevamente ==>"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    });

        }

    }

    private void addRecord() {
        Map<String, Object> user = new HashMap<>();
        user.put("Name", name.getText().toString());
        user.put("Price",Integer.parseInt(price.getText().toString()));
        user.put("Quantity",Integer.parseInt(quantity.getText().toString()));
        user.put("Image",downloadUrl);
        db.collection("Products").document(constant.Email).collection("Details")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddItem.this,"Successfully Add",Toast.LENGTH_SHORT).show();
                        clearFields();
                        loading.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddItem.this,"Try Again ==>"+e.getMessage(),Toast.LENGTH_SHORT).show();
               loading.dismiss();
                    }
                });


    }

    private void clearFields() {
        imgurl="";downloadUrl="";
        name.setText("");


        price.setText("");
        quantity.setText("");

        PImage.setImageResource(R.drawable.add_photo);
    }

    public void selectImg(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                imgurl= GetPath.getRealPath(this,imageUri);
                filename=GetPath.getFileName(this,imageUri);
                Bitmap bitmap = BitmapFactory.decodeFile(imgurl);
                PImage.setImageBitmap(bitmap);
                Log.d("pth",imgurl+","+filename);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Algo salio mal", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "No se ha elegido una imagen",Toast.LENGTH_LONG).show();
        }
    }
}