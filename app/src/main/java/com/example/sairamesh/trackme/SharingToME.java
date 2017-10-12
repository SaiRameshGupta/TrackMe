package com.example.sairamesh.trackme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class SharingToME extends AppCompatActivity implements ValueEventListener,AdapterView.OnItemClickListener{
    private ListView sharingToMe;
    private DatabaseReference reference;
    private String sendersList,selectedSender;
    public static String selectedSenderUid;
    private HashMap users;
    private ArrayList<String> sendersArrayList;
    private final String TAG="TrackMe/SharingToME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_to_me);
        GetContactsList get=new GetContactsList(Registration.uid);
        users=get.usersString;
        sendersList=new String();
        selectedSender=new String();
        sendersArrayList=new ArrayList<String>();
        sharingToMe=(ListView)findViewById(R.id.SharingUsersList);
        reference= FirebaseDatabase.getInstance().getReference("Received").child(Registration.uid);
        reference.addValueEventListener(this);
        //buildSendersList();
        /*if(sendersList!=null) {
            Log.i(TAG, "inside build");
            StringTokenizer st = new StringTokenizer(sendersList, ";");
            Log.i(TAG,"st"+st.nextToken());
            while (st.hasMoreTokens()) {
                Log.i(TAG, "Inside while loop");
                String presentUser = st.nextToken();
                Set<String> usersSet = users.keySet();
                Iterator<String> it = usersSet.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    if (key.equals(presentUser)) {
                        sendersArrayList.add((String) users.get(key));
                    }
                }
            }
            //Log.i(TAG, "array list:" + sendersArrayList.toString());
            //ArrayAdapter<String> sendersAdapter = new ArrayAdapter<String>(SharingToME.this, android.R.layout.simple_list_item_1, sendersArrayList);
            //sharingToMe.setAdapter(sendersAdapter);
            //sharingToMe.setOnItemClickListener(this);
        }*/
    }

    public void buildSendersList(){
        Log.i(TAG,"inside build function");
        if(sendersList.length()!=0){
            Log.i(TAG,"inside build,sendersList"+sendersList);
            StringTokenizer st=new StringTokenizer(sendersList,";");
  //          Log.i(TAG,"st"+st.nextToken());
            while(st.hasMoreTokens()){
                Log.i(TAG,"Inside while loop");
                String presentUser=st.nextToken();
                Set<String> usersSet=users.keySet();
                Iterator<String> it=usersSet.iterator();
                while(it.hasNext()){
                    String key=it.next();
                    if(key.equals(presentUser)){
                        sendersArrayList.add((String) users.get(key));
                    }
                }
            }
            Log.i(TAG,"array list:"+sendersArrayList.toString());
            ArrayAdapter<String> sendersAdapter=new ArrayAdapter<String>(SharingToME.this,android.R.layout.simple_list_item_1,sendersArrayList);
            sharingToMe.setAdapter(sendersAdapter);
            sharingToMe.setOnItemClickListener(this);
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        sendersList="";
        while(sendersList.length()==0) {
            sendersList = dataSnapshot.getValue(String.class);
            Log.i(TAG, "sendersList" + sendersList);
        }
        buildSendersList();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //selectedSender=(String) sharingToMe.getSelectedItem();
        selectedSender=sendersArrayList.get(position);
        Log.i(TAG,"selectedSender"+selectedSender);
        if(users.containsValue(selectedSender)){
            Set<String> keys=users.keySet();
            Iterator<String> it=keys.iterator();
            while(it.hasNext()){
                String key=it.next();
                if (users.get(key).equals(selectedSender))
                    selectedSenderUid=key;
                Log.i(TAG,"selcectedSenderUid"+selectedSenderUid);
            }
            startActivity(new Intent(SharingToME.this,MapsActivity.class));
        }
    }
}
