package com.example.ea.bondera2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hagar on 2017-12-18.
 */

class TripAdapter extends ArrayAdapter<Trip> {

    public TripAdapter(@NonNull Context context, ArrayList<Trip> trips) {
        super(context, R.layout.trip_scheme ,trips);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Trip singleTrip = getItem(position);

        LayoutInflater tripsInflater = LayoutInflater.from(getContext());
        View customTripView = tripsInflater.inflate(R.layout.trip_scheme,parent,false);


        //get a reference to everything
        TextView startLocation = (TextView) customTripView.findViewById(R.id.startLocationRetrieved);
        TextView destination = (TextView) customTripView.findViewById(R.id.destinationRetrieved);
        TextView time = (TextView) customTripView.findViewById(R.id.tripTimeRetrieved);
        TextView fare = (TextView) customTripView.findViewById(R.id.tripFareRetrieved);

        //set the values
        startLocation.setText(singleTrip.getOrigin());
        destination.setText(singleTrip.getDestination());
        time.setText(singleTrip.retrieveStartTime());

        return customTripView;
    }
}
