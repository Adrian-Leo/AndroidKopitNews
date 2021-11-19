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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class forgotPassword extends AppCompatActivity {

    private EditText emailRst;
    private Button resetBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailRst=(EditText)findViewById(R.id.edtEmailRst);

        resetBtn=(Button)findViewById(R.id.btnReset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        progressBar=(ProgressBar)findViewById(R.id.progress) ;

        mAuth= FirebaseAuth.getInstance();
    }

    private void resetPassword(){
        String email = emailRst.getText().toString().trim();

        if(email.isEmpty()){
            emailRst.setError("Email is required!");
            emailRst.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailRst.setError("please provide valid email");
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(forgotPassword.this,"Check your email to reset password !",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(forgotPassword.this,MainActivity.class));
                        } else {
                            Toast.makeText(forgotPassword.this,"Something wrong! please input valid email!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}