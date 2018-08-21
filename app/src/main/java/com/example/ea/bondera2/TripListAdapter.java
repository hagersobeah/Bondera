package com.example.ea.bondera2;

import android.content.Context;
import android.mtp.MtpConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ea on 12/22/2017.
 */


public class TripListAdapter extends BaseAdapter {


    private Context mContext;
    private List<Trip> mTripList;

    //Constructor
    public TripListAdapter(Context mContext, List<Trip> mTripList){


        this.mContext=mContext;
        this.mTripList=mTripList;
    }

    @Override
    public int getCount() {
        return mTripList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.trip_details, null);
        TextView tvDestination = (TextView)v.findViewById(R.id.tvTripDestination);
        TextView tvDuration = (TextView)v.findViewById(R.id.tvTripDuration);
        TextView tvTripFare = (TextView)v.findViewById(R.id.tvTripFare);




        //set text for textView
        tvDestination.setText("Your trip to " + mTripList.get(position).getDestination());
        tvTripFare.setText(mTripList.get(position).getTripFare() + " E£");

        String duration =  mTripList.get(position).getTripDuration();
        String[] durationText = duration.split(":");
        String hrs = durationText[0];
        int hrsInteger= Integer.parseInt(hrs);

        String mins = durationText[1];
       int timeInMins = hrsInteger *60;
        mins= timeInMins + mins;


            tvDuration.setText(mins + " mins");


        //Save product id to tag
        v.setTag(mTripList.get(position).getTripID());
        return v;

    }
}

