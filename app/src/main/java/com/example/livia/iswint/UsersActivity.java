package com.example.livia.iswint;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;
import com.kosalgeek.android.caching.FileCacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView mUsersList;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private SearchView mSearchView;
    private ImageView mSearchBtn;
    private EditText mNameToSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //mSearchView = (SearchView) findViewById(R.id.search_user);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.keepSynced(true);

        mUsersList = (RecyclerView) findViewById(R.id.users_viewList);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        mNameToSearch = (EditText) findViewById(R.id.search_nametxt);

        mSearchBtn = (ImageView) findViewById(R.id.search_userBtn);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseRecyclerAdapter<Participant, UserViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<Participant, UserViewHolder>(
                                Participant.class,
                                R.layout.user_row,
                                UsersActivity.UserViewHolder.class,
                                mDatabase
                        ) {
                            @Override
                            protected void populateViewHolder(UserViewHolder viewHolder, final Participant model, int position) {

                                final String post_key = getRef(position).getKey();
                                if(model.getName().toUpperCase().startsWith(mNameToSearch.getText().toString().trim().toUpperCase())
                                        || model.getEmail().startsWith(mNameToSearch.getText().toString().trim())
                                        || mNameToSearch.getText().toString().trim().contains(model.getRoom())
                                        || model.getPhone().startsWith(mNameToSearch.getText().toString().trim())){
                                    viewHolder.setUsername(model.getName());
                                    viewHolder.setType("("+model.getType()+")");
                                    viewHolder.setUser_image(UsersActivity.this, model.getImage());

                                    viewHolder.mView.findViewById(R.id.cardView_users).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            FileCacher<String> userUidCacher =
                                                    new FileCacher<String>(UsersActivity.this, "profileUid");
                                            try {
                                                userUidCacher.writeCache(post_key);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            startActivity(new Intent(UsersActivity.this, UserProfileActivity.class));

                                        }
                                    });
                                }
                                else{
                                    viewHolder.mCard.setVisibility(View.GONE);
                                }
                            }
                        };

                mUsersList.setAdapter(firebaseRecyclerAdapter);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(UsersActivity.this, LogIn.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Participant, UserViewHolder> firebaseRecyclerAdapter =
                                            new FirebaseRecyclerAdapter<Participant, UserViewHolder>(
                Participant.class,
                R.layout.user_row,
                UsersActivity.UserViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, final Participant model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setUsername(model.getName());
                viewHolder.setType("("+model.getType()+")");
                viewHolder.setUser_image(UsersActivity.this, model.getImage());

                viewHolder.mView.findViewById(R.id.cardView_users).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FileCacher<String> userUidCacher =
                                new FileCacher<String>(UsersActivity.this, "profileUid");
                        try {
                            userUidCacher.writeCache(post_key);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(UsersActivity.this, UserProfileActivity.class));

                    }
                });


            }
        };

        mUsersList.setAdapter(firebaseRecyclerAdapter);

    }


    public static class UserViewHolder extends RecyclerView.ViewHolder{

        View mView;
        FirebaseAuth mAuth;
        CardView mCard;

        public UserViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mAuth = FirebaseAuth.getInstance();
            mCard = (CardView) mView.findViewById(R.id.cardView_users);

        }

        public void setUsername(String username){

            TextView user_name = (TextView) mView.findViewById(R.id.user_viewname);
            user_name.setText(username);
        }

        public void  setUser_image(final Context ctxx, final String user_image) {

            final ImageView userimage = (ImageView) mView.findViewById(R.id.user_viewimage);

            Picasso.with(ctxx).load(user_image).networkPolicy(NetworkPolicy.OFFLINE).fit().into(userimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctxx).load(user_image).into(userimage);

                }
            });
        }

        public void setType(String type){

            TextView user_type = (TextView) mView.findViewById(R.id.user_viewtype);
            user_type.setText(type);
        }
    }
}
