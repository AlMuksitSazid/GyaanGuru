package com.gyaanguru.Account_details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.database.FirebaseDatabase;
import com.gyaanguru.Activities.TermsActivity;
import com.gyaanguru.Models.Users;
import com.gyaanguru.R;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Button SignUp;
    EditText username, email, password;
    ProgressDialog progressDialog;
    CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        SignUp = findViewById(R.id.signup);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        checkbox = (CheckBox) findViewById(R.id.checkbox);

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Account Creating");
        progressDialog.setMessage("Please wait until the process finished");

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String emailStr = email.getText().toString().toLowerCase();
                String passwordStr = password.getText().toString();
                if (emailStr.isEmpty() || passwordStr.isEmpty() || username.getText().toString().isEmpty()) {
                    Toasty.error(SignupActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                    return;
                } else if (passwordStr.length() < 6) {
                    Toasty.warning(SignupActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                    return;
                } else if (!checkbox.isChecked()) {
                    Toasty.info(SignupActivity.this, "Please accept the terms and conditions", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                    return;
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        String encryptedPassword = HashUtil.sha256(passwordStr);
                                        Users users = new Users(username.getText().toString(), emailStr, encryptedPassword);
                                        String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                                        firebaseDatabase.getReference().child("Users").child(id).setValue(users);
                                        Toasty.success(SignupActivity.this, "Account Registered Successfully", Toast.LENGTH_SHORT, true).show();
                                        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toasty.info(SignupActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG, true).show();
                                        progressDialog.dismiss();
                                        return;
                                    }
                                }
                            });
                    return;
                }
            }
        });
    }

    public void login(View view) {
        startActivity(new Intent(getApplicationContext(), SigninActivity.class));
    }


    public void terms(View view) {
        startActivity(new Intent(getApplicationContext(), TermsActivity.class));
    }
}