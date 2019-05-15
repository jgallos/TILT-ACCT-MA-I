/*
TILT-ACCT - Transparency in Learning and Teaching through Android and Cloud Computing Technologies
Programmer: Joseph M. Gallos
Date: May 2019
Software License: GNU-General Public License
*/
package com.programmer.jgallos.ma_i;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instacart.library.truetime.TrueTimeRx;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MySubjectsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseRef;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subjects);
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

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        String user_id =mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(user_id + "_subjects");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


        //Toast.makeText(MySubjectsActivity.this,mDatabase.toString(),Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<SubjectRecords, SubjectViewHolder> FBRA = new FirebaseRecyclerAdapter<SubjectRecords, SubjectViewHolder>(
                SubjectRecords.class,
                R.layout.subject_list,
                SubjectViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(SubjectViewHolder viewHolder, final SubjectRecords model, int position) {

                viewHolder.setSubject(model.getSubject());

                if (model.getSubject().equals("default")) {
                    viewHolder.mView.setVisibility(View.GONE);
                    viewHolder.mView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                } else {
                    viewHolder.mView.setVisibility(View.VISIBLE);
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MySubjectsActivity.this, "Signing-in...", Toast.LENGTH_SHORT).show();
                        databaseRef = FirebaseDatabase.getInstance().getReference().child(model.getSubject() + "_attendance");
                        final DatabaseReference newAttendance = databaseRef.push();

                        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String timeHolder;
                                final String signKey;
                                final String timeHolderBuff;
                                final String timeHolderDateBuff;

                                timeHolder = updateTime(1);
                                timeHolderDateBuff = timeHolder;

                                newAttendance.child("date").setValue(timeHolder);
                                signKey = newAttendance.getKey();
                                //Toast.makeText(AvailClassesActivity.this,signKey.toString(),Toast.LENGTH_LONG).show();
                                timeHolder = updateTime(2);
                                timeHolderBuff = timeHolder;
                                newAttendance.child("signin").setValue(timeHolder);
                                newAttendance.child("signout").setValue(("default"));
                                newAttendance.child("name").setValue(dataSnapshot.child("name").getValue());

                                //newAttendance.child("signin_time").setValue(ServerValue.TIMESTAMP);
                                newAttendance.child("uid").setValue(mCurrentUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent sessionIntent = new Intent(MySubjectsActivity.this, ClassSessionActivity.class);
                                            sessionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            sessionIntent.putExtra("SigninKey",signKey);
                                            sessionIntent.putExtra("SigninSubject", model.getSubject());
                                            sessionIntent.putExtra("SigninTime",timeHolderBuff);
                                            sessionIntent.putExtra("SigninDate", timeHolderDateBuff);
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
        };
        recyclerView.setAdapter(FBRA);
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public SubjectViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setSubject(String subject) {
            TextView subject_e = mView.findViewById(R.id.textViewSubject);
            subject_e.setText("Subject: " + subject);
        }

    }


    private String updateTime(int flag) {
        TextView textTime = (TextView) findViewById(R.id.textViewSubjectTime);

        if (!TrueTimeRx.isInitialized()) {
            Toast.makeText(MySubjectsActivity.this, "TrueTime is not yet initialized.", Toast.LENGTH_SHORT).show();
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
