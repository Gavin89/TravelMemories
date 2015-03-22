package com.hardygtw.travelmemories.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gavin
 */
public class Photo implements Serializable{
    private int photoId;
    String tags;
    int tripId;
    int placeId;
    String path;

    public void setPath(String path) {
        this.path = path;
    }


    public String getPath() {
        return this.path;
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
        if (tags == null) {
            return "";
        } else {
            return tags;
        }
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
}
