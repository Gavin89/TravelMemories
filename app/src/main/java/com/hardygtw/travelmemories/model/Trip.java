package com.hardygtw.travelmemories.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gavin.
 */
public class Trip {

	private int tripId;
    private ArrayList<PlaceVisited> placesVisited;
    private ArrayList<Photo> tripPhotos;
    private String tripName;
    private Date startDate;
    private Date endDate;
    private String notes;

    public Trip(ArrayList<PlaceVisited> placesVisited, ArrayList<Photo> tripPhotos, String tripName, Date startDate, Date endDate, String notes) {
    	this.placesVisited = placesVisited;
        this.tripPhotos = tripPhotos;
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

	public int getTripId() {
		return tripId;
	}

	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

	public ArrayList<PlaceVisited> getPlacesVisited() {
		return placesVisited;
	}

	public void setPlacesVisited(ArrayList<PlaceVisited> placesVisited) {
		this.placesVisited = placesVisited;
	}

	public ArrayList<Photo> getTripPhotos() {
		return tripPhotos;
	}

	public void setTripPhotos(ArrayList<Photo> tripPhotos) {
		this.tripPhotos = tripPhotos;
	}
}
