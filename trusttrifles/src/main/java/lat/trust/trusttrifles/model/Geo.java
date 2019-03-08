package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

public class Geo {
    private String lat;
    @SerializedName("long")
    private String lng;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String address;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Geo(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Geo(String lat, String lng, String country, String state, String city, String postalCode, String address) {
        this.lat = lat;
        this.lng = lng;
        this.country = country;
        this.state = state;
        this.city = city;
        this.postalCode = postalCode;
        this.address = address;
    }
}
