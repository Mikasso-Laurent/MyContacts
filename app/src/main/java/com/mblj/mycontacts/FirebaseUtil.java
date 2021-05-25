package com.mblj.mycontacts;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static com.mblj.mycontacts.FirebaseUtil firebaseUtil;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    public static ArrayList<Contact> mContacts;

    private FirebaseUtil() {};

    public static void openFbReference (String ref) {
        if(firebaseUtil == null){
            firebaseUtil = new com.mblj.mycontacts.FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mContacts = new ArrayList<Contact>();
            connectStorage();
        }

        mContacts = new ArrayList<Contact>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }
    public static void connectStorage(){
       mStorage = FirebaseStorage.getInstance();
       mStorageRef = mStorage.getReference().child("contact_pictures");
    }
}
