package com.programmer.jgallos.ma_i;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Picasso;


public class ViewFeedbackActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;

    String signin_subject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewFeedback);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        signin_subject = getIntent().getExtras().getString("SigninSubject");
        mDatabase = FirebaseDatabase.getInstance().getReference().child(signin_subject + "_feedback");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<FeedbackRecords, ViewFeedbackActivity.FeedbackViewHolder> FBRA = new FirebaseRecyclerAdapter<FeedbackRecords, FeedbackViewHolder>(
                FeedbackRecords.class,
                R.layout.feedback_list,
                ViewFeedbackActivity.FeedbackViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ViewFeedbackActivity.FeedbackViewHolder viewHolder, FeedbackRecords model, int position) {
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
                //Toast.makeText(ViewAcadActivity.this,mDatabaseUsers.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(ViewAcadActivity.this,model.getUid(),Toast.LENGTH_LONG).show();
                if (!mDatabaseUsers.toString().equals("https://ma-s-514f2.firebaseio.com/Users/" + model.getUid())) {
                    //viewHolder.mView.setVisibility(View.GONE);
                    //viewHolder.mView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                    viewHolder.mView.setVisibility(View.VISIBLE); //set all feedback viewable by all users (for testing only)
                } else {
                    viewHolder.mView.setVisibility(View.VISIBLE);
                }

                // if (mDatabaseUsers.toString().equals("https://ma-s-514f2.firebaseio.com/Users/" + model.getUid())) {
                final String feedback_key = getRef(position).getKey().toString();

                viewHolder.setLevel(model.getLevel());

                viewHolder.setDesc(model.getDesc());

                viewHolder.setStatus(model.getStatus());
                viewHolder.setUid(model.getUid());

               // viewHolder.setImageUrl(getApplicationContext(), model.getImageUrl());
                //Toast.makeText(ViewAcadActivity.this, model.getImageUrl().toString(), Toast.LENGTH_LONG).show();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleActivity = new Intent(ViewFeedbackActivity.this, SingleFeedbackActivity.class);
                        singleActivity.putExtra("FeedbackID", feedback_key);
                        singleActivity.putExtra("SigninSubject", signin_subject);
                        startActivity(singleActivity);
                        finish();
                    }
                });


            }
        };
        recyclerView.setAdapter(FBRA);
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public FeedbackViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setLevel(String level) {
            TextView feedback_level = mView.findViewById(R.id.feedbackLevel);
            feedback_level.setText("Level: " + level);
        }

        public void setDesc(String desc) {
            TextView feedback_desc = mView.findViewById(R.id.feedbackDesc);
            feedback_desc.setText("Feedback: " + desc);
        }

        public void setStatus(String status) {
            TextView feedback_status = mView.findViewById(R.id.feedbackStatus);
            feedback_status.setText("Status: " + status);

        }
        public void setUid(String uid) {
            TextView feedback_uid = mView.findViewById(R.id.feedbackUid);
            feedback_uid.setText("Id (remove later): " + uid);
        }




    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_addAcad2) {
            startActivity(new Intent(ViewAcadActivity.this, AddAcadActivity.class));


        } else if (id==R.id.action_viewAttendance2) {
            startActivity(new Intent(ViewAcadActivity.this, ViewAttendanceActivity.class));
        }
        return super.onOptionsItemSelected(item);
    } */


}
