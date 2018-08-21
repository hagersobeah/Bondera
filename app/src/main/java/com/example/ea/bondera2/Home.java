package com.example.ea.bondera2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private Button startTripBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startTripBtn = (Button)findViewById(R.id.startTripBtn);
        startTripBtn.setOnClickListener(this);

    }

    public void onClick(View view){

        switch(view.getId()){
            case R.id.startTripBtn:

                Intent intent = new Intent(Home.this,MapsActivity.class);
                startActivity(intent);
                break;
        }
    }



}
