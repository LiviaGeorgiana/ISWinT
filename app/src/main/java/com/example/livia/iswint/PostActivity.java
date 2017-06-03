package com.example.livia.iswint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelctImage;

    private EditText mPostDesc;

    private Button mSubmitBtn;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mPostDesc = (EditText) findViewById(R.id.descField);
        mSubmitBtn = (Button) findViewById(R.id.submit_btn);
        mSelctImage = (ImageButton) findViewById(R.id.imageSelect);
        mProgress = new ProgressDialog(this);

        mSelctImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galerryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galerryIntent.setType("image/*");
                startActivityForResult(galerryIntent, GALLERY_REQUEST);

            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPosting();
            }
        });
    }

        private void startPosting() {

            mProgress.setMessage("Posting to ISWinT Blog");


            final String desc_value = mPostDesc.getText().toString().trim();

            if(!TextUtils.isEmpty(desc_value) && mImageUri != null) {

                mProgress.show();

                StorageReference filepath = mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());

                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUri = taskSnapshot.getDownloadUrl();

                        DatabaseReference newPost = mDatabase.push();

                        newPost.child("desc").setValue(desc_value);
                        newPost.child("image").setValue(downloadUri.toString());
                        newPost.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        mProgress.dismiss();

                        startActivity(new Intent(PostActivity.this, account_activity.class));

                    }
                });
            }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            mImageUri = data.getData();

            mSelctImage.setImageURI(mImageUri);
    }
}
}
