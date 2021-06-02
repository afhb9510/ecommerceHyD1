package com.example.ecommerce.common;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.ecommerce.R;
import com.example.ecommerce.buyer.activity.BuyerHome;
import com.example.ecommerce.seller.activity.SellerHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    String selector="buyer";
    ImageView leftside,rightside;
    TextView buyer,seller;
    ConstraintLayout lseller,lbuyer,sbuyer,sseller;
    EditText lb_email,lb_password,sb_fname,sb_lname,sb_email,sb_password,sb_repassword,sb_address;
    EditText ls_email,ls_password,ss_fname,ss_lname,ss_email,ss_phone,ss_password,ss_repassword;
    Button confirmation;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    loader loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        CheckAlreadyLogin();
        leftside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmation.setText("Login");
                rightside.setAlpha(0f);
                leftside.setAlpha(1f);
                lseller.setVisibility(View.GONE);
                lbuyer.setVisibility(View.VISIBLE);
                sbuyer.setVisibility(View.GONE);
                sseller.setVisibility(View.GONE);
                selector="buyer";
                buyer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                seller.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txtcolor));


            }
        });
        rightside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmation.setText("Login");
                rightside.setAlpha(1f);
                leftside.setAlpha(0f);
                lseller.setVisibility(View.VISIBLE);
                lbuyer.setVisibility(View.GONE);
                sbuyer.setVisibility(View.GONE);
                sseller.setVisibility(View.GONE);
                selector="seller";
                seller.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                buyer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txtcolor));

            }
        });
    }

    private void CheckAlreadyLogin() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null)
        {  setconstant();
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Profile", MODE_PRIVATE);
          if(pref.getString("Type", "").equalsIgnoreCase("seller"))
              startActivity(new Intent(Registration.this, SellerHome.class));
          else if(pref.getString("Type", "").equalsIgnoreCase("buyer"))
              startActivity(new Intent(Registration.this, BuyerHome.class));

          finishAffinity();
        }
    }


    private void init() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        confirmation=findViewById(R.id.confirmation);
        sseller=findViewById(R.id.sellersignup);
        sbuyer=findViewById(R.id.buyersignup);
        lseller=findViewById(R.id.sellerlogin);
        lbuyer=findViewById(R.id.buyerlogin);
        leftside=findViewById(R.id.leftside);
        rightside=findViewById(R.id.rightside);
        buyer=findViewById(R.id.buyer);
        seller=findViewById(R.id.seller);
        lb_email=findViewById(R.id.lb_email);
        lb_password=findViewById(R.id.lb_password);
        sb_fname=findViewById(R.id.sb_fname);
        sb_lname=findViewById(R.id.sb_lname);
        sb_email=findViewById(R.id.sb_email);
        sb_password=findViewById(R.id.sb_password);
        sb_address=findViewById(R.id.sb_address);
        sb_repassword=findViewById(R.id.sb_repassword);

        ls_email=findViewById(R.id.ls_email);
        ls_password=findViewById(R.id.ls_password);
        ss_fname=findViewById(R.id.ss_fname);
        ss_lname=findViewById(R.id.ss_lname);
        ss_email=findViewById(R.id.ss_email);
        ss_phone=findViewById(R.id.ss_phone);
        ss_password=findViewById(R.id.ss_password);
        ss_repassword=findViewById(R.id.ss_repassword);
        loading=new loader(this,getWindow().getDecorView().getRootView());

    }

    public void BuyerSignupView(View view) {
        selector="buyer";

        confirmation.setText("Registrarse");
       sbuyer.setVisibility(View.VISIBLE);
        lbuyer.setVisibility(View.GONE);
        lseller.setVisibility(View.GONE);
        sseller.setVisibility(View.GONE);
    }

    public void SellerSignupView(View view) {

        selector="seller";
        confirmation.setText("Registrarse");
        sbuyer.setVisibility(View.GONE);
        lbuyer.setVisibility(View.GONE);
        lseller.setVisibility(View.GONE);
        sseller.setVisibility(View.VISIBLE);
    }

    public void BuyerSigninView(View view) {
        selector="buyer";

        confirmation.setText("Iniciar sesion");
        sbuyer.setVisibility(View.GONE);
        lbuyer.setVisibility(View.VISIBLE);
        lseller.setVisibility(View.GONE);
        sseller.setVisibility(View.GONE);

    }

    public void SellerSigninView(View view) {
        selector="seller";
        confirmation.setText("Iniciar sesion");
        sbuyer.setVisibility(View.GONE);
        lbuyer.setVisibility(View.GONE);
        lseller.setVisibility(View.VISIBLE);
        sseller.setVisibility(View.GONE);
    }

    public void Confirmation(View view) {

        if(selector.equalsIgnoreCase("buyer")&&confirmation.getText().toString().equalsIgnoreCase("Registrarse")){
            if(sb_password.getText().toString().equals(sb_repassword.getText().toString()))
            registeruser(sb_email.getText().toString(),sb_password.getText().toString(),"Buyer");
            else
                Toast.makeText(this,"Contraseña incorrecta",Toast.LENGTH_SHORT).show();
        }
        else if(selector.equalsIgnoreCase("seller")&&confirmation.getText().toString().equalsIgnoreCase("Registrarse")) {
            if(ss_password.getText().toString().equals(ss_repassword.getText().toString()))
                registeruser(ss_email.getText().toString(),ss_password.getText().toString(),"Seller");
            else
                Toast.makeText(this,"Contraseña incorrecta",Toast.LENGTH_SHORT).show();
        }
        else if(selector.equalsIgnoreCase("seller")&&confirmation.getText().toString().equalsIgnoreCase("Iniciar sesion"))
              loginuser(ls_email.getText().toString(),ls_password.getText().toString());
        else if(selector.equalsIgnoreCase("buyer")&&confirmation.getText().toString().equalsIgnoreCase("Iniciar sesion"))
            loginuser(lb_email.getText().toString(),lb_password.getText().toString());


    }

    private void loginuser(final String email, String password) {
        Pattern p = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$");
        Matcher matcher = p.matcher(email);
        if(matcher.find()) {
            loading.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                                GetFurtherInfo(email);
                            else
                                Toast.makeText(Registration.this, "Autenticacion Fallida." + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        else
            Toast.makeText(this,"Formato de Email incorrecto",Toast.LENGTH_SHORT).show();
    }

    private void GetFurtherInfo(final String email) {
        db.collection("Registration").document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                  DocumentSnapshot result=task.getResult();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("Profile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                        editor.putString("FName",result.getString("First Name"));
                        editor.putString("LName", result.getString("Last Name"));
                        editor.putString("Email", email);
                        editor.putString("Phone", result.getString("Phone Number"));
                        editor.putString("Type", result.getString("Type"));
                        editor.putString("Password", result.getString("Password"));
                        editor.putString("Image", result.getString("Image Url"));
                        editor.putString("Address", result.getString("Address"));
                        editor.commit();
                        setconstant();
                         if(result.getString("Type").equalsIgnoreCase("seller"))
                             startActivity(new Intent(Registration.this, SellerHome.class));
                         else
                             startActivity(new Intent(Registration.this, BuyerHome.class));

                         finishAffinity();
                } else {
                     Toast.makeText(Registration.this,"No hay documento"+task.getException(),Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registration.this,"Error==>"+e.getMessage(),Toast.LENGTH_SHORT).show();
                GetFurtherInfo(email);
            }
        });

    }

    private void setconstant() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Profile", MODE_PRIVATE);
        constant.FName=pref.getString("FName","");
        constant.LName=pref.getString("LName","");
        constant.Email=pref.getString("Email","");
        constant.Phone=pref.getString("Phone","");
        constant.Image=pref.getString("Image","");
        constant.Password=pref.getString("Password","");
        constant.Address=pref.getString("Address","");

    }

    private void registeruser(String email, String password, final String type) {
        Pattern p = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$");
        Matcher matcher = p.matcher(email);
        if(matcher.find()) {
            loading.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                AddFurtherInfo(type);
                            } else {
                                Log.w("result", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Registration.this, "Autenticacion Fallida." + task.getException(), Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }

                        }
                    });
        }
        else
            Toast.makeText(this,"Formato de Email incorrecto",Toast.LENGTH_SHORT).show();
    }

    private void AddFurtherInfo(final String type) {
        Map<String, Object> user = new HashMap<>();
        String email;
        if(type.equalsIgnoreCase("seller")){
        user.put("First Name", ss_fname.getText().toString());
        user.put("Last Name", ss_lname.getText().toString());
        user.put("Phone", ss_phone.getText().toString());
        user.put("Type", type);
        user.put("Password", ss_password.getText().toString());
        user.put("Image Url", "");
        user.put("Address", "");
        email=ss_email.getText().toString();
        }
        else{
            user.put("First Name", sb_fname.getText().toString());
            user.put("Last Name", sb_lname.getText().toString());
            user.put("Phone", "");
            user.put("Type", type);
            user.put("Password", sb_password.getText().toString());
            user.put("Image Url", "");
            user.put("Address", sb_address.getText().toString());
            email=sb_email.getText().toString();
        }
        db.collection("Registration").document(email)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        if(type.equalsIgnoreCase("seller"))
                        GoToSeller(type);
                        else
                        GoToBuyer(type);
                        loading.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        AddFurtherInfo(type);
                        Log.d("further",e.getMessage()+e.getLocalizedMessage());
                        Toast.makeText(Registration.this,"Error==>"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void GoToBuyer(String type) {
        savedPref(type);
        setconstant();
        startActivity(new Intent(Registration.this, BuyerHome.class));
        finishAffinity();

    }

    private void savedPref(String type) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if(type.equalsIgnoreCase("seller")){
            editor.putString("FName", ss_fname.getText().toString());
            editor.putString("LName", ss_lname.getText().toString());
            editor.putString("Email", ss_email.getText().toString());
            editor.putString("Phone", ss_phone.getText().toString());
            editor.putString("Type", type);
            editor.putString("Password", ss_password.getText().toString());
            editor.putString("Address", "");
            editor.putString("Image", ""); }
        else{
            editor.putString("FName", sb_fname.getText().toString());
            editor.putString("LName", sb_lname.getText().toString());
            editor.putString("Email", sb_email.getText().toString());
            editor.putString("Phone", "");
            editor.putString("Type", type);
            editor.putString("Password", sb_password.getText().toString());
            editor.putString("Address", sb_address.getText().toString());
            editor.putString("Image", ""); }
        editor.commit();
    }

    private void GoToSeller(String type) {
        savedPref(type);
        setconstant();
        startActivity(new Intent(Registration.this, SellerHome.class));
        finishAffinity();
    }

    public void skip(View view) {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("Profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

            editor.putString("FName", "");
            editor.putString("LName", "");
            editor.putString("Email", "");
            editor.putString("Phone", "");
            editor.putString("Type", "");
            editor.putString("Password", "");
            editor.putString("Image", "");
        editor.commit();
        setconstant();
        startActivity(new Intent(Registration.this, BuyerHome.class));
        finish();
    }
}