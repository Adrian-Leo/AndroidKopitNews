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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register,forgot;
    private EditText emailL,passwordL;
    private Button login;


    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        register=(TextView)findViewById(R.id.txtRegister);
        register.setOnClickListener(this);

        forgot=(TextView)findViewById(R.id.txtForgot);
        forgot.setOnClickListener(this);

        emailL=(EditText)findViewById(R.id.edtEmail);
        passwordL=(EditText)findViewById(R.id.edtPassword);

        login = (Button)findViewById(R.id.btnLogin);
        login.setOnClickListener(this);


        progressBar=(ProgressBar)findViewById(R.id.progress);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtRegister:
                startActivity(new Intent(this,RegisterUser.class));
                break;

            case R.id.btnLogin:
                LoginUser();
                break;

            case R.id.txtForgot:
                startActivity(new Intent(this,forgotPassword.class));
                break;

        }
    }



    private void LoginUser() {
        String emailLgn = emailL.getText().toString().trim();
        String passwordLgn=passwordL.getText().toString().trim();

        if(emailLgn.isEmpty()){
            emailL.setError("Email is required! ");
            emailL.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailLgn).matches()){
            emailL.setError("Wrong email format! ");
            emailL.requestFocus();
            return;
        }
        if(passwordLgn.isEmpty()){
            passwordL.setError("Password is required! ");
            passwordL.requestFocus();
            return;
        }
        if(passwordLgn.length()<6){
            passwordL.setError("Password must more than 6 character! ");
            passwordL.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailLgn,passwordLgn)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if(user.isEmailVerified()){
                              startActivity(new Intent(MainActivity.this,Home.class));
                            }
                            else{
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this,"Check your email to verified your account",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(MainActivity.this,MainActivity.class));
                            }

                        }else{
                            Toast.makeText(MainActivity.this,"Failed to login ! please check your email and password!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}