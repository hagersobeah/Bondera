package com.example.ea.bondera2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class SignUp extends AppCompatActivity {
    private Button signUpbtn2;
    private ProgressDialog progressDialog;
    private EditText dateOfBirthEdittext;
    private RadioButton radioSexBtn;
    private RadioGroup radioGroup;
    private EditText usernameedittext;
    private EditText emailedittext;
    private EditText passwordedittext;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        signUpbtn2 = (Button) findViewById(R.id.signUpbtn2);
        usernameedittext = (EditText) findViewById(R.id.usernameedittext);
        emailedittext = (EditText) findViewById(R.id.emailedittext);
        passwordedittext = (EditText) findViewById(R.id.passwordedittext);
        dateOfBirthEdittext = (EditText) findViewById(R.id.dateOfBirth);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        progressDialog = new ProgressDialog(this);
        addListenerOnButton();

    }

    public void addListenerOnButton() {

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        signUpbtn2 = (Button) findViewById(R.id.signUpbtn2);

        signUpbtn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                radioSexBtn = (RadioButton) findViewById(selectedId);

                final String gender = radioSexBtn.getText().toString().trim();
                final String userName = usernameedittext.getText().toString().trim();
                final String email = emailedittext.getText().toString().trim();
                final String dateOfBirth = dateOfBirthEdittext.getText().toString();
                String password = passwordedittext.getText().toString().trim();


                firebaseAuth = FirebaseAuth.getInstance();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUp.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUp.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Creating an account..");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();
                                    String userID = firebaseUser.getUid();
                                    User user = new User(userID, userName, email, dateOfBirth, gender);
                                    saveUserInfo(user);

                                    Toast.makeText(SignUp.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                                } else {
                                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }
        });
    }




    private void saveUserInfo(User user) {

        userDatabaseReference.child(user.getUserID()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Info Added to the database", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }


}
