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
