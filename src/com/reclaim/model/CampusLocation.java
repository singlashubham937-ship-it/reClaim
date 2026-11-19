package com.reclaim.model;

public class CampusLocation {
    private int locationId;
    private String locationName;
    private String blockName;
    private String description;

    public CampusLocation(int locationId, String locationName, String blockName, String description) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.blockName = blockName;
        this.description = description;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        if (locationId > 0) {
            this.locationId = locationId;
        }
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        if (locationName != null && !locationName.trim().isEmpty()) {
            this.locationName = locationName;
        }
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        if (blockName != null && !blockName.trim().isEmpty()) {
            this.blockName = blockName;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CampusLocation [locationId=" + locationId + ", locationName=" + locationName 
                + ", blockName=" + blockName + ", description=" + description + "]";
    }
}
