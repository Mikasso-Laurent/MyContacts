package com.mblj.mycontacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ContactActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final int PICTURE_RESULT = 42;
    EditText txtFirst;
    EditText txtLast;
    EditText txtPhone;
    ImageView imageContact;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        txtFirst = (EditText) findViewById(R.id.txtFirst);
        txtLast = (EditText) findViewById(R.id.txtLast);
        txtPhone = (EditText) findViewById(R.id.txtNumber);
        imageContact = (ImageView) findViewById(R.id.ImageContact);
        Intent intent = getIntent();
        contact = (Contact) intent.getSerializableExtra("contact");
        if(contact == null){
            contact = new Contact();
        }
        this.contact = contact;
        txtFirst.setText(contact.getFirstname());
        txtLast.setText(contact.getLastname());
        txtPhone.setText(contact.getPhonenumber());
        if(contact.getImageUrl()==null) {
            imageContact.setImageDrawable(getDrawable(R.mipmap.ic_launcher));
        }
        else {
            showImage(contact.getImageUrl());
        }
        Button btnImage = (Button) findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_RESULT && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            StorageReference ref = FirebaseUtil.mStorageRef.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadUri = uri;
                                        String download_url = uri.toString();
                                        String pictureName = uri.getPath();
                                        contact.setImageUrl(download_url);
                                        contact.setImageName(pictureName);
                                        Log.d("Url", download_url);
                                        Log.d("Name", pictureName);
                                        showImage(download_url);
                                    }
                                });
                            }
                        }

                    });
                    //deal.setImageUrl(url);
                    //showImage(url);
                }
            });

        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                if(txtPhone.getText() == null || txtPhone.getText().toString().isEmpty() || txtFirst.getText() == null || txtFirst.getText().toString().isEmpty()){

                    Toast.makeText(this, "Insert At least First name and phone number", Toast.LENGTH_LONG).show();
                }
                else{
                    saveDeal();
                    Toast.makeText(this, "Contact saved", Toast.LENGTH_LONG).show();
                    clean();
                    backToList();
                }
                return true;
            case R.id.delete_menu:
                deleteDeal();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clean() {
        txtFirst.setText("");
        txtLast.setText("");
        txtPhone.setText("");
        txtFirst.requestFocus();
    }

    private void saveDeal() {
        contact.setFirstname(txtFirst.getText().toString());
        contact.setLastname(txtLast.getText().toString());
        contact.setPhonenumber(txtPhone.getText().toString());
        if(contact.getId() == null){
            mDatabaseReference.push().setValue(contact);
        }
        else {
            mDatabaseReference.child(contact.getId()).setValue(contact);
        }
    }
    private void deleteDeal(){
        if(contact.getId() == null){
            Toast.makeText(this, "Please save the Contact before deleting", Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseReference.child(contact.getId()).removeValue();
        Toast.makeText(this, "Contact Deleted", Toast.LENGTH_SHORT).show();
        if(contact.getImageName() != null && contact.getImageName().isEmpty() == false){
            StorageReference picRef = FirebaseUtil.mStorage.getReference().child(contact.getImageName());
            picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Delete image", "Image Successfully Deleted");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Delete image", e.getMessage());
                }
            });
        }
    }
    private void backToList(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
    private void showImage(String url){
        if(url != null && url.isEmpty() == false){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this).setLoggingEnabled(true);
            Picasso.with(this)
                    .load(url)
                    .resize(width*1/3, width*1/3)
                    .centerCrop()
                    .into(imageContact);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }
}