package com.hardygtw.travelmemories.model;

import java.util.ArrayList;

/**
 * Created by Gavin
 */
public class Photo {
    private int photoId;
    private ArrayList<Tag> tags;
    Long tripId;
    Long placeId;
    String title;

    public Photo(int photoId, ArrayList<Tag> tags, String title) {
        this.photoId = photoId;
        this.tags = tags;
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }


    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}
}
