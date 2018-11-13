package com.droidbots.areatravelogue;
public class AugmentedPOI {
	private int mId;
	private String mName;
	private String mDescription;
	private double mLatitude;
	private double mLongitude;
	private String freeFormAddress;

    public String getFreeFormAddress() {
        return freeFormAddress;
    }

    public void setFreeFormAddress(String freeFormAddress) {
        this.freeFormAddress = freeFormAddress;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double distance;

	
	public AugmentedPOI(String newName, String newDescription,
						double newLatitude, double newLongitude,String freeFormAddress,double distance) {
		this.mName = newName;
        this.mDescription = newDescription;
        this.mLatitude = newLatitude;
        this.mLongitude = newLongitude;
        this.freeFormAddress = freeFormAddress;
        this.distance = distance;
	}
	
	public int getPoiId() {
		return mId;
	}
	public void setPoiId(int poiId) {
		this.mId = poiId;
	}
	public String getPoiName() {
		return mName;
	}
	public void setPoiName(String poiName) {
		this.mName = poiName;
	}
	public String getPoiDescription() {
		return mDescription;
	}
	public void setPoiDescription(String poiDescription) {
		this.mDescription = poiDescription;
	}
	public double getPoiLatitude() {
		return mLatitude;
	}
	public void setPoiLatitude(double poiLatitude) {
		this.mLatitude = poiLatitude;
	}
	public double getPoiLongitude() {
		return mLongitude;
	}
	public void setPoiLongitude(double poiLongitude) {
		this.mLongitude = poiLongitude;
	}
}
