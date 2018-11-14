package com.droidbots.areatravelogue;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.droidbots.areatravelogue.deals.Deal;

import java.util.ArrayList;
import java.util.List;

public class POIAdapter extends ArrayAdapter implements View.OnClickListener {

    private ArrayList<AugmentedPOI> data;
    View prevView = null;
    Context context;

    private static class ViewHolder {
        TextView tVName, tVType, tVDistance, tVAddress;
        CardView cardView;
        Button btnShowStore;
    }

    public POIAdapter(Context context, ArrayList<AugmentedPOI> data) {
        super(context, R.layout.item_bucket_list, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public void onClick(View view) {/*
        if(prevView != null)
            prevView.findViewById(R.id.lLAddress).setVisibility(View.GONE);
        view.findViewById(R.id.lLAddress).setVisibility(View.VISIBLE);
        prevView = view;*/

    }

    private int lastPosition = 0;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        AugmentedPOI poi = (AugmentedPOI) getItem(position);
        ViewHolder viewHolder;
        final View result;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.item_bucket_list, parent, false);
            viewHolder.tVName = (TextView) convertView.findViewById(R.id.tVName);
            viewHolder.tVType = (TextView) convertView.findViewById(R.id.tVType);
            viewHolder.tVDistance = (TextView) convertView.findViewById(R.id.tVDistance);
            viewHolder.tVAddress = (TextView) convertView.findViewById(R.id.tVAddress);
            viewHolder.cardView = (CardView) convertView.findViewById(R.id.cardView);


            result = convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.tVName.setText(poi.getPoiName());
        viewHolder.tVType.setText(poi.getPoiDescription());
        viewHolder.tVDistance.setText(String.format("%.1f", poi.getDistance()) + " m");
        viewHolder.tVAddress.setText(poi.getFreeFormAddress());
        viewHolder.cardView.setOnClickListener(this);
        viewHolder.cardView.setTag(position);

        return convertView;

    }
}
