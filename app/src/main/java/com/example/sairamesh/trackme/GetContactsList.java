package com.example.sairamesh.trackme;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Created by sairamesh on 5/10/17.
 */

public class GetContactsList implements ValueEventListener {
    private String uid;
    private DatabaseReference mContacts,mUsers;
    public ArrayList<String> contactsList;
    private ArrayList<String> contacts;
    public HashMap<String,String> usersString;
    private String TAG="TrackMe/GetContacts";
    GetContactsList(String uid){
        Log.i(TAG,"uid="+uid);
        this.uid=uid;
        contactsList=new ArrayList<String>();
        contacts=new ArrayList<String>();
        usersString=new HashMap<String, String>();
        mContacts= FirebaseDatabase.getInstance().getReference("UsersContacts").child(Registration.uid);
        mUsers=FirebaseDatabase.getInstance().getReference("Users");
        //mUsers.addListenerForSingleValueEvent(this);
        fetchUsers();
        //mContacts.addValueEventListener(this);
        fetchContacts();
    }
    /*public ArrayList<String> buildContactsList(){
        if(this.contacts!=null){
            Log.i(TAG,"inside if of buildContactsList:"+this.contacts.toString());
            for(int i=0;i<contacts.size();i++){
                String userName=getUserName(this.contacts.get(i));
                if(userName!=null){
                    this.contactsList.add(userName);
                }
            }
        }
        Log.i(TAG,"Final List:"+this.contactsList.toString());
        return contactsList;
    }*/
    private void fetchUsers(){
        mUsers.addValueEventListener(this);
    }
    private void fetchContacts(){
        mContacts.addValueEventListener(this);
    }
    private String getUserName(String uid){
        Log.i(TAG,"uid inside getUserName:"+uid);
        if(usersString.containsKey(uid)){
            Log.i(TAG,"userName found is "+usersString.get(uid));
            return usersString.get(uid);
        }
        else
            return null;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
       if (dataSnapshot.getKey().equals(uid)){
           String str=dataSnapshot.getValue(String.class);
           Log.i(TAG,"existing contacts:"+str);
           if(str!=null) {
               StringTokenizer st = new StringTokenizer(str, ";");
               while (st.hasMoreTokens()) {
                   contacts.add(st.nextToken());
               }
           }
           if(contacts!=null){
               Log.i(TAG,"inside if of onDataChange:"+contacts.toString());
               for(int i=0;i<contacts.size();i++){
                   String userName=getUserName(contacts.get(i));
                   Log.i(TAG,"UserName:"+userName);
                   if(userName!=null){
                       contactsList.add(userName);
                   }
               }
           }
           Log.i(TAG,"contactsList:"+contactsList.toString());
       }
       if(dataSnapshot.getKey().equals("Users")){
           for(DataSnapshot ds:dataSnapshot.getChildren()){
               usersString.put(ds.getKey(),ds.getValue(String.class));
           }
           Log.i(TAG,"users:"+usersString.toString());
       }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
