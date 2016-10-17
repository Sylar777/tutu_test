package com.example.pc.applicationfortutu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class StationAdapter extends BaseAdapter {
    Context context;
    ArrayList<Station> arrayList;
    LayoutInflater inflater;
    public int positionP;

    public StationAdapter(Context context, ArrayList<Station> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        positionP = position;

        LayoutInflater lInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view=convertView;
        if(view==null){
            view = lInflater.inflate(R.layout.choose_line,parent,false);
        }

        Station station = (Station) getItem(position);

        ((TextView) view.findViewById(R.id.country)).setText(station.getCountryTitle());
        ((TextView) view.findViewById(R.id.city)).setText(station.getCityTitle());
        ((TextView) view.findViewById(R.id.station)).setText(station.getStationTitle());

        return view;
    }

    Station getStation(int position) {
        return ((Station) getItem(position));
    }
}
