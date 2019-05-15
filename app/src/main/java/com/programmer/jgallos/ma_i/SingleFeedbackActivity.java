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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleFeedbackActivity extends AppCompatActivity {

    private TextView singleDate, singleLevel, singleDesc, singleStatus, singleUID;
    String post_key = null;
    private DatabaseReference mDatabase;
    private Button replyBtn, scalateBtn, resolvedBtn;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private DatabaseReference mReplyDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;

    private DatabaseReference mDatabaseUsers;

    String signin_subject = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_feedback);

        //singleDate = (TextView)findViewById(R.id.sfeedbackDate);
        singleLevel = (TextView)findViewById(R.id.sfeedbackLevel);
        singleDesc = (TextView)findViewById(R.id.sfeedbackDesc);
        singleStatus = (TextView)findViewById(R.id.sfeedbackStatus);
        //singleUID = (TextView)findViewById(R.id.sfeedbackUid);

        signin_subject = getIntent().getExtras().getString("SigninSubject");
        mDatabase = FirebaseDatabase.getInstance().getReference().child(signin_subject + "_feedback");
        post_key = getIntent().getExtras().getString("FeedbackID");
        //Toast.makeText(SinglePostActivity.this,post_key.toString(),Toast.LENGTH_LONG).show();
        replyBtn = (Button)findViewById(R.id.buttonReply);
        //scalateBtn = (Button)findViewById(R.id.buttonScalate);
        //resolvedBtn = (Button)findViewById(R.id.buttonResolved);
        mAuth = FirebaseAuth.getInstance();
        //replyBtn.setVisibility(View.INVISIBLE); for instructors all feedbacks are visible

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewSingleFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mReplyDatabase = FirebaseDatabase.getInstance().getReference().child("Reply_" + post_key);

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String feedback_level = (String) dataSnapshot.child("level").getValue();
                String feedback_desc = (String) dataSnapshot.child("desc").getValue();
                String feedback_status = (String) dataSnapshot.child("status").getValue();
                String feedback_uid = (String) dataSnapshot.child("uid").getValue();
                String feedback_username = (String) dataSnapshot.child("username").getValue();

                //singleDate.setText("");
                singleLevel.setText("Level: " + feedback_level);
                singleDesc.setText("Feedback: " + feedback_desc);
                singleStatus.setText("Status: " + feedback_status);
                //singleUID.setText("(remove later): " + feedback_uid);
                if (mAuth.getCurrentUser().getUid().equals(feedback_uid)){

                    replyBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleFeedbackActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClickReply(View view) {
        Intent singleReply = new Intent(this, FeedbackReplyActivity.class);
        singleReply.putExtra("SingleFeedbackID", post_key);
        singleReply.putExtra("SigninSubject", signin_subject);
        startActivity(singleReply);
        //finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<ReplyRecords, SingleFeedbackActivity.SFBViewHolder> FBRA = new FirebaseRecyclerAdapter<ReplyRecords, SingleFeedbackActivity.SFBViewHolder>(
                ReplyRecords.class,
                R.layout.reply_list,
                SingleFeedbackActivity.SFBViewHolder.class,
                mReplyDatabase
        ) {
            @Override
            protected void populateViewHolder(SingleFeedbackActivity.SFBViewHolder viewHolder, ReplyRecords model, int position) {
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                //mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

               /* if (!mDatabaseUsers.toString().equals("https://ma-s-514f2.firebaseio.com/Users/" + model.getUid())){
                    viewHolder.mView.setVisibility(View.GONE);
                    viewHolder.mView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                } else {
                    viewHolder.mView.setVisibility(View.VISIBLE);
                } */

                final String reply_key = getRef(position).getKey().toString();

                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
                viewHolder.setReply(model.getReply());


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


    public static class SFBViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public SFBViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setDate(String date) {
            TextView response_date = mView.findViewById(R.id.textViewResponseDate);
            response_date.setText("Date: " + date);
        }

        public void setTime(String time) {
            TextView response_time = mView.findViewById(R.id.textViewResponseTime);
            response_time.setText("Time: " + time);
        }

        public void setReply(String reply) {
            TextView response_reply = mView.findViewById(R.id.textViewResponse);
            response_reply.setText(reply);
        }

    }

}
