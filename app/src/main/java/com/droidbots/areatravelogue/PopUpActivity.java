package com.droidbots.areatravelogue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PopUpActivity extends Activity {

    private static POIAdapter poiAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_layout);

        LinearLayout lLSheet = (LinearLayout) findViewById(R.id.lLSheet);
        lLSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ListView lVPOI;
        lVPOI = (ListView) findViewById(R.id.lVPOI);

        Intent i = getIntent();
        ArrayList<AugmentedPOI> augmentedPOIList = (ArrayList<AugmentedPOI>) i.getSerializableExtra("List");

        poiAdapter = new POIAdapter(getApplicationContext(), augmentedPOIList);
        lVPOI.setAdapter(poiAdapter);
        lVPOI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }

}
