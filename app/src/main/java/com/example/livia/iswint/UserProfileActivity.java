package com.example.livia.iswint;

        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.kosalgeek.android.caching.FileCacher;
        import com.squareup.picasso.Picasso;

        import java.io.IOException;


public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView mTypeText;
    private TextView mNameText;
    private TextView mEmailText;
    private TextView mPhoneText;
    private TextView mGenderText;
    private TextView mBirthText;
    private TextView mCountryText;
    private TextView mRoomText;
    private TextView mWorkshopText;

    private ImageView mUserImage;

    private DatabaseReference mDatabase;
    
    
    
    private FileCacher<String> userUidCacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){

                    Intent loginIntent = new Intent(UserProfileActivity.this, LogIn.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
        
        userUidCacher =new FileCacher<String>(UserProfileActivity.this, "profileUid");
        try {
            userUidCacher.readCache();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mTypeText = (TextView) findViewById(R.id.user_typeText) ;
        mNameText = (TextView) findViewById(R.id.user_nameText);
        mEmailText = (TextView) findViewById(R.id.user_emailText);
        mPhoneText = (TextView) findViewById(R.id.user_phoneText);
        mGenderText = (TextView) findViewById(R.id.user_genderText);
        mBirthText = (TextView) findViewById(R.id.user_birthText);
        mCountryText = (TextView) findViewById(R.id.user_departmentcountryText);
        mRoomText = (TextView) findViewById(R.id.user_roomText);
        mWorkshopText = (TextView) findViewById(R.id.user_workshopText);

        mUserImage = (ImageView) findViewById(R.id.user_image);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    dataSnapshot.child(userUidCacher.readCache()).getValue(Participant.class);
                    mTypeText.setText(dataSnapshot.child(userUidCacher.readCache())
                            .getValue(Participant.class).getType());
                    mNameText.setText(dataSnapshot.child(userUidCacher.readCache())
                            .getValue(Participant.class).getName());
                    mEmailText.setText(dataSnapshot.child(userUidCacher.readCache())
                            .getValue(Participant.class).getEmail());
                    mPhoneText.setText(dataSnapshot.child(userUidCacher.readCache())
                            .getValue(Participant.class).getPhone());
                    mGenderText.setText(dataSnapshot.child(userUidCacher.readCache())
                            .getValue(Participant.class).getGender());
                    mBirthText.setText(dataSnapshot.child(userUidCacher.readCache())
                            .getValue(Participant.class).getBirthdate());
                    mCountryText.setText(dataSnapshot.child(userUidCacher.readCache())
                            .getValue(Participant.class).getCountry());
                    mRoomText.setText(dataSnapshot.child(userUidCacher.readCache())
                            .getValue(Participant.class).getRoom());
                    mWorkshopText.setText(dataSnapshot.child(userUidCacher.readCache())
                            .getValue(Participant.class).getWorkshop());
                    if (!dataSnapshot.child(userUidCacher.readCache()).getValue(Participant.class).getImage().contains("default")){
                        Picasso.with(UserProfileActivity.this).load(
                                dataSnapshot.child(userUidCacher.readCache()).getValue(Participant.class).getImage()
                        ).fit().into(mUserImage);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }
}
