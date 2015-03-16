package com.hardygtw.travelmemories.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gavin
 */
public class PlaceVisited {

    private int placeVisitId;
    private String placeName;
    private String dateVisited;
    private LatLng location;
    private String address;
    private String travellerNotes;
    private String travelCompanions;
    private int trip_id;
    private ArrayList<Photo> placePhotos;

    public PlaceVisited() {
        placePhotos = new ArrayList<Photo>();
    }


    public int getPlaceVisitId() {
        return placeVisitId;
    }

    public void setPlaceVisitId(int placeVisitId) {
        this.placeVisitId = placeVisitId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDateVisited() {
        return dateVisited;
    }

    public void setDateVisited(String dateVisited) {
        this.dateVisited = dateVisited;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTravellerNotes() {
        return travellerNotes;
    }

    public void setTravellerNotes(String travellerNotes) {
        this.travellerNotes = travellerNotes;
    }

    public String getTravelCompanions() {
        return travelCompanions;
    }

    public void setTravelCompanions(String travelCompanions) {
        this.travelCompanions = travelCompanions;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public ArrayList<Photo> getPlacePhotos() {
        return placePhotos;
    }

    public void setPlacePhotos(ArrayList<Photo> placePhotos) {
        this.placePhotos = placePhotos;
    }
}
