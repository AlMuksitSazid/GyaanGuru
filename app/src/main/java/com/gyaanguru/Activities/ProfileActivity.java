package com.gyaanguru.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.gyaanguru.Account_details.SigninActivity;
import com.gyaanguru.R;
import com.gyaanguru.databinding.ActivityProfileBinding;

import de.hdodenhof.circleimageview.CircleImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;
    TextView userNameTxt, userEmailTxt;
    CircleImageView profileImage;
    Uri imageUri;

    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.top_background));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MultiDex.install(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        profileImage = (CircleImageView) findViewById(R.id.profileImage);
        userNameTxt = (TextView) findViewById(R.id.userName);
        userEmailTxt = (TextView) findViewById(R.id.userEmail);

        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("username") && sharedPreferences.contains("email")){
            userNameTxt.setText(sharedPreferences.getString("username", ""));
            userEmailTxt.setText(sharedPreferences.getString("email", ""));
        }
        // Try to load profile image from SharedPreferences first
        String storedProfileImageUrl = sharedPreferences.getString("profileImageUrl", null);
        if (storedProfileImageUrl != null) {
            loadProfileImage(storedProfileImageUrl);
        }

        // Read the userEmail from the database
        userRef = firebaseDatabase.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
    //    userRef.child("profileImageUrl").setValue(imageUri.toString());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences.Editor editor = sharedPreferences.edit(); // Initialize editor

                String userName = dataSnapshot.child("userName").getValue(String.class);
                userNameTxt.setText(userName != null ? userName : "Name notfound");
                editor.putString("username", userName); // Store username

                String userEmail = dataSnapshot.child("email").getValue(String.class);
                userEmailTxt.setText(userEmail != null ? userEmail : "Email not found");
                editor.putString("email", userEmail); // Store email

                String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                if (profileImageUrl != null) {
                    loadProfileImage(profileImageUrl);  // Load the image into the CircleImageView using Glide or Picasso
                    editor.putString("profileImageUrl", profileImageUrl);  // Update SharedPreferences with the latest profile image URL
                }
            //    editor.commit(); // Save all changes synchronously
                editor.apply(); // Apply changes asynchronously
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseError", "Error reading user data: " + error.getMessage());
            }
        });

        binding.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?").setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {firebaseAuth.signOut();
                                Intent intent = new Intent(ProfileActivity.this, SigninActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .create()
                        .show();
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select an image"),1);
            }
        });

    }

    private void loadProfileImage(String imageUrl) {
        // Using Glide:
        Glide.with(this)
                .load(imageUrl)
                .into(profileImage);
        // Or, using Picasso:
        // Picasso.get().load(imageUrl).into(profileImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri=data.getData();
            try {
                Bitmap imageBitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(imageBitmap);
                upload(imageUri);
            }catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void upload(Uri imageUri) {
        if (imageUri!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference ref = storageReference.child("Users/" + firebaseAuth.getCurrentUser().getUid() + "/Image/" + UUID.randomUUID().toString());
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String downmainImageuri = task.getResult().toString();
                                        progressDialog.dismiss();

                                        // Store the download URL in the database
                                        userRef.child("profileImageUrl").setValue(downmainImageuri) // Use download URL
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void Void) {
                                                        Toast.makeText(ProfileActivity.this, "Profile picture saved", Toast.LENGTH_SHORT).show();
                                                        // You can reload the profile image here if needed
                                                        loadProfileImage(downmainImageuri);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ProfileActivity.this, "Failed to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Failed to get download", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finish();
    }
}