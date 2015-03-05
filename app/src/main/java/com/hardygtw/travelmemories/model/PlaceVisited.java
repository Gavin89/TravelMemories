package com.hardygtw.travelmemories.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gavin
 */
public class PlaceVisited {

    private int placeVisitedId;
    private Date dateVisited;
    private String travellerNotes;
    private ArrayList<Photo> placePhotos;
    private ArrayList<TravelCompanion> travelCompanions;

    public PlaceVisited(int placeVisitedId, Date dateVisited, String travellerNotes, ArrayList<Photo> placePhotos, ArrayList<TravelCompanion> travelCompanions) {
        this.placeVisitedId = placeVisitedId;
        this.dateVisited = dateVisited;
        this.travellerNotes = travellerNotes;
        this.placePhotos = placePhotos;
        this.travelCompanions = travelCompanions;
    }

    public int getPlacesVisitedId() {
        return placeVisitedId;
    }

    public void setPlacesVisitedId(int placeVisitedId) {
        this.placeVisitedId = placeVisitedId;
    }

    public Date getDateVisited() {
        return dateVisited;
    }

    public void setDateVisited(Date dateVisited) {
        this.dateVisited = dateVisited;
    }

    public String getNotes() {
        return travellerNotes;
    }

    public void setNotes(String notes) {
        this.travellerNotes = notes;
    }

    public ArrayList<Photo> getPlacePhotos() {
        return placePhotos;
    }

    public void setPlacePhotos(ArrayList<Photo> placePhotos) {
        this.placePhotos = placePhotos;
    }

    public ArrayList<TravelCompanion> getTravelCompanions() {
        return travelCompanions;
    }

    public void setTravelCompanions(ArrayList<TravelCompanion> travelCompanions) {
        this.travelCompanions = travelCompanions;
    }
}
