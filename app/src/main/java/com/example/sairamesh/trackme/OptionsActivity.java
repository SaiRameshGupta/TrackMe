package com.example.sairamesh.trackme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends AppCompatActivity {
    Button addContacts,enableSharing,sharingToMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        addContacts=(Button)findViewById(R.id.AddContacts);
        enableSharing=(Button)findViewById(R.id.EnableSharing);
        sharingToMe=(Button)findViewById(R.id.SharingToMe);
        addContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsActivity.this,AddContacts.class));
            }
        });
        enableSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsActivity.this,EnableSharingActivity.class));
            }
        });
        sharingToMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsActivity.this,SharingToME.class));
            }
        });

    }
}
