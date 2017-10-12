package com.example.sairamesh.trackme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class AddContacts extends AppCompatActivity {
    Button addContactsButton;
    EditText addContact;
    DatabaseReference mDatabase,mUsers;
    Boolean contactFound=Boolean.FALSE;
    String contactUid,contact;
    static String userEmail;
    ListView contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts2);
        addContactsButton=(Button)findViewById(R.id.addContactsButton);
        addContact=(EditText)findViewById(R.id.addContactsEditText);
        contactsList=(ListView)findViewById(R.id.contactsListView);
        //userEmail=new String();
        GetContactsList get=Registration.get;
        //ArrayList<String> contactsArray=get.contactsList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddContacts.this,android.R.layout.simple_list_item_1, get.contactsList);
        contactsList.setAdapter(adapter);
        mDatabase=FirebaseDatabase.getInstance().getReference("UsersContacts").child(Registration.uid);

        /*mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String existingContacts=dataSnapshot.getValue(String.class);
                    Toast.makeText(AddContacts.this,existingContacts,Toast.LENGTH_SHORT).show();
                    if(existingContacts!=null) {
                        StringTokenizer st = new StringTokenizer(existingContacts, ";");
                        while (st.hasMoreTokens()) {
                            getUserEmail(st.nextToken());
                            if(AddContacts.userEmail!=null)
                                contactsArray.add(AddContacts.userEmail);
                            else {
                                Log.i("UserEmail/listLength",""+contactsArray.size());
                                Toast.makeText(AddContacts.this, "Returned email is null", Toast.LENGTH_SHORT).show();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddContacts.this,android.R.layout.simple_list_item_1, contactsArray);
                        contactsList.setAdapter(adapter);
                    }
                    else
                        Toast.makeText(AddContacts.this,"No existing Contacts",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        */
        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact=addContact.getText().toString().trim();
                mDatabase=FirebaseDatabase.getInstance().getReference("Users");
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            if(ds.getValue(String.class).equals(contact)) {
                                Toast.makeText(AddContacts.this,"ContactFound",Toast.LENGTH_SHORT).show();
                                contactFound = Boolean.TRUE;
                                contactUid = ds.getKey();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(contactFound) {
                    mDatabase = FirebaseDatabase.getInstance().getReference("UsersContacts").child(Registration.uid);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String contacts = dataSnapshot.getValue(String.class);
                            if(!isContactAlreadyExists(contacts,contactUid)) {
                                if (contacts != null)
                                    mDatabase.setValue(contacts + contactUid + ";");
                                else
                                    mDatabase.setValue(contactUid + ";");
                            }
                            else
                                Toast.makeText(AddContacts.this,"Specified contact is already added",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    GetContactsList get=Registration.get;
                    //Toast.makeText(AddContacts.this,get.buildContactsList().get(1),Toast.LENGTH_SHORT).show();
                    ArrayAdapter<String > arrayAdapter=new ArrayAdapter<String>(AddContacts.this,android.R.layout.simple_list_item_1,get.contactsList);
                    contactsList.setAdapter(arrayAdapter);
                }
                else
                    Toast.makeText(AddContacts.this,"Specified contact does not exist",Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void getUserEmail(final String uid){
        mUsers=FirebaseDatabase.getInstance().getReference("Users");
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String key=ds.getKey();
                    Log.i("UserIdFetch",key);
                    Log.i("UserIdFetch","uid="+uid);
                    if(key.equals(uid)){
                        AddContacts.userEmail=ds.getValue(String.class);
                        Log.i("userEmail",AddContacts.userEmail);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public Boolean isContactAlreadyExists(String contactList,String searchContact){
        Boolean flag=Boolean.FALSE;
        if(contactList!=null){
            StringTokenizer st=new StringTokenizer(contactList,";");
            while(st.hasMoreTokens()){
                if(st.nextToken().equals(searchContact))
                    flag=Boolean.TRUE;
            }
        }
        return flag;
    }
}

/*class ContactsArrayBuilder implements ValueEventListener{
    DatabaseReference contacts;
    DatabaseReference users;
    String uid;
    ArrayList<String> contactsArray;
    ContactsArrayBuilder(){
        this.contacts=FirebaseDatabase.getInstance().getReference("UsersContacts");
        this.users=FirebaseDatabase.getInstance().getReference("Users");
        this.contacts.addListenerForSingleValueEvent(this);
        this.users.addListenerForSingleValueEvent(this);
        this.uid=Registration.uid;
        this.contactsArray=new ArrayList<String>();
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getKey()=="Users"){

        }
        if (dataSnapshot.getKey()=="UsersContacts"){

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
*/