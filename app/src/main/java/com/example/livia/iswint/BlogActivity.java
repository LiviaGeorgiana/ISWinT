package com.example.livia.iswint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kosalgeek.android.caching.FileCacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

public class BlogActivity extends AppCompatActivity {

    private RecyclerView mBlogList;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabaseUsers;

    private boolean mProcessLike = false;

    private DatabaseReference mDatabaseLike;

    private DatabaseReference mDatabaseLikeNumber;
    private long nrOfLikes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseLike.keepSynced(true);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabase.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){

                    Intent loginIntent = new Intent(BlogActivity.this, LogIn.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };



        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Blog, BlogViewHolder > firebaseRecyclerAdapter =
                                                new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setProfileImage(getApplicationContext(), model.getProfileimage());

                viewHolder.mView.findViewById(R.id.post_username).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FileCacher<String> userUidCacher =
                                            new FileCacher<String>(BlogActivity.this, "profileUid");
                        try {
                            userUidCacher.writeCache(model.getUid());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(BlogActivity.this, UserProfileActivity.class));

                    }
                });

                viewHolder.mView.findViewById(R.id.profile_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FileCacher<String> userUidCacher =
                                new FileCacher<String>(BlogActivity.this, "profileUid");
                        try {
                            userUidCacher.writeCache(model.getUid());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(BlogActivity.this, UserProfileActivity.class));
                    }
                });



                mDatabaseLikeNumber  = FirebaseDatabase.getInstance().getReference().child("Likes")
                        .child(post_key);
                mDatabaseLikeNumber.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        nrOfLikes = dataSnapshot.getChildrenCount();
                        viewHolder.setNrLikes(String.valueOf(nrOfLikes));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                viewHolder.setLoveBtn(post_key);

                viewHolder.mLoveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mProcessLike = true;

                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (mProcessLike) {

                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();

                                        mProcessLike = false;

                                    } else {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("random");

                                        mProcessLike = false;

                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        };



        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;

        ImageView mLoveBtn;
        TextView mNrLikes;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;

        public BlogViewHolder(View itemView) {
            super(itemView);

             mView = itemView;

            mLoveBtn = (ImageView) mView.findViewById(R.id.love_btn);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();
            mDatabaseLike.keepSynced(true);
            mNrLikes = (TextView) mView.findViewById(R.id.txtNrLikes);

        }

        public void setLoveBtn(final String post_key){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){

                        mLoveBtn.setImageResource(R.mipmap.green_heart);

                    }else{

                        mLoveBtn.setImageResource(R.mipmap.greyheart3);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setDesc(String desc){

            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void  setProfileImage(final Context ctxx, final String profilimage) {

            final ImageView profile_image = (ImageView) mView.findViewById(R.id.profile_pic);

            Picasso.with(ctxx).load(profilimage).networkPolicy(NetworkPolicy.OFFLINE).fit().into(profile_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctxx).load(profilimage).into(profile_image);

                }
            });
        }

        public void setUsername(String username){

            TextView post_username = (TextView) mView.findViewById(R.id.post_username);
            post_username.setText(username);

        }

        public void setImage(final Context ctx, final String image){

            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);

            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(image).into(post_image);

                }
            });
        }

        public void setNrLikes(String nrLikes){
            mNrLikes.setText(nrLikes);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add) {

            startActivity(new Intent(BlogActivity.this, PostActivity.class));
        }

        if(item.getItemId() == R.id.users){
            startActivity(new Intent(BlogActivity.this, UsersActivity.class));
        }

        if(item.getItemId() == R.id.action_logout){

            logout();
        }

        if(item.getItemId() == R.id.action_gotoprofile){
            startActivity(new Intent(BlogActivity.this, ProfileActivity.class));
        }

        return super.onOptionsItemSelected(item);

    }

    private void logout() {

        mAuth.signOut();
    }

}

