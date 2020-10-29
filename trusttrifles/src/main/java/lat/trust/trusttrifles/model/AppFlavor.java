package lat.trust.trusttrifles.model;

import com.google.gson.annotations.SerializedName;

public class AppFlavor {

    @SerializedName("id")
    private int id;
    @SerializedName("bundle_id")
    private String bundleId;
    @SerializedName("name")
    private String name;
    @SerializedName("flavor_id")
    private String flavorId;

    public AppFlavor() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlavorId() {
        return flavorId;
    }

    public void setFlavorId(String flavorId) {
        this.flavorId = flavorId;
    }

}
