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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.instacart.library.truetime.TrueTimeRx;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ClassSessionActivity extends AppCompatActivity {

    private EditText editMessage;
    private Button signoutBtn;
    private TextView signSubject;
    private TextView signTime;

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DatabaseReference sessDatabaseRef;
    private DatabaseReference signoutDatabaseRef;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private String timeHolder;

    String signin_key = null;
    String signin_subject = null;
    String signin_time = null;
    String signin_date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassSessionActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });

        //Spinner spinner = (Spinner) findViewById(R.id.spinnerLevel);
        // Create an ArrayAdapter using the string array and a default spinner layout
       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.feedback_level45, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // spinner.setAdapter(adapter);

        signoutBtn = (Button)findViewById(R.id.signoutBtn);
        editMessage = (EditText)findViewById(R.id.editMessage);
        signSubject = (TextView)findViewById(R.id.subjectText);
        signTime = (TextView)findViewById(R.id.signinText);

        signin_key = getIntent().getExtras().getString("SigninKey");
        signin_subject = getIntent().getExtras().getString("SigninSubject");
        signin_time = getIntent().getExtras().getString("SigninTime");
        signin_date = getIntent().getExtras().getString("SigninDate");

        signSubject.setText("Subject: " + signin_subject);
        signTime.setText("Sign-in: " + signin_time);

        databaseRef = database.getInstance().getReference().child(signin_subject + "_lesson");
        sessDatabaseRef = database.getInstance().getReference().child("Class Signins");
        signoutDatabaseRef = database.getInstance().getReference().child(signin_subject + "_attendance").child(signin_key);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        final DatabaseReference newSignin = sessDatabaseRef.push();

        newSignin.child("class").setValue(signin_subject);
        newSignin.child("userid").setValue(mCurrentUser.getUid());
        newSignin.child("signinkey").setValue(signin_key);

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassSessionActivity.this, "Signing out...",Toast.LENGTH_SHORT).show();
                final String feedback = editMessage.getText().toString().trim();
                final DatabaseReference newFeedback = databaseRef.push();
                Map<String, Object> updates = new HashMap<String, Object>();

                timeHolder = updateTime(2);
                updates.put("signout",timeHolder);
                signoutDatabaseRef.updateChildren(updates);

                //newFeedback.child("lesson").setValue("5");
                newFeedback.child("lessonDescription").setValue(editMessage.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(ClassSessionActivity.this,MainActivity.class));
                            finish();
                        }
                    }
                });


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_addAcad) {
           // startActivity(new Intent(ClassSessionActivity.this, AddAcadActivity.class));
            Intent addAcadIntent = new Intent(ClassSessionActivity.this, AddAcadActivity.class);
            addAcadIntent.putExtra("SigninSubject",signin_subject);
            addAcadIntent.putExtra("SigninDate", signin_date);
            startActivity(addAcadIntent);

        } else if (id==R.id.action_viewAcad) {
            Intent viewAcadIntent = new Intent(ClassSessionActivity.this, ViewAcadActivity.class);
            viewAcadIntent.putExtra("SigninSubject", signin_subject);
            viewAcadIntent.putExtra("SigninDate", signin_date);
            startActivity(viewAcadIntent);
            //startActivity(new Intent(ClassSessionActivity.this, ViewAcadActivity.class));
        } else if (id==R.id.action_viewAttendance) {
            Intent viewAttendanceIntent = new Intent(ClassSessionActivity.this, ViewAttendanceActivity.class);
            viewAttendanceIntent.putExtra("SigninSubject", signin_subject);
            startActivity(viewAttendanceIntent);

            //startActivity(new Intent(ClassSessionActivity.this, ViewAttendanceActivity.class));
        } else if (id==R.id.action_viewFeedback) {
            Intent viewFeedbackIntent = new Intent(ClassSessionActivity.this, ViewFeedbackActivity.class);
            viewFeedbackIntent.putExtra("SigninSubject", signin_subject);
            startActivity(viewFeedbackIntent);

            //startActivity(new Intent(ClassSessionActivity.this, ViewFeedbackActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private String updateTime(int flag) {
        TextView textTime = (TextView) findViewById(R.id.textViewOutTime);

        if (!TrueTimeRx.isInitialized()) {
            Toast.makeText(ClassSessionActivity.this, "TrueTime is not yet initialized.", Toast.LENGTH_SHORT).show();
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
