package com.programmer.jgallos.ma_i;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
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


public class NotificationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private RecyclerView recyclerView;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mNotifDatabase;

    Boolean firstFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewNotifs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mNotifDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");

        //mCurrentUser = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        mNotifDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(NotificationActivity.this,"sdfsd",Toast.LENGTH_SHORT).show();
                if (firstFlag == true) {
                    firstFlag = false;
                } else {

                    String notifText = (String) dataSnapshot.child("nnotif").getValue();
                    String notifDate = (String) dataSnapshot.child("ndate").getValue();
                    String notifTime = (String) dataSnapshot.child("ntime").getValue();

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

                        // Configure the notification channel.
                        notificationChannel.setDescription("Channel description");
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                        notificationChannel.enableVibration(true);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }


                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(NotificationActivity.this, NOTIFICATION_CHANNEL_ID);

                    Intent intent = new Intent(NotificationActivity.this, NotificationActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);

                    notificationBuilder.setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.login)
                            .setContentIntent(pendingIntent)
                            .setTicker("Program designed by: J.Gallos")
                            // .setPriority(Notification.IMPORTANCE_HIGH)
                            .setContentTitle("TILT-ACCT System")
                            .setContentText("New alert received.")
                            .setContentInfo("Info");

                    notificationManager.notify(/*notification id*/1, notificationBuilder.build());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                final String stringNotif = "Sample notif";
                final DatabaseReference newNotif = mNotifDatabase.push();

                newNotif.child("nnotif").setValue(stringNotif);
                newNotif.child("ndate").setValue("x");
                newNotif.child("ntime").setValue("x").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            //startActivity(new Intent(FeedbackReplyActivity.this, ViewFeedbackActivity.class));

                            //Intent notif_back = new Intent(FeedbackReplyActivity.this, SingleFeedbackActivity.class);
                            //reply_back.putExtra("FeedbackID",feedback_key);
                            //reply_back.putExtra("SigninSubject", signin_subject);
                            //startActivity(reply_back);
                            //finish();
                        }
                    }
                });

            }
        }); */


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<NotificationRecords, NotificationActivity.NViewHolder> FBRA = new FirebaseRecyclerAdapter<NotificationRecords, NotificationActivity.NViewHolder>(
                NotificationRecords.class,
                R.layout.notif_list,
                NotificationActivity.NViewHolder.class,
                mNotifDatabase
        ) {
            @Override
            protected void populateViewHolder(NotificationActivity.NViewHolder viewHolder, NotificationRecords model, int position) {
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                //mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

               /* if (!mDatabaseUsers.toString().equals("https://ma-s-514f2.firebaseio.com/Users/" + model.getUid())){
                    viewHolder.mView.setVisibility(View.GONE);
                    viewHolder.mView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                } else {
                    viewHolder.mView.setVisibility(View.VISIBLE);
                } */

                final String notif_key = getRef(position).getKey().toString();

                viewHolder.setNdate(model.getNdate());
                viewHolder.setNtime(model.getNtime());
                viewHolder.setNnotif(model.getNnotif());


                //Toast.makeText(ViewAttendanceActivity.this, model.getSignout().toString(), Toast.LENGTH_LONG).show();
                viewHolder.mnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        recyclerView.setAdapter(FBRA);
    }


    public static class NViewHolder extends RecyclerView.ViewHolder {
        View mnView;

        public NViewHolder(View itemView) {
            super(itemView);
            mnView=itemView;
        }

        public void setNdate(String ndate) {
            TextView notif_date = mnView.findViewById(R.id.textViewNotifDate);
            notif_date.setText(ndate);
        }

        public void setNtime(String ntime) {
            TextView notif_time = mnView.findViewById(R.id.textViewNotifTime);
            notif_time.setText(ntime);
        }

        public void setNnotif(String nnotif) {
            TextView notif = mnView.findViewById(R.id.textViewNotif);
            notif.setText(nnotif);
        }

    }

}

