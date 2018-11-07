package com.droidbots.areatravelogue;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


import com.droidbots.areatravelogue.interfaces.NearbyInterface;
import com.droidbots.areatravelogue.response.NearbyResponse;
import com.google.common.collect.ImmutableList;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.location.LocationUpdateListener;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.Marker;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.Route;
import com.tomtom.online.sdk.map.RouteBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TextBalloonViewAdapter;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.routing.OnlineRoutingApi;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.InstructionsType;
import com.tomtom.online.sdk.routing.data.Report;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.routing.data.RouteResponse;
import com.tomtom.online.sdk.routing.data.TravelMode;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.api.SearchError;
import com.tomtom.online.sdk.search.api.fuzzy.FuzzySearchResultListener;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQuery;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchQueryBuilder;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResponse;
import com.tomtom.online.sdk.search.data.fuzzy.FuzzySearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,

        TomtomMapCallback.OnMapLongClickListener {

    public static TomtomMap tomtomMap;
    Switch ARMode;
    int oneinFiveCount = 0;
    public static double lat, longi;
    public boolean startSet = false, endSet = false;
    LatLng startCord, endCord;
    List<LatLng> routeCordList = new ArrayList<>();
    RoutingApi routePlannerAPI;
    Icon startIcon, endIcon;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTomTomServices();
        initUIViews();
        routePlannerAPI = OnlineRoutingApi.create(this);

    }

    private void initSearchAPI() {
        final SearchApi searchApi = OnlineSearchApi.create(MainActivity.this);
        final EditText eTSearch = findViewById(R.id.eTSearch);

        Button btnSearchGo = findViewById(R.id.btnGo);
        btnSearchGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FuzzySearchQueryBuilder build = new FuzzySearchQueryBuilder(eTSearch.getText().toString());
                LatLng latLng = new LatLng(tomtomMap.getUserLocation().getLatitude(), tomtomMap.getUserLocation().getLongitude());
                FuzzySearchQueryBuilder queryBuilder = build.withIdx("POI").withPosition(latLng);
                FuzzySearchQuery query = queryBuilder.build();

                searchApi.search(query, new FuzzySearchResultListener() {
                    @Override
                    public void onSearchResult(FuzzySearchResponse fuzzySearchResponse) {
                        Log.d("Search", "Success");
                        tomtomMap.clear();
                        tomtomMap.setMyLocationEnabled(true);
                        CameraViewActivity.poiList.clear();
                        ImmutableList<FuzzySearchResult> hmm = fuzzySearchResponse.getResults();
                        for (int i = 0; i < hmm.size(); i++) {
                            Log.d("RESULT", hmm.get(i).toString());
                            CameraViewActivity.poiList.add(new AugmentedPOI(hmm.get(i).getPoi().getName(), hmm.get(i).getPoi().getClassifications()[0].getNames()[0].getName(), hmm.get(i).getPosition().getLatitude(), hmm.get(i).getPosition().getLongitude()));
                        }
                        setBalloons();
                    }

                    @Override
                    public void onSearchError(SearchError searchError) {
                        Log.d("Search", searchError.getMessage());
                    }
                });
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        ARMode.setChecked(false);
    }

    private void setPoints() {
        NearbyInterface nearbyInterface = RetrofitClient.getClient().create(NearbyInterface.class);

        try {
            nearbyInterface.postNearby(String.valueOf(this.tomtomMap.getUserLocation().getLatitude()), String.valueOf(this.tomtomMap.getUserLocation().getLongitude()), 10, "POI", AppUtils.API_KEY).

                    enqueue(new Callback<NearbyResponse>() {
                        @Override
                        public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                            CameraViewActivity.poiList.clear();
                            String toast = "";
                            NearbyResponse t = response.body();
                            for (int i = 0; i < t.getResults().size(); i++) {
                                //toast = t.getResults().get(i).getPoi().getName() + "\n" + toast;
                                CameraViewActivity.poiList.add(new AugmentedPOI(t.getResults().get(i).getPoi().getName(), t.getResults().get(i).getPoi().getCategories().get(0), t.getResults().get(i).getPosition().getLat(), t.getResults().get(i).getPosition().getLon()));
                                Log.d("LAT : " + i, " " + t.getResults().get(i).getPosition().getLat());
                                Log.d("LON : " + i, " " + t.getResults().get(i).getPosition().getLon());
                            }
                            //Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
                            setBalloons();
                            initSearchAPI();
                        }

                        @Override
                        public void onFailure(Call<NearbyResponse> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (NullPointerException e) {
            Log.d("ARea", "Location is currently NULL");
        }
    }

    private void setBalloons() {

        for (int i = 0; i < CameraViewActivity.poiList.size(); i++) {

            LatLng location = new LatLng(CameraViewActivity.poiList.get(i).getPoiLatitude(), CameraViewActivity.poiList.get(i).getPoiLongitude());
            tomtomMap.getMarkerSettings().setMarkerBalloonViewAdapter(new TextBalloonViewAdapter());
            SimpleMarkerBalloon balloon = new SimpleMarkerBalloon(CameraViewActivity.poiList.get(i).getPoiName() + "\n" + CameraViewActivity.poiList.get(i).getPoiDescription());
            MarkerBuilder markerBuilder = new MarkerBuilder(location)
                    .markerBalloon(balloon);

            Marker m = tomtomMap.addMarker(markerBuilder);
            balloon.setText(CameraViewActivity.poiList.get(i).getPoiName() + "\n" + CameraViewActivity.poiList.get(i).getPoiDescription());

        }
    }


    @Override

    public void onMapReady(@NonNull TomtomMap tomtomMap) {
        this.tomtomMap = tomtomMap;

        this.tomtomMap.setMyLocationEnabled(true);
        Log.d("ZOOM", "" + this.tomtomMap.getZoomLevel());
        //tomtomMap.getUiSettings().getCurrentLocationView().updateViewWithMapLocation(tomtomMap.getUserLocation(),tomtomMap.getUserLocation().getLatitude(),tomtomMap.getUserLocation().getLongitude());


        this.tomtomMap.addLocationUpdateListener(new LocationUpdateListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (oneinFiveCount == 0 || oneinFiveCount == 5) {
                    setPoints();
                    MainActivity.tomtomMap.centerOn(
                            MainActivity.tomtomMap.getUserLocation().getLatitude(),
                            MainActivity.tomtomMap.getUserLocation().getLongitude(),
                            15,
                            MapConstants.ORIENTATION_NORTH);

                    oneinFiveCount = 1;
                } else {
                    oneinFiveCount++;

                }

                MainActivity.lat = MainActivity.tomtomMap.getUserLocation().getLatitude();
                MainActivity.longi = MainActivity.tomtomMap.getUserLocation().getLongitude();
            }

        });

        this.tomtomMap.addOnMapLongClickListener(this);

        this.tomtomMap.getMarkerSettings().setMarkersClustering(true);

        this.tomtomMap.setLanguage(Locale.getDefault().getLanguage());


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        this.tomtomMap.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.recreate();


    }


    @Override

    public void onMapLongClick(@NonNull LatLng latLng) {

        /*tomtomMap.getMarkerSettings().setMarkerBalloonViewAdapter(new TextBalloonViewAdapter());
        SimpleMarkerBalloon balloon = new SimpleMarkerBalloon("User Marker");
        MarkerBuilder markerBuilder = new MarkerBuilder(latLng)
                .markerBalloon(balloon);

        Marker m = tomtomMap.addMarker(markerBuilder);
        balloon.setText("User Marker");
        CameraViewActivity.poiList.add(new AugmentedPOI("User Marker","Placed",latLng.getLatitude(),latLng.getLongitude()));*/

        if (endSet) {
            startSet = false;
            endSet = false;
            routeCordList.clear();
            this.tomtomMap.clear();
            tomtomMap.setMyLocationEnabled(true);

        }

        if (!startSet && !endSet) {
            startSet = true;
            startCord = latLng;
            routeCordList.add(latLng);
        } else if (startSet && !endSet) {
            endSet = true;
            endCord = latLng;
            routeCordList.add(latLng);


            RouteQuery queryBuilder = RouteQueryBuilder.create(startCord, endCord)
                    .withMaxAlternatives(0)
                    .withReport(Report.EFFECTIVE_SETTINGS)
                    .withInstructionsType(InstructionsType.TEXT)
                    .withTravelMode(TravelMode.CAR)
                    .withConsiderTraffic(false).build();


            routePlannerAPI.planRoute(queryBuilder)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<RouteResponse>() {
                        @Override
                        public void onSuccess(RouteResponse routeResult) {
                            displayRoutes(routeResult.getRoutes());
                            tomtomMap.displayRoutesOverview();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }
                    });
        }
    }

    private void displayRoutes(List<FullRoute> routes) {
        for (FullRoute fullRoute : routes) {
            Route route = tomtomMap.addRoute(new RouteBuilder(
                    fullRoute.getCoordinates()).startIcon(startIcon).endIcon(endIcon).isActive(true));
        }
    }


    private void initTomTomServices() {
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getAsyncMap(this);
    }


    private void initUIViews() {
        ARMode = (Switch) findViewById(R.id.ARSwitch);
        ARMode.setChecked(false);
        endIcon = Icon.Factory.fromResources(MainActivity.this, R.drawable.end_icon);
        startIcon = Icon.Factory.fromResources(MainActivity.this, R.drawable.start_icon);
        ARMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Intent i = new Intent(MainActivity.this, CameraViewActivity.class);
                    startActivity(i);
                }
            }
        });
    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
