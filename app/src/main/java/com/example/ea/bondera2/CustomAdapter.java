package com.example.ea.bondera2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ea on 12/19/2017.
 */

public class CustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<Trip> trips;
    public CustomAdapter(Context c, ArrayList<Trip> trips) {
        this.c = c;
        this.trips = trips;
    }
    @Override
    public int getCount() {
        return trips.size();
    }
    @Override
    public Object getItem(int position) {
        return trips.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.model,parent,false);
        }

        //ONITECLICK
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}

