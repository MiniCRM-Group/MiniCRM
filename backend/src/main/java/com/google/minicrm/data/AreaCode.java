/**
 * This class represents an Area Code with and its estimated latitude and longitude
 */
package com.google.minicrm.data;

public class AreaCode {
    public Double areaCodes;
    public Double latitude;
    public Double longitude;

    public AreaCode(Double areaCodes, Double latitude, Double longitude) {
        this.areaCodes = areaCodes;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}