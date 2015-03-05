package com.hardygtw.travelmemories.model;
/**
 * Created by Gavin
 */
public class TravelCompanion {

    private int travelId;
    private String companionName;
    private int placeId;

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public TravelCompanion(int travelId, String companionName, int placeId) {
        this.travelId = travelId;
        this.companionName = companionName;
        this.placeId = placeId;
    }

    public int getTravelId() {
        return travelId;
    }

    public void setTravelId(int travelId) {
        this.travelId = travelId;
    }

    public String getCompanion() {
        return companionName;
    }

    public void setCompanion(String companionName) {
        this.companionName = companionName;
    }
}
