package com.mblj.mycontacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ArrayList<Contact> contacts;
    private FirebaseDatabase mFireDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        final ContactAdapter adapter = new ContactAdapter();
        rvContacts.setAdapter(adapter);
        LinearLayoutManager contactsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvContacts.setLayoutManager(contactsLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert_menu:
                Intent intent = new Intent(this, ContactActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}