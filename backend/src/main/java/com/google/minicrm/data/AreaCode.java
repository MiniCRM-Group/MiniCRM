/**
 * This class represents an Area Code with and its estimated latitude and longitude
 */
package com.google.minicrm.data;

public class AreaCode {
    public Double areaCode;
    public Double latitude;
    public Double longitude;

    public AreaCode(Double areaCode, Double latitude, Double longitude) {
        this.areaCode = areaCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}