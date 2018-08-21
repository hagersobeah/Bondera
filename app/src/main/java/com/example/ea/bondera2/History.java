package com.example.ea.bondera2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private DatabaseReference historyDatabaseReference;
    private ListView historyList;

    private ArrayList<Trip> trips = new ArrayList<Trip>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyDatabaseReference = FirebaseDatabase.getInstance().getReference();
        historyList = (ListView) findViewById(R.id.tripsHistory);

        //Array Adapter
        ArrayAdapter arrayAdapter = new TripAdapter(this,trips);
        historyList.setAdapter(arrayAdapter);

        historyDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Trip value = dataSnapshot.getValue(Trip.class);
                trips.add(value);
                //arrayAdapter.notifyDataSetChanged();
                        
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
