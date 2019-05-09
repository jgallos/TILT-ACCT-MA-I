package com.programmer.jgallos.ma_i;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ViewAttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;

    String signin_subject =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAttendanceActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewAttendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        signin_subject = getIntent().getExtras().getString("SigninSubject");

        mDatabase = FirebaseDatabase.getInstance().getReference().child(signin_subject + "_attendance");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<AttendanceRecords, AttendanceViewHolder> FBRA = new FirebaseRecyclerAdapter<AttendanceRecords, AttendanceViewHolder>(
                AttendanceRecords.class,
                R.layout.attendance_list,
                AttendanceViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(AttendanceViewHolder viewHolder, AttendanceRecords model, int position) {
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

                if (!mDatabaseUsers.toString().equals("https://ma-s-514f2.firebaseio.com/Users/" + model.getUid())){
                    viewHolder.mView.setVisibility(View.GONE);
                    viewHolder.mView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                } else {
                    viewHolder.mView.setVisibility(View.VISIBLE);
                }

                final String attendance_key = getRef(position).getKey().toString();

                viewHolder.setDate(model.getDate());
                viewHolder.setSignin(model.getSignin());
                viewHolder.setSignout(model.getSignout());
                //viewHolder.setUid(model.getUid());

                //Toast.makeText(ViewAttendanceActivity.this, model.getSignout().toString(), Toast.LENGTH_LONG).show();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        recyclerView.setAdapter(FBRA);
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public AttendanceViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setDate(String date) {
            TextView attendance_date = mView.findViewById(R.id.textDate);
            attendance_date.setText("Date: " + date);
        }

        public void setSignin(String signin) {
            TextView attendance_signin = mView.findViewById(R.id.textSigninTime);
            attendance_signin.setText("Sign-in Time: " + signin);
        }

        public void setSignout(String signout) {
            TextView attendance_signout = mView.findViewById(R.id.textSignoutTime);
            attendance_signout.setText("Sign-out Time: " + signout);
        }
       // public void setUid(String uid) {
        //    TextView attendance_uid = mView.findViewById(R.id.textUid);
        //    attendance_uid.setText("User Id: " + uid);
       // }


    }


}
