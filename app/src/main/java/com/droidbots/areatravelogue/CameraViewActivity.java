package com.droidbots.areatravelogue;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.droidbots.areatravelogue.deals.Guide;
import com.droidbots.areatravelogue.deals.Store;
import com.tomtom.online.sdk.location.LocationUpdateListener;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.Instruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraViewActivity extends Activity implements
		SurfaceHolder.Callback, OnAzimuthChangedListener , View.OnClickListener {

    public static ArrayList<AugmentedPOI> poiList = new ArrayList<>();
    public static Guide assignedGuide = new Guide();
    private static double AZIMUTH_ACCURACY = 15;
    private static POIAdapter poiAdapter;
    //private AugmentedPOI mPoi;
public static List<Store> storeList = new ArrayList<>();
        List<AugmentedPOI> bucketListCenter = new ArrayList<>();
    List<AugmentedPOI> bucketListLeft = new ArrayList<>();
    List<AugmentedPOI> bucketListRight = new ArrayList<>();
    public static List<AugmentedPOI> displayBucketList = null;
    TextView descriptionTextView, tVLeft, tVCenter, tVRight, tVToLeft, tVToRight;
    Button btnCenter, btnLeft, btnRight;
    BottomSheetBehavior sheetBehavior;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private boolean isCameraviewOn = false;
    private double mAzimuthReal = 0;
    private double mAzimuthTeoretical = 0;
    private double mMyLatitude;
    private double mMyLongitude;
    private MyCurrentAzimuth myCurrentAzimuth;
    Instruction[] instructions = null;
    int instructionCount = 0;
    LinearLayout lLPois;
    RelativeLayout lLInstructions;
    ImageView iVInstruction;
    TextView tVNextMove, tVNextMoveDist;
    Button btnPrevInstruction, btnNextInstruction;
    int prevDist = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setupListeners();
        setupLayout();

        LinearLayout lLSheet = findViewById(R.id.lLSheet);
        sheetBehavior = BottomSheetBehavior.from(lLSheet);


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                ListView lVPOI;
                lVPOI = (ListView) findViewById(R.id.lVPOI);
                poiAdapter = new POIAdapter(getApplicationContext(), (ArrayList) displayBucketList);
                lVPOI.setAdapter(poiAdapter);
            }
        });

        mMyLatitude = MainActivity.lat;
        mMyLongitude = MainActivity.longi;

        btnCenter = (Button) findViewById(R.id.btnCenter);
        btnCenter.setOnClickListener(this);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(this);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnRight.setOnClickListener(this);

        tVToLeft = (TextView) findViewById(R.id.tVToLeft);
        tVToRight = (TextView) findViewById(R.id.tVToRight);
        tVLeft = (TextView) findViewById(R.id.tVLeft);
        tVRight = (TextView) findViewById(R.id.tVRight);
        tVCenter = (TextView) findViewById(R.id.tVCenter);

        btnPrevInstruction = findViewById(R.id.getPrevInstruction);
        btnPrevInstruction.setOnClickListener(this);
        btnNextInstruction = findViewById(R.id.getNextInstruction);
        btnNextInstruction.setOnClickListener(this);
        iVInstruction = findViewById(R.id.iVInstruction);
        lLInstructions = findViewById(R.id.lLInstruction);
        lLPois = findViewById(R.id.lLPois);

        tVNextMove = findViewById(R.id.tVNextMove);
        tVNextMoveDist = findViewById(R.id.tVNextMoveDist);


        if(MainActivity.currentRoute != null) {
            List<FullRoute> route = MainActivity.currentRoute;
            instructions = route.get(0).getGuidance().getInstructions();
            lLInstructions.setVisibility(View.VISIBLE);
            lLPois.setVisibility(View.GONE);
            String maneuver = "";
            if (instructionCount < instructions.length)
                tVNextMove.setText(instructions[instructionCount].getMessage());
                tVNextMoveDist.setText("Now");
                prevDist = instructions[instructionCount].getRouteOffsetInMeters();
                maneuver = instructions[instructionCount].getManeuver();
            if (maneuver.contains("TURN_LEFT"))
                iVInstruction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nav_arrow_left));
            else if (maneuver.contains("TURN_RIGHT"))
                iVInstruction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nav_arrow_right));
            else if (maneuver.contains("ARRIVE") || maneuver.contains("DEPART") || maneuver.contains("KEEP"))
                iVInstruction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nav_arrow_up));
            else if (maneuver.contains("MAKE_UTURN"))
                iVInstruction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nav_arrow_down));

        } else {
            lLInstructions.setVisibility(View.GONE);
            lLPois.setVisibility(View.VISIBLE);
            instructions = null;
        }

    }

    public double calculateTeoreticalAzimuth(AugmentedPOI mPoi) {

        double dX = mPoi.getPoiLatitude() - mMyLatitude;
        double dY = mPoi.getPoiLongitude() - mMyLongitude;

        double phiAngle;
        double tanPhi;
        double azimuth = 0;

        tanPhi = Math.abs(dY / dX);
        phiAngle = Math.atan(tanPhi);
        phiAngle = Math.toDegrees(phiAngle);

        if (dX > 0 && dY > 0) { // I quater
            return azimuth = phiAngle;
        } else if (dX < 0 && dY > 0) { // II
            return azimuth = 180 - phiAngle;
        } else if (dX < 0 && dY < 0) { // III
            return azimuth = 180 + phiAngle;
        } else if (dX > 0 && dY < 0) { // IV
            return azimuth = 360 - phiAngle;
        }

        return phiAngle;
    }

    private List<Double> calculateAzimuthAccuracy(double azimuth) {
        double minAngle = azimuth - AZIMUTH_ACCURACY;
        double maxAngle = azimuth + AZIMUTH_ACCURACY;
        List<Double> minMax = new ArrayList<Double>();
        double q1 = (azimuth + minAngle) / 2;
        double q3 = (azimuth + maxAngle) / 2;

        if (minAngle < 0)
            minAngle += 360;

        if (maxAngle >= 360)
            maxAngle -= 360;

        if (q1 < 0)
            q1 += 360;

        if (q3 >= 360)
            q3 -= 360;


        minMax.clear();
        minMax.add(minAngle);
        minMax.add(q1);
        minMax.add(azimuth);
        minMax.add(q3);
        minMax.add(maxAngle);
        return minMax;
    }


    private boolean isBetween(double minAngle, double maxAngle, double azimuth) {
        if (minAngle > maxAngle) {
            if (isBetween(0, maxAngle, azimuth) && isBetween(minAngle, 360, azimuth))
                return true;
        } else {
            if (azimuth > minAngle && azimuth < maxAngle)
                return true;
        }
        return false;
    }

    private void updateDescription(AugmentedPOI mPoi) {
        descriptionTextView.setText(mPoi.getPoiName() + " azimuthTeoretical "
                + mAzimuthTeoretical + " azimuthReal " + mAzimuthReal + " latitude "
                + mMyLatitude + " longitude " + mMyLongitude);
    }

	/*@Override
	public void onLocationChanged(Location location) {

		mMyLatitude = location.getLatitude();
		mMyLongitude = location.getLongitude();
	}*/


    @Override
    public void onAzimuthChanged(float azimuthChangedFrom, float azimuthChangedTo) {
        mAzimuthReal = azimuthChangedTo;

        btnCenter.setVisibility(View.INVISIBLE);
        btnLeft.setVisibility(View.INVISIBLE);
        btnRight.setVisibility(View.INVISIBLE);
        tVLeft.setText("");
        tVCenter.setText("");
        tVRight.setText("");
        tVToLeft.setText("");
        tVToRight.setText("");

        int toLeft = 0, toRight = 0;

        bucketListCenter.clear();
        bucketListLeft.clear();
        bucketListRight.clear();

        if (poiList.size() != 0) {
            for (int i = 0; i < poiList.size(); i++) {
                mAzimuthTeoretical = calculateTeoreticalAzimuth(poiList.get(i));

                //pointerIcon = (ImageView) findViewById(R.id.icon);

                List<Double> benchmarks = calculateAzimuthAccuracy(mAzimuthTeoretical);

                double minAngle = benchmarks.get(0);
                double q1 = benchmarks.get(1);
                double azimuth = benchmarks.get(2);
                double q3 = benchmarks.get(3);
                double maxAngle = benchmarks.get(4);


                if (mAzimuthReal < minAngle) toLeft++;
                else if (mAzimuthReal > maxAngle) toRight++;
                else if (isBetween(minAngle, q1, mAzimuthReal)) {
                    bucketListLeft.add(poiList.get(i));
                } else if (isBetween(q1, q3, mAzimuthReal)) {
                    bucketListCenter.add(poiList.get(i));
                } else if (isBetween(q3, maxAngle, mAzimuthReal)) {
                    bucketListRight.add(poiList.get(i));
                }

                if (displayBucketList == null) displayBucketList = bucketListCenter;

                int s;
                if ((s = bucketListLeft.size()) > 0) {
                    btnLeft.setVisibility(View.VISIBLE);
                    tVLeft.setText(bucketListLeft.get(0).getPoiName() + " + " + (s - 1) + " others\nIn " + String.format("%.1f",bucketListLeft.get(0).getDistance()) + "m");
                } else if ((s = bucketListCenter.size()) > 0) {
                    btnCenter.setVisibility(View.VISIBLE);
                    tVCenter.setText(bucketListCenter.get(0).getPoiName() + " + " + (s - 1) + " others\nIn " + String.format("%.1f",bucketListCenter.get(0).getDistance()) + "m");
                } else if ((s = bucketListRight.size()) > 0) {
                    btnRight.setVisibility(View.VISIBLE);
                    tVRight.setText(bucketListRight.get(0).getPoiName() + " + " + (s - 1) + " others\nIn " + String.format("%.1f",bucketListRight.get(0).getDistance()) + "m");
                } /*else if(toLeft > 0) tVToLeft.setText(toLeft + "");
				else if(toRight > 0) tVToRight.setText(toRight + "");
*/
                String op = "List 1\n";
                for (AugmentedPOI poi : bucketListRight)
                    op += poi.getPoiName() + "\n";

                op += "List 2\n";
                for (AugmentedPOI poi : bucketListCenter)
                    op += poi.getPoiName() + "\n";
                op += "List 3\n";
                for (AugmentedPOI poi : bucketListLeft)
                    op += poi.getPoiName() + "\n";

                //descriptionTextView.setText(op);
            }

            //updateDescription(poiList.get(i));
        }
    }

    @Override
    protected void onStop() {
        myCurrentAzimuth.stop();
        //myCurrentLocation.stop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myCurrentAzimuth.start();
        //myCurrentLocation.start();
    }

    private void setupListeners() {
        MainActivity.tomtomMap.addLocationUpdateListener(new LocationUpdateListener() {
            @Override
            public void onLocationChanged(Location location) {

                mMyLatitude = location.getLatitude();
                mMyLongitude = location.getLongitude();
                Log.d("LOCATION CHANGED", String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()));
            }
        });

        myCurrentAzimuth = new MyCurrentAzimuth(this, this);
        myCurrentAzimuth.start();
    }

    private void setupLayout() {
        descriptionTextView = (TextView) findViewById(R.id.cameraTextView);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraview);
        mSurfaceHolder = surfaceView.getHolder();

        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Switch ArSwitch = findViewById(R.id.ARSwitch);
        ArSwitch.setChecked(true);
        ArSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    finish();
                }
            }
        });


        //setContentView(view);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (isCameraviewOn) {
            mCamera.stopPreview();
            isCameraviewOn = false;
        }

        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
                isCameraviewOn = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        isCameraviewOn = false;
    }


    @Override
    public void onClick(View view) {
        String maneuver = "";
        ListView lVPOI;
        switch (view.getId()) {
            case R.id.btnCenter:
                displayBucketList = bucketListCenter;
                lVPOI = findViewById(R.id.lVPOI);
                poiAdapter = new POIAdapter(getApplicationContext(), (ArrayList) displayBucketList);
                lVPOI.setAdapter(poiAdapter);

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.btnLeft:
                displayBucketList = bucketListLeft;
                lVPOI = findViewById(R.id.lVPOI);
                poiAdapter = new POIAdapter(getApplicationContext(), (ArrayList) displayBucketList);
                lVPOI.setAdapter(poiAdapter);

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.btnRight:
                displayBucketList = bucketListRight;
                lVPOI = findViewById(R.id.lVPOI);
                poiAdapter = new POIAdapter(getApplicationContext(), (ArrayList) displayBucketList);
                lVPOI.setAdapter(poiAdapter);

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.getNextInstruction:
                if (instructionCount < instructions.length - 1) {
                    tVNextMove.setText(instructions[++instructionCount].getMessage());
                    tVNextMoveDist.setText("In " + (instructions[instructionCount].getRouteOffsetInMeters() - prevDist) + "m");
                    prevDist = instructions[instructionCount].getRouteOffsetInMeters();
                    maneuver = instructions[instructionCount].getManeuver();
                }
                break;
            case R.id.getPrevInstruction:
                if (instructionCount >= 2) {
                    --instructionCount;
                    tVNextMove.setText(instructions[instructionCount].getMessage());
                    tVNextMoveDist.setText("In " + (prevDist - instructions[instructionCount-1].getRouteOffsetInMeters()) + "m");
                    prevDist = instructions[instructionCount-1].getRouteOffsetInMeters();
                    maneuver = instructions[instructionCount].getManeuver();
                }
                break;
        }

        if (maneuver.contains("TURN_LEFT"))
            iVInstruction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nav_arrow_left));
        else if (maneuver.contains("TURN_RIGHT"))
            iVInstruction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nav_arrow_right));
        else if (maneuver.contains("ARRIVE") || maneuver.contains("DEPART") || maneuver.contains("KEEP"))
            iVInstruction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nav_arrow_up));
        else if (maneuver.contains("MAKE_UTURN"))
            iVInstruction.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nav_arrow_down));


    }

    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else finish();

    }
}
