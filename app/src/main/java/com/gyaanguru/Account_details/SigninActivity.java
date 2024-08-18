package com.gyaanguru.Account_details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyaanguru.Activities.MainActivity;
import java.util.Objects;
import com.gyaanguru.R;

import es.dmoral.toasty.Toasty;

public class SigninActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;
    Button SignIn;
    EditText email, password;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        SignIn = findViewById(R.id.signin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        progressDialog = new ProgressDialog(SigninActivity.this);
        progressDialog.setTitle("Account Logging");
        progressDialog.setMessage("Please wait to login your account");

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                String encryptedPassword = HashUtil.sha256(passwordStr);

                if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                    Toasty.error(SigninActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                    return;
                } else if (passwordStr.length() < 6) {
                    Toasty.warning(SigninActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                    return;
                } else if (!emailStr.contains("@") || !emailStr.contains(".")) {
                    Toasty.warning(SigninActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                    return;
                } else {
                    firebaseAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                                        userRef = firebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                progressDialog.dismiss();
                                                String storedPassword = snapshot.child("password").getValue(String.class);
                                                if (storedPassword != null && storedPassword.equals(encryptedPassword)) {
                                                    Toasty.success(SigninActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT, true).show();
                                                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toasty.warning(SigninActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT, true).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                progressDialog.dismiss();
                                                Toasty.error(SigninActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT, true).show();
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                        Toasty.error(SigninActivity.this, "Incorrect Email or Password", Toast.LENGTH_LONG, true).show();
                                    }
                                }
                            });
                    return;
                }

            }
        });


    }

    public void register(View view) {
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}