package com.example.livia.iswint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.MessagePattern;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mSetupImageBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private DatabaseReference mDatabase;
    private TextView mTypeText;
    private TextView mNameText;
    private TextView mEmailText;
    private TextView mPhoneText;
    private TextView mGenderText;
    private TextView mBirthText;
    private TextView mCountryText;
    private TextView mRoomText;
    private TextView mWorkshopText;

    private DatabaseReference mRef;

    private DatabaseReference mDatabaseUsers;

    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private StorageReference mStorageImage;

    private String downloarUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSetupImageBtn = (ImageView) findViewById(R.id.setupImageBtn);

        mSetupImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/=");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

               // startSetupAccount();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(ProfileActivity.this, LogIn.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mTypeText = (TextView) findViewById(R.id.typeText);
        mNameText = (TextView) findViewById(R.id.nameText);
        mEmailText = (TextView) findViewById(R.id.emailText);
        mPhoneText = (TextView) findViewById(R.id.phoneText);
        mGenderText = (TextView) findViewById(R.id.genderText);
        mBirthText = (TextView) findViewById(R.id.birthText);
        mCountryText = (TextView) findViewById(R.id.departmentcountryText);
        mRoomText = (TextView) findViewById(R.id.roomText);
        mWorkshopText = (TextView) findViewById(R.id.workshopText);



        mDatabase = mDatabase.child("Users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child(firebaseUser.getUid()).getValue(Participant.class);
                mTypeText.setText(dataSnapshot.child(firebaseUser.getUid())
                        .getValue(Participant.class).getType());
                mNameText.setText(dataSnapshot.child(firebaseUser.getUid())
                        .getValue(Participant.class).getName());
                mEmailText.setText(dataSnapshot.child(firebaseUser.getUid())
                        .getValue(Participant.class).getEmail());
                mPhoneText.setText(dataSnapshot.child(firebaseUser.getUid())
                        .getValue(Participant.class).getPhone());
                mGenderText.setText(dataSnapshot.child(firebaseUser.getUid())
                        .getValue(Participant.class).getGender());
                mBirthText.setText(dataSnapshot.child(firebaseUser.getUid())
                        .getValue(Participant.class).getBirthdate());
                mCountryText.setText(dataSnapshot.child(firebaseUser.getUid())
                        .getValue(Participant.class).getCountry());
                mRoomText.setText(dataSnapshot.child(firebaseUser.getUid())
                        .getValue(Participant.class).getRoom());
                mWorkshopText.setText(dataSnapshot.child(firebaseUser.getUid())
                        .getValue(Participant.class).getWorkshop());
                if (!dataSnapshot.child(firebaseUser.getUid()).getValue(Participant.class).getImage().contains("default")){
                    Picasso.with(ProfileActivity.this).load(
                            dataSnapshot.child(firebaseUser.getUid()).getValue(Participant.class).getImage()
                    ).fit().into(mSetupImageBtn);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.action_logout) {

            logout();
        }

        if (item.getItemId() == R.id.action_gotoblog) {
            startActivity(new Intent(ProfileActivity.this, BlogActivity.class));
        }

        return super.onOptionsItemSelected(item);

    }

    private void logout() {

        mAuth.signOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                Picasso.with(ProfileActivity.this).load(mImageUri)
                        .fit().into(mSetupImageBtn);
                //mSetupImageBtn.setImageURI(resultUri);

                startSetupAccount();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void startSetupAccount() {

        final String user_id = mAuth.getCurrentUser().getUid();

        StorageReference filepath = mStorageImage.child(mImageUri.getLastPathSegment());
        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloarUri = taskSnapshot.getDownloadUrl().toString();

                mDatabaseUsers.child(user_id).child("image").setValue(downloarUri);
            }
        });
    }

}

