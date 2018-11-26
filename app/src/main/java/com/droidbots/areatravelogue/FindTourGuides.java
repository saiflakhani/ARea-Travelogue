package com.droidbots.areatravelogue;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.TomtomMap;

import java.util.ArrayList;
import java.util.Calendar;

public class FindTourGuides extends Activity implements OnMapReadyCallback {

    TextView tVTime;
    Spinner spinnerType,spinnerHours,spinnerMins;
    ImageButton iBPickTime;
    ImageView iVTourist;
    Button btnBook;
    public static int mHour=-1, mMinute=0;
    public static boolean isTourBooked = false;
    public static String tourType = "";
    TextView tVtime;
    ArrayList<String> mTypes = new ArrayList<>();
    ArrayList<String> mHours = new ArrayList<>();
    ArrayList<String> mMins = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tour_guides);
        initNavBar();
        getIDs();
        setSpinner();
    }

    @Override
    public void onMapReady(@NonNull TomtomMap tomtomMap) {


    }

    public void getIDs() {
        spinnerType = findViewById(R.id.spinnerType);
        btnBook = findViewById(R.id.bookBtn);
        spinnerHours = findViewById(R.id.spinnerPickHour);
        spinnerMins = findViewById(R.id.spinnerPickMin);
        iVTourist = findViewById(R.id.iVTour);
        Picasso.with(this).load("https://www.greecetravel.com/tour-guides/tour-group5.jpg").fit().into(iVTourist);
    }

    public void setSpinner()
    {
        mTypes.add("Tour guide, land package");
        mTypes.add("Tour guide, commute package");
        mTypes.add("Tour guide, day package");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTypes);
        spinnerType.setAdapter(adapter);
        for(int i=8;i<12;i++){
            mHours.add(String.valueOf(i));
        }
        for(int i=12;i<19;i++){
            mHours.add(String.valueOf(i));
        }
        mMins.add("00");
        mMins.add("15");
        mMins.add("30");
        mMins.add("45");
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, mHours);
        ArrayAdapter<String> minAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, mMins);
        spinnerHours.setAdapter(hourAdapter);
        spinnerMins.setAdapter(minAdapter);

        spinnerHours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mHour = Integer.parseInt(mHours.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMinute = Integer.parseInt(mMins.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tourType = mTypes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initNavBar() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationView1);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_maps:
                        finish();
                        return true;
                    case R.id.nav_book_tour_guide:
                        //do nothing
                        return true;
                }
                return false;
            }
        });
    }




    public void book(View v){
        if(mHour!=-1) {
            isTourBooked = true;
            Toast.makeText(FindTourGuides.this, "You have booked a tour!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    }
