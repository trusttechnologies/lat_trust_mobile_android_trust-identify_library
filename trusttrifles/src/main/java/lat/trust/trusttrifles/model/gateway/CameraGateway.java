package lat.trust.trusttrifles.model.gateway;

import com.google.gson.annotations.SerializedName;


public class CameraGateway {

    @SerializedName("focal_length")
    private String focalLength;

    @SerializedName("horizontal_view_angle")
    private String horizontalViewAngle;

    @SerializedName("max_exposure_comp")
    private String maxExposureComp;

    @SerializedName("mega_pixels")
    private String megaPixels;

    @SerializedName("min_exposure_comp")
    private String minExposureComp;

    @SerializedName("type")
    private String type;

    @SerializedName("vertical_view_angle")
    private String verticalViewAngle;


    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public String getHorizontalViewAngle() {
        return horizontalViewAngle;
    }

    public void setHorizontalViewAngle(String horizontalViewAngle) {
        this.horizontalViewAngle = horizontalViewAngle;
    }

    public String getMaxExposureComp() {
        return maxExposureComp;
    }

    public void setMaxExposureComp(String maxExposureComp) {
        this.maxExposureComp = maxExposureComp;
    }

    public String getMegaPixels() {
        return megaPixels;
    }

    public void setMegaPixels(String megaPixels) {
        this.megaPixels = megaPixels;
    }

    public String getMinExposureComp() {
        return minExposureComp;
    }

    public void setMinExposureComp(String minExposureComp) {
        this.minExposureComp = minExposureComp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVerticalViewAngle() {
        return verticalViewAngle;
    }

    public void setVerticalViewAngle(String verticalViewAngle) {
        this.verticalViewAngle = verticalViewAngle;
    }
}


