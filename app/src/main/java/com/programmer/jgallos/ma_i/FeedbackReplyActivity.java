package com.programmer.jgallos.ma_i;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackReplyActivity extends AppCompatActivity {

    String feedback_key = null;
    private TextView feedbackID;
    private EditText editText_reply;
    private Button button_send;

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    String signin_subject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_reply);

        feedback_key = getIntent().getExtras().getString("SingleFeedbackID");
        signin_subject = getIntent().getExtras().getString("SigninSubject");
        feedbackID = (TextView)findViewById(R.id.textViewFID);

        editText_reply = (EditText)findViewById(R.id.editTextReply);
        button_send = (Button)findViewById(R.id.buttonSend);

        databaseRef = database.getInstance().getReference().child("Reply_" + feedback_key);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        feedbackID.setText(feedback_key);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stringReply = editText_reply.getText().toString().trim();
                final DatabaseReference newReply = databaseRef.push();

                newReply.child("reply").setValue(stringReply);
                newReply.child("date").setValue("x");
                newReply.child("time").setValue("x").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            //startActivity(new Intent(FeedbackReplyActivity.this, ViewFeedbackActivity.class));

                            Intent reply_back = new Intent(FeedbackReplyActivity.this, SingleFeedbackActivity.class);
                            reply_back.putExtra("FeedbackID",feedback_key);
                            reply_back.putExtra("SigninSubject", signin_subject);
                            startActivity(reply_back);
                            finish();
                        }
                    }
                });


            }
        });

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
    }

}
