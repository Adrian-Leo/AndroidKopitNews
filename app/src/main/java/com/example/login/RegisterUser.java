package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView bannerV,registerUserV;
    private EditText edtNameV,edtAgeV,edtEmailV,edtPasswordV;
    private ProgressBar progressBarV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        bannerV =(TextView)findViewById(R.id.banner);
        bannerV.setOnClickListener(this);

        registerUserV =(Button)findViewById(R.id.btnRegister);
        registerUserV.setOnClickListener(this);

        edtNameV=(EditText)findViewById(R.id.edtName);
        edtAgeV=(EditText)findViewById(R.id.edtAge);
        edtEmailV=(EditText)findViewById(R.id.edtEmail);
        edtPasswordV=(EditText)findViewById(R.id.edtPassword);

        progressBarV=(ProgressBar)findViewById(R.id.progress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.btnRegister:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String name = edtNameV.getText().toString().trim();
        String age = edtAgeV.getText().toString().trim();
        String email = edtEmailV.getText().toString().trim();
        String password = edtPasswordV.getText().toString().trim();

        if(name.isEmpty()){
            edtNameV.setError("Name is Required");
            edtNameV.requestFocus();
            return;
        }
        if(age.isEmpty()){
            edtAgeV.setError("age is Required");
            edtAgeV.requestFocus();
            return;
        }
        if(email.isEmpty()){
            edtEmailV.setError("Email is Required");
            edtEmailV.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmailV.setError("Please Input Valid Email");
            edtEmailV.requestFocus();
            return;

        }

        if(password.isEmpty()){
            edtPasswordV.setError("Name is Required");
            edtPasswordV.requestFocus();
            return;
        }

        if(password.length() < 6){
            edtEmailV.setError("Password must be more than 6 character!");
            edtEmailV.requestFocus();
            return;
        }

        progressBarV.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name,email,age);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "User has been registered sucessfuly!",Toast.LENGTH_LONG).show();
                                        progressBarV.setVisibility(View.GONE);
                                        startActivity(new Intent(RegisterUser.this,MainActivity.class));
                                    } else {
                                        Toast.makeText(RegisterUser.this,"Failed to register! Try again!1",Toast.LENGTH_LONG).show();
                                        progressBarV.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this,"Failed to register! Try again!2",Toast.LENGTH_LONG).show();
                            progressBarV.setVisibility(View.GONE);
                    }

                    }
                });
    }
}