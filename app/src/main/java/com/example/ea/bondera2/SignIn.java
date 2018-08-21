package com.example.ea.bondera2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.Calendar;

public class SignIn extends AppCompatActivity implements View.OnClickListener{
    private Button signInbtn;
    private Button signUpbtn;
    private Button forgetpasswordbtn;
    private EditText emailtext;
    private EditText passwordtext;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        emailtext = (EditText) findViewById(R.id.emailtext);
        passwordtext = (EditText) findViewById(R.id.passwordtext) ;
        signInbtn = (Button) findViewById(R.id.signInbtn);
        signUpbtn = (Button) findViewById(R.id.signUpbtn);
        forgetpasswordbtn = (Button) findViewById(R.id.forgetpasswordbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){

            //if user is already signed in go to MapsActivity activity directly
            startActivity(new Intent(SignIn.this,MapsActivity.class));

        }
        forgetpasswordbtn = (Button) findViewById(R.id.forgetpasswordbtn);
        passwordtext = (EditText) findViewById(R.id.passwordtext);
        progressDialog = new ProgressDialog(this);
        signInbtn.setOnClickListener(this);
        forgetpasswordbtn.setOnClickListener(this);
        signUpbtn.setOnClickListener(this);
    }
    private void signIn() {
        String email = emailtext.getText().toString().trim();
        String password = passwordtext.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            Date currentTime = Calendar.getInstance().getTime();
            String time= currentTime.toString();
            Toast.makeText(this, time, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Signing in..");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            finish();

                            Intent intent = new Intent(SignIn.this,MapsActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signInbtn:
                signIn();
                break;
            case R.id.signUpbtn:
                Intent intent1 = new Intent(SignIn.this,SignUp.class);
                startActivity(intent1);
                break;
           /* case R.id.forgetpasswordbtn:
                Intent intent2 = new Intent(SignIn.this,TripHistoryFragment.class);
                startActivity(intent2);
                break;*/
        }

    }

}
