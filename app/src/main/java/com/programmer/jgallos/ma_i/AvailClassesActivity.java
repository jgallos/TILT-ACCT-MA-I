package com.programmer.jgallos.ma_i;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.DatabaseError;
import com.instacart.library.truetime.TrueTimeRx;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class AvailClassesActivity extends AppCompatActivity {

    private Button signinBtn;

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DatabaseReference mDatabaseUsers;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //subSpinner = (Spinner)findViewById(R.id.subSpinner);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avail_classes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.subSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.available_classes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        signinBtn = (Button)findViewById(R.id.signinBtn);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AvailClassesActivity.this, "Signing-in...", Toast.LENGTH_SHORT).show();
                Spinner spinnerContent = (Spinner)findViewById(R.id.subSpinner);
                final String subject = spinnerContent.getSelectedItem().toString();
                databaseRef = database.getInstance().getReference().child(subject + "_attendance");
                final DatabaseReference newAttendance = databaseRef.push();


                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String timeHolder;

                        timeHolder = updateTime(1);
                        newAttendance.child("date").setValue(timeHolder);
                        timeHolder = updateTime(2);
                        newAttendance.child("signin").setValue(timeHolder);
                        newAttendance.child("signout").setValue(("default"));

                        //newAttendance.child("signin_time").setValue(ServerValue.TIMESTAMP);
                        newAttendance.child("uid").setValue(mCurrentUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent sessionIntent = new Intent(AvailClassesActivity.this, ClassSessionActivity.class);
                                    sessionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(sessionIntent);
                                    finish();
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private String updateTime(int flag) {
        TextView textTime = (TextView) findViewById(R.id.textViewTime);

        if (!TrueTimeRx.isInitialized()) {
            Toast.makeText(AvailClassesActivity.this, "TrueTime is not yet initialized.", Toast.LENGTH_SHORT).show();
            return " ";
        }

        Date trueTime = TrueTimeRx.now();
        if (flag == 1) {
            textTime.setText(getString(R.string.tt_time_gmt, _formatDate(trueTime, "yyyy-MM-dd", TimeZone.getTimeZone("GMT+08:00"))));

        } else if (flag==2) {
            textTime.setText(getString(R.string.tt_time_gmt, _formatDate(trueTime, "HH:mm:ss", TimeZone.getTimeZone("GMT+08:00"))));
        }
        return textTime.getText().toString();
    }

    private String _formatDate(Date date, String pattern, TimeZone timeZone) {
        DateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        format.setTimeZone(timeZone);
        return format.format(date);
    }




}
