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

public class ViewAcadActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_acad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewAcad);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Android_Development");
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

        FirebaseRecyclerAdapter<AcadRecords, AcadViewHolder> FBRA = new FirebaseRecyclerAdapter<AcadRecords, AcadViewHolder>(
                AcadRecords.class,
                R.layout.card_items,
                AcadViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(AcadViewHolder viewHolder, AcadRecords model, int position) {
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
                //Toast.makeText(ViewAcadActivity.this,mDatabaseUsers.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(ViewAcadActivity.this,model.getUid(),Toast.LENGTH_LONG).show();
                if (!mDatabaseUsers.toString().equals("https://ma-s-514f2.firebaseio.com/Users/" + model.getUid())) {
                    viewHolder.mView.setVisibility(View.GONE);
                    viewHolder.mView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                } else {
                    viewHolder.mView.setVisibility(View.VISIBLE);
                }

                // if (mDatabaseUsers.toString().equals("https://ma-s-514f2.firebaseio.com/Users/" + model.getUid())) {
                final String acad_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());

                viewHolder.setDesc(model.getDesc());

                viewHolder.setImageUrl(getApplicationContext(), model.getImageUrl());
                //Toast.makeText(ViewAcadActivity.this, model.getImageUrl().toString(), Toast.LENGTH_LONG).show();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


            }
        };
        recyclerView.setAdapter(FBRA);
    }

    public static class AcadViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public AcadViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setTitle(String title) {
            TextView acad_title = mView.findViewById(R.id.acadTitle);
            acad_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView acad_desc = mView.findViewById(R.id.acadDesc);
            acad_desc.setText(desc);
        }

        public void setImageUrl(Context ctx, String imageUrl) {
            ImageView acad_image = mView.findViewById(R.id.acadImage);
            Picasso.with(ctx).load(imageUrl).into(acad_image);

        }




    }

    @Override
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
    }

}
