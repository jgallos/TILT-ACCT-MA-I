package com.programmer.jgallos.ma_i;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddAcadActivity extends AppCompatActivity {

    private ImageButton imageAcad;
    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri uri = null;

    private EditText editTitle;
    private EditText editDesc;
    private Button saveBtn;

    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    String logged_subject = null;
    String signin_date = null;

    private static final String TAG = AddAcadActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acad);

        imageAcad = (ImageButton)findViewById(R.id.imageAcad);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        editDesc = (EditText)findViewById(R.id.editDesc);
        editTitle = (EditText)findViewById(R.id.editTitle);
        storage = FirebaseStorage.getInstance().getReference();
        logged_subject = getIntent().getExtras().getString("SigninSubject");
        databaseRef = database.getInstance().getReference().child(logged_subject + "_storage");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


        imageAcad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                } catch (Exception e) {
                    Log.e(TAG,"exception", e);
                }

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddAcadActivity.this, "Saving...", Toast.LENGTH_SHORT).show();
                final String recordTitle = editTitle.getText().toString().trim();
                final String recordDesc = editDesc.getText().toString().trim();

                if (!TextUtils.isEmpty(recordTitle) && !TextUtils.isEmpty(recordDesc)){
                    final StorageReference filepath = storage.child(logged_subject + "_images").child(uri.getLastPathSegment());

                    UploadTask uploadTask = filepath.putFile(uri);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return filepath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final Uri downloadUrl = task.getResult();
                                Toast.makeText(AddAcadActivity.this, "Record saved.",Toast.LENGTH_SHORT).show();

                                final DatabaseReference newAcad = databaseRef.push();
                                mDatabaseUsers.addValueEventListener((new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        newAcad.child("date").setValue(signin_date);
                                        newAcad.child("title").setValue(recordTitle);
                                        newAcad.child("desc").setValue(recordDesc);
                                        newAcad.child("imageUrl").setValue(downloadUrl.toString());
                                        newAcad.child("uid").setValue(mCurrentUser.getUid());
                                        newAcad.child("username").setValue(dataSnapshot.child("name").getValue())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //Intent intent = new Intent(AddAcadActivity.this, ViewAcadActivity.class);
                                                            //intent.putExtra("SigninSubject",logged_subject);
                                                            //startActivity(intent);
                                                            Toast.makeText(AddAcadActivity.this, "Record successfully uploaded!",Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                }));

                            }else {
                                //handle failure
                                Toast.makeText(AddAcadActivity.this, "Save failed.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAcadActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK) {
            uri = data.getData();
            imageAcad.setImageURI(uri);
        }
    }

}