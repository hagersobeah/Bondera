package com.example.ea.bondera2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ea on 12/22/2017.
 */

public class TripHistory extends Activity {
    private ListView lvTrip;
    private TripListAdapter adapter;
    private List<Trip> mTripList;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*super.onCreate(savedInstanceState);*/
        setContentView(R.layout.trips_history);
        lvTrip = (ListView)findViewById(R.id.historyListView);
        mTripList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Adding a header for the list view
        TextView textView = new TextView(getApplicationContext());
        textView.setText("Trips History");
        lvTrip.addHeaderView(textView);


        //get data from firebase

        databaseReference.child("Users").child(firebaseUser.getUid()).child("Trips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                //get all of the trips for the current user who is signed in
                Iterable<com.google.firebase.database.DataSnapshot> children = dataSnapshot.getChildren();

                //loop on each trip
                for (com.google.firebase.database.DataSnapshot child: children) {
                    Trip trip = child.getValue(Trip.class);

                    mTripList.add(trip);


                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });







        adapter = new TripListAdapter(getApplicationContext(),mTripList);
        lvTrip.setAdapter(adapter);

        lvTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(TripHistory.this, "Clicked trip id"+ view.getTag(), Toast.LENGTH_SHORT).show();


            }
        });

    }
}
