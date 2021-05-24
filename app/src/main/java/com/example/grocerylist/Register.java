package com.example.grocerylist;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button btnReg;
    private EditText fname;
    private EditText lname;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    private FirebaseFirestore db;
    private TextView signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        mDialog = new ProgressDialog(this);

        email = findViewById(R.id.reg_email);
        pass = findViewById(R.id.reg_password);
        fname = findViewById(R.id.reg_firstname);
        lname = findViewById(R.id.reg_lastname);
        btnReg = findViewById(R.id.register_button);
        signin = findViewById(R.id.reg_alreadyhaveaccount);


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();
                String mFname = fname.getText().toString().trim();
                String mLname = lname.getText().toString().trim();

                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("Email required");
                    return;
                }
                if (TextUtils.isEmpty(mPass)) {
                    pass.setError("Password required");
                    return;
                }
                if (TextUtils.isEmpty(mFname)) {
                    fname.setError("First name required");
                }
                if (TextUtils.isEmpty(mLname)) {
                    lname.setError("Last name required");
                }

                mDialog.setMessage("Processing..");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("Register act", "User created");

                            HashMap<String, Object> userdetails = new HashMap<>();
                            userdetails.put("Email", mEmail);
                            userdetails.put("First Name", mFname);
                            userdetails.put("Last Name", mLname);

                            db.collection("users").document(mEmail).set(userdetails)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Register act", "User details added");

                                            Toast.makeText(Register.this, "User added", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            onDestroy();
                                            Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                                            mDialog.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Log.d("Register act", "user details not added");
                                            Toast.makeText(Register.this, "User not added", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        } else {
                            Log.d("Register act", "User not created");
                            Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });


            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Register act", "intent to Login activity");
                startActivity(new Intent(getApplicationContext(),Login.class));
                onDestroy();
            }
        });

    }
}
