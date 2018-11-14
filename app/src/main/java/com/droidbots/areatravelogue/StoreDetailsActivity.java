package com.droidbots.areatravelogue;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.droidbots.areatravelogue.deals.Deal;
import com.droidbots.areatravelogue.deals.Review;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);

        ListView dealListView = findViewById(R.id.listDeals);
        ListView reviewListView = findViewById(R.id.listReviews);

        TextView storeName = findViewById(R.id.store_name);
        TextView storeAddr = findViewById(R.id.store_addr);

        //
        // int id = getIntent().getExtras().get("UniqueID");

        AugmentedPOI poi = (AugmentedPOI) getIntent().getSerializableExtra("UniqueID");

        storeName.setText(poi.getPoiName());
        storeAddr.setText(poi.getFreeFormAddress());

        List<String> dealListAsString = new ArrayList<>();
        List<String> reviewListAsString = new ArrayList<>();
        List<Deal> dealList = CameraViewActivity.storeList.get(0).getDeals();
        List<Review> reviewList = CameraViewActivity.storeList.get(0).getReviews();

        for(int i=0; i<dealList.size(); i++) {
            dealListAsString.add(dealList.get(i).getTitle() + "\n" + dealList.get(i).getDescription());
        }

        for(int i=0; i<reviewList.size(); i++) {
            reviewListAsString.add(Html.fromHtml("<b>" + reviewList.get(i).getTitle() + "</b>") + "\t\t" + reviewList.get(i).getRating() + "/5\n" + reviewList.get(i).getBody() + "\n- " +
                    reviewList.get(i).getUsername());
        }


        ArrayAdapter dealAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, dealListAsString);

        dealListView.setAdapter(dealAdapter);
        dealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StoreDetailsActivity.this);

                builder.setMessage("Your coupon code is RDX5J2E. Please visit the store and show them this coupon.")
                        .setTitle("Deal Successfully Redeemed");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //User clicked button
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        ArrayAdapter reviewAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, reviewListAsString);

        reviewListView.setAdapter(reviewAdapter);

    }


}
