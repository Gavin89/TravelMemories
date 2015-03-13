package com.hardygtw.travelmemories.model;

import java.util.ArrayList;

/**
 * Created by Gavin
 */
public class Photo {
    private int photoId;
    String tags;
    int tripId;
    int placeId;
    String title;
    String path;

    public void setPath(String path) {
        this.path = path;
    }


    public String getPath() {
        return this.path;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }


    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
}
