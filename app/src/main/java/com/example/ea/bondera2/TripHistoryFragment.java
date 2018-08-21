package com.example.ea.bondera2;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.realtime.util.StringListReader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.ea.bondera2.R.id.container;


public class TripHistoryFragment extends ListFragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    //private ListView lv;


   // public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //this will hold our collection of trips
        //View view = inflater.inflate(R.layout.list_fragment,container,false);
        //ListView lv = (ListView)view.findViewById(R.id.listViewHistory);
        final List<Trip> trips = new ArrayList<Trip>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference.child("Users").child(firebaseUser.getUid()).child("Trips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                //get all of the trips for the current user who is signed in
                Iterable<com.google.firebase.database.DataSnapshot> children = dataSnapshot.getChildren();

                //loop on each trip
                for (com.google.firebase.database.DataSnapshot child: children) {
                    Trip trip = child.getValue(Trip.class);
                    trips.add(trip);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter<Trip> tripAdapter = new ArrayAdapter<Trip>(getActivity(),android.R.layout.simple_list_item_1,trips);
        //lv.setAdapter(tripAdapter);
        setListAdapter(tripAdapter);
           getListAdapter();
       // return view;
    }}