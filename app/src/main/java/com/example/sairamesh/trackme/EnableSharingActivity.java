package com.example.sairamesh.trackme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EnableSharingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private Switch enableSharing;
    private Intent i;
    private ListView receiversList;
    private Button share;
    private DatabaseReference mReceived,mSharing;
    private String contactsBuf=new String();
    private ArrayList<String> contactsList;
    private String selectedReceivers;
    private GetContactsList get;
    private String existingReceivers;
    private HashMap<String,String> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_sharing);
        i =new Intent(EnableSharingActivity.this,LocationService.class);
        mSharing= FirebaseDatabase.getInstance().getReference("UsersSharing").child(Registration.uid).child("sharingTo");
        mReceived=FirebaseDatabase.getInstance().getReference("Received");
        enableSharing=(Switch)findViewById(R.id.EnableSharingSwitch);
        enableSharing.setOnCheckedChangeListener(this);
        receiversList=(ListView)findViewById(R.id.receiversView);
        share=(Button)findViewById(R.id.shareButton);
        get=Registration.get;
        users=get.usersString;
        contactsList=get.contactsList;
        ArrayAdapter<String> receivers=new ArrayAdapter<String>(EnableSharingActivity.this,android.R.layout.simple_list_item_multiple_choice,android.R.id.text1,contactsList);
        receiversList.setAdapter(receivers);
        receiversList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                /*for(int i=0;i<array.size();i++) {
                    if(users.containsValue(array.get(i))){

                    }*/
                    mSharing.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            existingReceivers = dataSnapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mSharing.setValue(existingReceivers + receiversList.getItemAtPosition(position).toString());
                }
            //}
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // SparseBooleanArray array=receiversList.getCheckedItemPositions();
                //Log.i("TrackMe/Receivers","array:"+array.toString());
                String receivers=new String();
                for(int i=0;i<contactsList.size();i++) {
                    if (receiversList.isItemChecked(i)){
                        receivers+=contactsList.get(i);

                        mReceived.child(contactsList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                contactsBuf=dataSnapshot.getValue(String.class);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        mReceived.child(contactsList.get(i)).setValue(contactsBuf+contactsList.get(i));
                    }

                }
                mSharing.setValue(receivers);

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){

            Toast.makeText(EnableSharingActivity.this,"Strating Location Service",Toast.LENGTH_SHORT).show();
            startService(i);
            Log.d("EnableSharing","Location service started");
        }
        else{
            stopService(i);
            Log.d("EnableSharing","Location service stopped");
            Toast.makeText(EnableSharingActivity.this,"Stopping Location Service",Toast.LENGTH_SHORT).show();
        }

    }
}
