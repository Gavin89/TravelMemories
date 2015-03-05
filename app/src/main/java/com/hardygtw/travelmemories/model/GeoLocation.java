package com.hardygtw.travelmemories.model;

/**
 * Created by beaumoaj on 03/02/15.
 */
import java.io.Serializable;

import com.google.android.gms.maps.model.LatLng;

public class GeoLocation implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1861462299276634548L;
    private double latitude;
    private double longitude;

    /**
     * @return the lat
     */
    public double setLat(double lat) {
        return latitude=lat;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GeoLocation) {
            if (this == o) return true;
            else {
                GeoLocation other = (GeoLocation) o;
                return latitude == other.latitude &&
                        longitude == other.longitude;
            }
        } else {
            return false;
        }
    }


    /**
     * @return the lng
     */
    public double setLng(double lon) {
        return longitude = lon;
    }

    public LatLng getLatLon() {
        return new LatLng(this.latitude, this.longitude);
    }

 /*
    public GeoPoint getGeoPoint() {
        int latE6 = (int) (getLat() * 1e6);
        int lonE6 = (int) (getLng() * 1e6);
        return new GeoPoint(latE6, lonE6);

    }
    */

    public String toString() {
        return "(" + latitude + "," + longitude + ")";
    }
}