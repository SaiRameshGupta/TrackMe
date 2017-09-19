package com.example.sairamesh.trackme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private Button register;
    private DatabaseReference mDatabase;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private String uid;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button user_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth= FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference();
        register =(Button) findViewById(R.id.user_register);
        email= (EditText)findViewById(R.id.user_email);
        password= (EditText)findViewById(R.id.user_password);
        user_login=(Button)findViewById(R.id.login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,LoginActivity.class));
            }
        });
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(Registration.this,MapsActivity.class));
                }
            }
        };

    }
    private void signUp(){
        String user_email= email.getText().toString();
        String user_password= password.getText().toString();
        mAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Registration.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Registration.this,"Error in Registration",Toast.LENGTH_LONG).show();
                }
            }
        });
        if(mAuth.getCurrentUser()!=null) {
            uid = mAuth.getCurrentUser().getUid();
            mDatabase=FirebaseDatabase.getInstance().getReference("UsersSharing");
            mDatabase.child("isActive").setValue("false");
            mDatabase.child("location").setValue(" ; ");
            mDatabase.child("sharingTo").setValue(" ").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isComplete())
                        Toast.makeText(Registration.this,"Completed Adding Row",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

}
