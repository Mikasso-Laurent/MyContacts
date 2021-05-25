package com.mblj.mycontacts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    ArrayList<Contact> contacts;
    private FirebaseDatabase mFireDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ImageView imageContactList;
    public ContactAdapter() {

        com.mblj.mycontacts.FirebaseUtil.openFbReference("contacts");
        mFireDatabase = com.mblj.mycontacts.FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = com.mblj.mycontacts.FirebaseUtil.mDatabaseReference;
        contacts = com.mblj.mycontacts.FirebaseUtil.mContacts;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Contact ct = snapshot.getValue(Contact.class);
                Log.d("Contact",ct.getFirstname());
                ct.setId(snapshot.getKey());
                contacts.add(ct);
                notifyItemInserted(contacts.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.rv_row, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        TextView tvFirstName;
        TextView tvLastName;
        TextView tvPhoneNumber;
        ImageView imageContactList;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFirstName = (TextView) itemView.findViewById(R.id.tvFirstName);
            tvLastName = (TextView) itemView.findViewById(R.id.tvLastName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            imageContactList = (ImageView) itemView.findViewById(R.id.imageContactList);
            itemView.setOnClickListener(this);
        }

        public void bind(Contact contact){
            tvFirstName.setText(contact.getFirstname());
            tvLastName.setText(contact.getLastname());
            tvPhoneNumber.setText(contact.getPhonenumber());
            showImage(contact.getImageUrl());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));
            Contact selectedContact = contacts.get(position);
            Intent intent = new Intent(v.getContext(), ContactActivity.class);
            intent.putExtra("contact", selectedContact);
            v.getContext().startActivity(intent);
        }
        private void showImage(String url){
            if(url != null && url.isEmpty()==false){
                Picasso.with((imageContactList.getContext()))
                        .load(url)
                        .resize(240,240)
                        .centerCrop()
                        .into(imageContactList);
            }
        }
    }
}
